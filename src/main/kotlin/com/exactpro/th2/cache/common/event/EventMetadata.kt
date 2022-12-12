package com.exactpro.th2.cache.common.event

import com.google.protobuf.Timestamp


class EventMetadata (
    val eventId: String?,
    val parentId: String?,
    val eventName: String?,
    val eventType: String?,
    val successful: String?,
    val startTimestamp: Timestamp?
)
