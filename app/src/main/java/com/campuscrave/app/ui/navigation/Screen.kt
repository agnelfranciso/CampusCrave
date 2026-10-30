package com.campuscrave.app.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object StudentAccess : Screen("student_access")
    object Main : Screen("main")
    object FoodDetails : Screen("food_details/{foodId}") {
        fun createRoute(foodId: String) = "food_details/$foodId"
    }
    object OrderConfirmation : Screen("order_confirmation/{orderId}") {
        fun createRoute(orderId: String) = "order_confirmation/$orderId"
    }
    object OrderHistory : Screen("order_history")
}

enum class BottomTab(val route: String, val title: String, val iconName: String) {
    Explore("tab_explore", "Explore", "local_mall"),
    Search("tab_search", "Search", "manage_search"),
    Cart("tab_cart", "Cart", "shopping_bag"),
    Track("tab_track", "Track", "near_me"),
    Profile("tab_profile", "Profile", "account_circle");

    companion object {
        val items: List<BottomTab> = entries.toList()
    }
}
