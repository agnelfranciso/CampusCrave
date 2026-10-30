package com.campuscrave.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.campuscrave.app.data.local.SampleData
import com.campuscrave.app.data.model.CartItem
import com.campuscrave.app.data.model.Category
import com.campuscrave.app.data.model.CustomizationOption
import com.campuscrave.app.data.model.DayScheduleType
import com.campuscrave.app.data.model.FoodItem
import com.campuscrave.app.data.model.Order
import com.campuscrave.app.data.model.OrderStatus
import com.campuscrave.app.data.model.ScheduleState
import com.campuscrave.app.data.model.StudentProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class CampusCraveRepository(context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("campuscrave_prefs", Context.MODE_PRIVATE)

    private val _hasCompletedOnboarding = MutableStateFlow(
        prefs?.getBoolean("has_completed_onboarding", false) ?: false
    )
    val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

    fun setOnboardingCompleted(completed: Boolean) {
        _hasCompletedOnboarding.value = completed
        prefs?.edit()?.putBoolean("has_completed_onboarding", completed)?.apply()
    }

    private val _categories = MutableStateFlow(SampleData.categories)
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _foodItems = MutableStateFlow(SampleData.foodItems)
    val foodItems: StateFlow<List<FoodItem>> = _foodItems.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem(SampleData.foodItems[0], 1), // Chicken Biryani
            CartItem(SampleData.foodItems[8], 1), // Chitato Supreme
            CartItem(SampleData.foodItems[9], 1)  // Iced Matcha
        )
    )
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _profile = MutableStateFlow(StudentProfile())
    val profile: StateFlow<StudentProfile> = _profile.asStateFlow()

    private val _scheduleState = MutableStateFlow(ScheduleState())
    val scheduleState: StateFlow<ScheduleState> = _scheduleState.asStateFlow()

    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    private val _orderHistory = MutableStateFlow<List<Order>>(SampleData.samplePastOrders)
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<String>>(setOf("cb_thalassery", "iced_matcha"))
    val wishlistIds: StateFlow<Set<String>> = _wishlistIds.asStateFlow()

    // Cart Operations
    fun addToCart(
        foodItem: FoodItem,
        quantity: Int = 1,
        selectedCustomizations: List<CustomizationOption> = emptyList(),
        specialInstruction: String = ""
    ) {
        _cartItems.update { current ->
            val existingIndex = current.indexOfFirst {
                it.foodItem.id == foodItem.id && it.selectedCustomizations == selectedCustomizations
            }
            if (existingIndex != -1) {
                current.mapIndexed { index, item ->
                    if (index == existingIndex) item.copy(quantity = item.quantity + quantity)
                    else item
                }
            } else {
                current + CartItem(
                    foodItem = foodItem,
                    quantity = quantity,
                    specialInstruction = specialInstruction,
                    selectedCustomizations = selectedCustomizations
                )
            }
        }
    }

    fun updateCartQuantity(foodItemId: String, delta: Int) {
        _cartItems.update { current ->
            current.mapNotNull { item ->
                if (item.foodItem.id == foodItemId) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else item
            }
        }
    }

    fun removeFromCart(foodItemId: String) {
        _cartItems.update { current ->
            current.filterNot { it.foodItem.id == foodItemId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // Wishlist Toggle
    fun toggleWishlist(foodItemId: String) {
        _wishlistIds.update { set ->
            if (set.contains(foodItemId)) set - foodItemId else set + foodItemId
        }
    }

    fun activateCravePass(plan: String) {
        _profile.update {
            it.copy(
                hasCravePass = true,
                cravePassPlan = plan
            )
        }
    }

    // Place Order Flow
    fun placeOrder(
        paymentMethod: String,
        runnerNote: String
    ): Order {
        val currentCart = _cartItems.value
        val subtotal = currentCart.sumOf { it.totalPrice }
        val currentProf = _profile.value
        val deliveryFee = when {
            currentProf.hasCravePass && currentProf.cravePassPlan == "Pro" -> 0.0
            currentProf.hasCravePass && currentProf.cravePassPlan == "Essential" -> 7.0
            else -> 15.0
        }
        val packagingFee = 10.0
        val total = subtotal + deliveryFee + packagingFee
        val randomOrderNumber = Random.nextInt(84920, 85999)
        val randomPin = Random.nextInt(1000, 9999).toString()
        val runner = SampleData.demoRunners.random()

        val newOrder = Order(
            orderId = "CC-$randomOrderNumber",
            items = currentCart,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            packagingFee = packagingFee,
            totalAmount = total,
            classroom = _profile.value.fullClassroomString,
            runnerNote = runnerNote.ifBlank { "Drop quietly outside door" },
            paymentMethod = paymentMethod,
            timestamp = _scheduleState.value.simulatedTimeText,
            status = OrderStatus.ORDER_PLACED,
            runnerName = runner.name,
            runnerRating = runner.rating,
            runnerRole = runner.role,
            runnerPhotoUrl = runner.photoUrl,
            runnerPhone = runner.phone,
            runnerRunsCompleted = runner.runsCompleted,
            deliveryPin = randomPin
        )

        _activeOrder.value = newOrder
        _orderHistory.update { listOf(newOrder) + it }
        _cartItems.value = emptyList()

        // Deduct from wallet if wallet was used
        if (paymentMethod.contains("Wallet", ignoreCase = true)) {
            _profile.update {
                it.copy(
                    walletBalance = (it.walletBalance - total).coerceAtLeast(0.0),
                    totalOrders = it.totalOrders + 1,
                    cravePoints = it.cravePoints + (total / 10).toInt()
                )
            }
        } else {
            _profile.update {
                it.copy(
                    totalOrders = it.totalOrders + 1,
                    cravePoints = it.cravePoints + (total / 10).toInt()
                )
            }
        }

        return newOrder
    }

    // Advance Order Status (for Demo)
    fun advanceActiveOrderStatus(): OrderStatus? {
        val current = _activeOrder.value ?: return null
        val nextStatus = current.status.next()
        if (nextStatus != null) {
            val updated = current.copy(status = nextStatus)
            _activeOrder.value = updated
            _orderHistory.update { history ->
                history.map { if (it.orderId == updated.orderId) updated else it }
            }
            return nextStatus
        }
        return null
    }

    fun stepBackActiveOrderStatus(): OrderStatus? {
        val current = _activeOrder.value ?: return null
        val prevStatus = current.status.prev()
        if (prevStatus != null) {
            val updated = current.copy(status = prevStatus)
            _activeOrder.value = updated
            _orderHistory.update { history ->
                history.map { if (it.orderId == updated.orderId) updated else it }
            }
            return prevStatus
        }
        return null
    }

    fun setActiveOrderDirect(order: Order) {
        _activeOrder.value = order
    }

    fun clearActiveOrder() {
        _activeOrder.value = null
    }

    // Reorder from history
    fun reorder(order: Order) {
        _cartItems.value = order.items
    }

    // Profile Operations
    fun selectSavedDropoff(id: String) {
        _profile.update { current ->
            val target = current.savedDropoffs.find { it.id == id } ?: return@update current
            current.copy(
                block = target.block,
                room = target.room,
                bench = target.bench,
                savedDropoffs = current.savedDropoffs.map { it.copy(isDefault = it.id == id) }
            )
        }
    }

    fun addSavedDropoff(title: String, block: String, room: String, bench: String = "Door Pickup") {
        _profile.update { current ->
            val cleanTitle = title.ifBlank { "$block Room $room" }
            val cleanBench = bench.ifBlank { "Door $room" }
            val newDropoff = com.campuscrave.app.data.model.SavedDropoff(
                id = "drop_${System.currentTimeMillis()}",
                title = cleanTitle,
                block = block,
                room = room,
                bench = cleanBench,
                isDefault = true
            )
            current.copy(
                block = newDropoff.block,
                room = newDropoff.room,
                bench = newDropoff.bench,
                savedDropoffs = current.savedDropoffs.map { it.copy(isDefault = false) } + newDropoff
            )
        }
    }

    fun deleteSavedDropoff(id: String) {
        _profile.update { current ->
            val remaining = current.savedDropoffs.filterNot { it.id == id }
            if (remaining.isEmpty()) current
            else {
                val wasDefault = current.savedDropoffs.find { it.id == id }?.isDefault == true
                val newActive = if (wasDefault) remaining.first() else null
                current.copy(
                    block = newActive?.block ?: current.block,
                    room = newActive?.room ?: current.room,
                    bench = newActive?.bench ?: current.bench,
                    savedDropoffs = remaining.map { it.copy(isDefault = if (newActive != null) it.id == newActive.id else it.isDefault) }
                )
            }
        }
    }

    fun updateProfile(
        name: String,
        email: String,
        rollNumber: String,
        department: String,
        year: String,
        semester: String,
        division: String,
        block: String,
        room: String,
        bench: String
    ) {
        _profile.update { current ->
            // Also ensure active dropoff is in sync
            val updatedSaved = current.savedDropoffs.map { drop ->
                if (drop.isDefault) {
                    drop.copy(block = block, room = room, bench = bench)
                } else drop
            }
            current.copy(
                name = name,
                email = email,
                rollNumber = rollNumber,
                department = department,
                academicYear = year,
                semester = semester,
                division = division,
                block = block,
                room = room,
                bench = bench,
                savedDropoffs = updatedSaved
            )
        }
    }

    fun topUpWallet(amount: Double = 100.0) {
        _profile.update { it.copy(walletBalance = it.walletBalance + amount) }
    }

    // Demo Controls for Schedule
    fun setSimulatedTime(timeString: String, minutes: Int, countdownMins: Int, countdownSecs: Int) {
        _scheduleState.update {
            it.copy(
                simulatedTimeText = timeString,
                simulatedMinutesFromMidnight = minutes,
                customCountdownMinutes = countdownMins,
                customCountdownSeconds = countdownSecs
            )
        }
    }

    fun setScheduleDayType(dayType: DayScheduleType) {
        _scheduleState.update { it.copy(dayType = dayType) }
    }

    fun toggleSmartStop() {
        _scheduleState.update { it.copy(smartStopForced = !it.smartStopForced) }
    }

    fun resetDemoData() {
        _cartItems.value = listOf(
            CartItem(SampleData.foodItems[0], 1),
            CartItem(SampleData.foodItems[8], 1),
            CartItem(SampleData.foodItems[9], 1)
        )
        _profile.value = StudentProfile()
        _scheduleState.value = ScheduleState()
        _activeOrder.value = null
        _orderHistory.value = SampleData.samplePastOrders
    }
}

