package com.exactpro.th2.cache.common.event

import java.time.Instant


class EventMetadata (
    val eventId: String?,
    val parentId: String?,
    val eventName: String?,
    val eventType: String?,
    val successful: String?,
    val startTimestamp: Instant?
)
