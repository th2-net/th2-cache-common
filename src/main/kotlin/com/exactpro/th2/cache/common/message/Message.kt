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

package com.exactpro.th2.cache.common.message

import com.arangodb.entity.Key

data class RawMessage (
    @Key
    val id: String,
    val book: String,
    val group: String,
    val sessionAlias: String,
    val direction: String,
    val sequence: Long,
    val subsequence: List<Int>,
    val timestamp: Long,
    val sessionId: String,
    val attachedEventIds: Set<String>,
    @Suppress("ArrayInDataClass")
    val rawMessageBody: ByteArray,
    val imageType: String?,
    val metadata: MessageMetadata
)

data class ParsedMessage (
    @Key
    val id: String,
    val book: String,
    val group: String,
    val sessionAlias: String,
    val direction: String,
    val sequence: Long,
    val subsequence: List<Int>,
    val timestamp: Long,
    val sessionId: String,
    val attachedEventIds: Set<String>,
    val parsedMessageGroup: List<BodyWrapper>?,
    val imageType: String?,
    val metadata: MessageMetadata
)
