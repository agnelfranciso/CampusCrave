package com.campuscrave.app.data.model

data class DemoRunner(
    val id: String,
    val name: String,
    val rating: Double,
    val role: String,
    val runsCompleted: Int,
    val photoUrl: String,
    val phone: String
)

data class Order(
    val orderId: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double = 15.0,
    val packagingFee: Double = 10.0,
    val totalAmount: Double,
    val classroom: String,
    val runnerNote: String = "Drop quietly outside door",
    val paymentMethod: String = "Campus Crave Wallet",
    val timestamp: String = "12:22 PM",
    val status: OrderStatus = OrderStatus.ORDER_PLACED,
    val runnerName: String = "Marcus K.",
    val runnerRating: Double = 4.9,
    val runnerRole: String = "Student Courier #14 • Mechanical 3rd Year",
    val runnerPhotoUrl: String = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&auto=format&fit=crop&q=80",
    val runnerPhone: String = "+91 98471 28491",
    val runnerRunsCompleted: Int = 342,
    val deliveryPin: String = "4821",
    val estimatedArrival: String = "12:32 PM",
    val indoorStepDetail: String = "Level 4 Corridor • 30m away"
)
