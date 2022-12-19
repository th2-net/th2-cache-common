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

package com.exactpro.th2.cache.common

import com.arangodb.ArangoDB
import com.arangodb.ArangoDBException
import com.arangodb.ArangoDatabase
import com.arangodb.DbName
import com.arangodb.mapping.ArangoJack
import com.arangodb.model.AqlQueryOptions
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.Instant
import java.util.function.Consumer

class Arango(credentials: ArangoCredentials) : AutoCloseable {
    private val logger = LoggerFactory.getLogger(Arango::class.java)
    private val arangoDB = ArangoDB.Builder()
        .serializer(ArangoJack().apply {
            configure {
                it.registerModule(KotlinModule.Builder().build())
                it.registerModule(JavaTimeModule())
            }
        })
        .host(credentials.host, credentials.port)
        .user(credentials.username)
        .password(credentials.password)
        .build()
    private val db = arangoDB.db(DbName.of(credentials.database))

    fun getDatabase(): ArangoDatabase =  db

    fun <T> executeAqlQuery(query: String, clazz: Class<T>): List<T> {db
        logger.debug("Executing AQL query: $query")
        try {
            val result = mutableListOf<T>()
            executeAqlQuery(query, clazz) { doc -> result.add(doc) }
            return result
        } catch (e: ArangoDBException) {
            logger.error("Failed to execute query: $query", e)
            throw e;
        }
    }

    fun <T> executeAqlQuery(query: String, clazz: Class<T>, action: Consumer<T>) {
        val cursor = db.query(
            query, null as AqlQueryOptions?,
            clazz
        )
        cursor.forEachRemaining(action) // Handle each document using action
        logger.debug("Fetched records: ${cursor.count}")
    }

    override fun close() {
        arangoDB.shutdown()
    }

    companion object {
        const val EVENT_COLLECTION = "events"
        const val EVENT_GRAPH = "event_graph"
        const val EVENT_EDGES = "event_relations"
        const val RAW_MESSAGE_COLLECTION = "raw_messages"
        const val PARSED_MESSAGE_COLLECTION = "parsed_messages"
    }
}

fun toArangoTimestamp(timestamp: Instant): Long =  timestamp.epochSecond shl 32 + timestamp.nano

fun toInstant(timestamp: Long) : Instant = Instant.ofEpochSecond(timestamp shr 32, timestamp and 0xffffffff)

