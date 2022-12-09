/*
 * Copyright 2022 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.exactpro.th2.cachecommon.db

import com.arangodb.ArangoDB
import com.arangodb.ArangoDBException
import com.arangodb.DbName
import com.arangodb.mapping.ArangoJack
import com.arangodb.model.AqlQueryOptions
import com.exactpro.th2.cachecommon.entities.configuration.Configuration
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.function.Consumer

class Arango(cfg: Configuration) : AutoCloseable {
    private val logger = LoggerFactory.getLogger(Arango::class.java)
    private val arango = ArangoDB.Builder()
        .serializer(ArangoJack().apply {
            configure {
                it.registerModule(KotlinModule.Builder().build())
                it.registerModule(JavaTimeModule())
            }
        })
        .host(cfg.arangoHost.value, cfg.arangoPort.value.toInt())
        .user(cfg.arangoUser.value).password(cfg.arangoPassword.value)
        .build()
    private val db = arango.db(DbName.of(cfg.arangoDbName.value))


    fun <T> executeAqlQuery(query: String, clazz: Class<T>): List<T> {
        logger.debug("AQL query to execute: $query")
        val result = mutableListOf<T>()
        executeAqlQuery(query, clazz) { aDocument ->
            result.add(aDocument)
        }
        return result
    }

    fun <T> executeAqlQuery(query: String, clazz: Class<T>, action: Consumer<T>) {
        logger.debug("AQL query to execute: $query")
        try {
            val cursor = db.query(
                query, null as AqlQueryOptions?,
                clazz
            )
            cursor.forEachRemaining(action) // Handle each document using action
            logger.debug("Returned record count: ${cursor.count}")
        } catch (e: ArangoDBException) {
            logger.error("Failed to execute query. " + e.message)
        }
    }

    override fun close() {
        arango.shutdown()
    }
}
