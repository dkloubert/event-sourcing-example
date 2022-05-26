package org.makernet.eventsourcing

import java.time.temporal.ChronoUnit

fun main() {
    val warehouseProductRepository = WarehouseProductRepository()
    var selection = ""
    while(selection != "x") {
        displayMenu()
        selection = readln().lowercase()
        handleSelection(selection, warehouseProductRepository)
    }
}


private fun displayMenu() {
    println("Warehouse Operation")
    println("-------------------")
    println("R - Receive Inventory")
    println("S - Ship Inventory")
    println("A - Adjust Inventory")
    println("Q - Display Quantity")
    println("E - Events")
    println("X - Quit")
    print("> ")
}

private fun handleSelection(selection: String, warehouseProductRepository: WarehouseProductRepository) {
    if(selection == "x") return
    val sku = getSkuFromConsole()
    val warehouseProduct = warehouseProductRepository.get(sku)
    when(selection) {
        "r" -> {
            getQuantity()?.also {  quantity ->
                warehouseProduct.receiveProduct(quantity)
                println("$sku Received: $quantity")
            }
        }
        "s" -> {
            getQuantity()?.also { quantity ->
                warehouseProduct.shipProduct(quantity)
                println("$sku Shipped: $quantity")
            }
        }
        "a" -> {
            getQuantity()?.also { quantity ->
                print("Reason: ")
                val reason = readln()
                warehouseProduct.adjustInventory(quantity, reason)
                println("$sku Adjusted: $quantity $reason")
            }
        }
        "q" -> {
            val quantityOnHand = warehouseProduct.quantityOnHand
            println("$sku Quantity On Hand: $quantityOnHand")
        }
        "e" -> {
            println("Events: $sku")
            warehouseProduct.getEvents().forEach {event ->
                when(event) {
                    is InventoryAdjusted -> println("${event.dateTime.truncatedTo(ChronoUnit.SECONDS)} $sku Adjusted: ${event.quantity} ${event.reason}")
                    is ProductReceived -> println("${event.dateTime.truncatedTo(ChronoUnit.SECONDS)} $sku Received: ${event.quantity}")
                    is ProductShipped -> println("${event.dateTime.truncatedTo(ChronoUnit.SECONDS)} $sku Shipped: ${event.quantity}")
                }
            }
        }
    }

    warehouseProductRepository.save(warehouseProduct)
    readln()
    println()
}

private fun getQuantity(): Int? {
    print("Quantity: ")
    return readln().toIntOrNull()
}

private fun getSkuFromConsole(): String {
    println()
    print("SKU: ")
    return readln()
}