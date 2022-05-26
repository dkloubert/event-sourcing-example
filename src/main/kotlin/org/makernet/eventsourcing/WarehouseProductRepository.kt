package org.makernet.eventsourcing

class WarehouseProductRepository {

    private val inMemoryStreams: MutableMap<String, List<Event>> = mutableMapOf()

    fun get(sku: String): WarehouseProduct {
        val warehouseProduct = WarehouseProduct(sku)

        inMemoryStreams[sku]?.forEach { event ->
            warehouseProduct.addEvent(event)
        }

        return warehouseProduct
    }

    fun save(warehouseProduct: WarehouseProduct) {
        inMemoryStreams[warehouseProduct.sku] = warehouseProduct.getEvents()
    }
}