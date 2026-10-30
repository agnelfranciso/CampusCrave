package com.campuscrave.app.data.model

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1,
    val specialInstruction: String = "",
    val selectedCustomizations: List<CustomizationOption> = emptyList()
) {
    val unitPrice: Double
        get() = foodItem.price + selectedCustomizations.sumOf { it.additionalPrice }

    val totalPrice: Double
        get() = unitPrice * quantity

    val customizationSummary: String
        get() = selectedCustomizations.joinToString(" • ") { opt ->
            if (opt.additionalPrice > 0) "${opt.name} (+₹${opt.additionalPrice.toInt()})" else opt.name
        }
}
