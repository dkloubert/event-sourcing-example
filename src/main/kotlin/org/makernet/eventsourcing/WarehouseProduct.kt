package org.makernet.eventsourcing

import java.time.Instant

class WarehouseProduct(val sku: String) {

    private val events: MutableList<Event> = mutableListOf()
    private val currentState: WarehouseProductState = WarehouseProductState()

    fun addEvent(event: Event) {
        when (event) {
            is ProductShipped -> apply(event)
            is InventoryAdjusted -> apply(event)
            is ProductReceived -> apply(event)
        }
        events.add(event)
    }

    private fun apply(event: ProductReceived) {
        currentState.quantityOnHand += event.quantity
    }

    private fun apply(event: InventoryAdjusted) {
        currentState.quantityOnHand += event.quantity
    }

    private fun apply(event: ProductShipped) {
        currentState.quantityOnHand -= event.quantity
    }

    fun getEvents(): List<Event> {
        return events.toList()
    }

    fun shipProduct(quantity: Int) {
        if (quantity > currentState.quantityOnHand) {
            throw UnsupportedOperationException("Ah... we don't have enough on hand!")
        }

        addEvent(ProductShipped(sku, quantity, Instant.now()))
    }

    fun receiveProduct(quantity: Int) {
        addEvent(ProductReceived(sku, quantity, Instant.now()))
    }

    fun adjustInventory(quantity: Int, reason: String) {
        if (quantity + currentState.quantityOnHand < 0) {
            throw UnsupportedOperationException("Negative quantities are not allowed")
        }
        addEvent(InventoryAdjusted(sku, quantity, reason, Instant.now()))
    }

    val quantityOnHand: Int
        get() = currentState.quantityOnHand
}

data class WarehouseProductState(var quantityOnHand: Int = 0)
