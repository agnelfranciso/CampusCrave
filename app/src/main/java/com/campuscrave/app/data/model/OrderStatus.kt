package com.campuscrave.app.data.model

enum class OrderStatus(
    val stepIndex: Int,
    val title: String,
    val description: String,
    val iconName: String
) {
    ORDER_PLACED(
        1,
        "Order Placed",
        "Pre-order ticket registered at Canteen",
        "receipt_long"
    ),
    ACCEPTED_BY_CANTEEN(
        2,
        "Accepted by Canteen",
        "Kitchen confirmed batch slot",
        "done"
    ),
    PREPARING(
        3,
        "Food Being Prepared",
        "Warm preparation in progress",
        "skillet"
    ),
    READY_FOR_COLLECTION(
        4,
        "Ready for Collection",
        "Packed in thermal insulated container #4",
        "inventory_2"
    ),
    RUNNER_ASSIGNED(
        5,
        "Delivery Partner Assigned",
        "Marcus K. (#14 CS Senior) on standby",
        "person_pin"
    ),
    COLLECTED(
        6,
        "Collected from Canteen",
        "Dispatched from canteen hub locker",
        "shopping_bag"
    ),
    OUT_FOR_DELIVERY(
        7,
        "Out for Delivery",
        "Corridor runner ascending Block B stairwell",
        "directions_run"
    ),
    DELIVERED(
        8,
        "Delivered to Desk",
        "Delivered safely to your classroom desk",
        "verified"
    );

    val displayName: String get() = title

    fun next(): OrderStatus? {
        val entries = entries
        val currentIndex = ordinal
        return if (currentIndex < entries.size - 1) entries[currentIndex + 1] else null
    }

    fun prev(): OrderStatus? {
        val entries = entries
        val currentIndex = ordinal
        return if (currentIndex > 0) entries[currentIndex - 1] else null
    }
}

