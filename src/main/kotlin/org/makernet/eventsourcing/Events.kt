package org.makernet.eventsourcing

import java.time.Instant

sealed interface Event

data class ProductShipped(val sku: String, val quantity: Int, val dateTime: Instant) : Event

data class ProductReceived(val sku: String, val quantity: Int, val dateTime: Instant) : Event

data class InventoryAdjusted(
    val sku: String,
    val quantity: Int,
    val reason: String,
    val dateTime: Instant
) : Event