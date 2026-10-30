package com.campuscrave.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.campuscrave.app.data.model.CartItem
import com.campuscrave.app.data.model.Category
import com.campuscrave.app.data.model.DayScheduleType
import com.campuscrave.app.data.model.FoodItem
import com.campuscrave.app.data.model.Order
import com.campuscrave.app.data.model.OrderStatus
import com.campuscrave.app.data.model.ScheduleState
import com.campuscrave.app.data.model.StudentProfile
import com.campuscrave.app.data.repository.CampusCraveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class CampusCraveViewModel(
    private val repository: CampusCraveRepository
) : ViewModel() {

    val categories: StateFlow<List<Category>> = repository.categories
    val foodItems: StateFlow<List<FoodItem>> = repository.foodItems
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val profile: StateFlow<StudentProfile> = repository.profile
    val scheduleState: StateFlow<ScheduleState> = repository.scheduleState
    val activeOrder: StateFlow<Order?> = repository.activeOrder
    val orderHistory: StateFlow<List<Order>> = repository.orderHistory
    val wishlistIds: StateFlow<Set<String>> = repository.wishlistIds
    val hasCompletedOnboarding: StateFlow<Boolean> = repository.hasCompletedOnboarding

    fun completeOnboarding() {
        repository.setOnboardingCompleted(true)
    }

    fun resetOnboarding() {
        repository.setOnboardingCompleted(false)
    }

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedTab = MutableStateFlow<com.campuscrave.app.ui.navigation.BottomTab>(com.campuscrave.app.ui.navigation.BottomTab.Explore)
    val selectedTab: StateFlow<com.campuscrave.app.ui.navigation.BottomTab> = _selectedTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDietFilter = MutableStateFlow("all") // all, veg, protein, under_100, fast
    val selectedDietFilter: StateFlow<String> = _selectedDietFilter.asStateFlow()

    private val _selectedFoodItem = MutableStateFlow<FoodItem?>(null)
    val selectedFoodItem: StateFlow<FoodItem?> = _selectedFoodItem.asStateFlow()

    private val _showDemoControls = MutableStateFlow(false)
    val showDemoControls: StateFlow<Boolean> = _showDemoControls.asStateFlow()

    private val _showClassroomDialog = MutableStateFlow(false)
    val showClassroomDialog: StateFlow<Boolean> = _showClassroomDialog.asStateFlow()

    private val _showCravePassDialog = MutableStateFlow(false)
    val showCravePassDialog: StateFlow<Boolean> = _showCravePassDialog.asStateFlow()

    private val _tabHistory = mutableListOf<com.campuscrave.app.ui.navigation.BottomTab>()

    // Filtered food items based on category, search and dietary tag
    val filteredFoodItems: StateFlow<List<FoodItem>> = combine(
        foodItems,
        _selectedCategory,
        _searchQuery,
        _selectedDietFilter
    ) { items, cat, query, diet ->
        items.filter { item ->
            val matchesCategory = (cat == "all" || item.categoryId == cat)
            val matchesQuery = query.isBlank() ||
                    item.name.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.subtitle.contains(query, ignoreCase = true)
            val matchesDiet = when (diet) {
                "veg" -> item.isPureVeg
                "protein" -> item.isHighProtein
                "under_100" -> item.price <= 100.0
                "fast" -> item.prepTimeMinutes <= 2
                else -> true
            }
            matchesCategory && matchesQuery && matchesDiet
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartCount: StateFlow<Int> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSubtotal: StateFlow<Double> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotal: StateFlow<Double> = combine(cartSubtotal, profile) { subtotal, prof ->
        if (subtotal > 0.0) {
            val deliveryFee = when {
                prof.hasCravePass && prof.cravePassPlan == "Pro" -> 0.0
                prof.hasCravePass && prof.cravePassPlan == "Essential" -> 7.0
                else -> 15.0
            }
            subtotal + deliveryFee + 10.0
        } else 0.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun selectCategory(id: String) {
        _selectedCategory.value = id
    }

    fun setSelectedTab(tab: com.campuscrave.app.ui.navigation.BottomTab) {
        if (_selectedTab.value != tab) {
            _tabHistory.add(_selectedTab.value)
            _selectedTab.value = tab
        }
    }

    fun navigateBackTab(): Boolean {
        if (_tabHistory.isNotEmpty()) {
            val prev = _tabHistory.removeAt(_tabHistory.lastIndex)
            _selectedTab.value = prev
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDietFilter(filter: String) {
        _selectedDietFilter.value = filter
    }

    fun selectFoodItem(item: FoodItem?) {
        _selectedFoodItem.value = item
    }

    fun addToCart(
        foodItem: FoodItem,
        quantity: Int = 1,
        selectedCustomizations: List<com.campuscrave.app.data.model.CustomizationOption> = emptyList(),
        specialInstruction: String = ""
    ) {
        repository.addToCart(foodItem, quantity, selectedCustomizations, specialInstruction)
    }

    fun setCravePassDialogVisible(show: Boolean) {
        _showCravePassDialog.value = show
    }

    fun activateCravePass(plan: String) {
        repository.activateCravePass(plan)
    }

    fun updateCartQuantity(foodItemId: String, delta: Int) {
        repository.updateCartQuantity(foodItemId, delta)
    }

    fun removeFromCart(foodItemId: String) {
        repository.removeFromCart(foodItemId)
    }

    fun clearCart() {
        repository.clearCart()
    }

    fun toggleWishlist(foodItemId: String) {
        repository.toggleWishlist(foodItemId)
    }

    fun placeOrder(paymentMethod: String, runnerNote: String): Order {
        return repository.placeOrder(paymentMethod, runnerNote)
    }

    fun advanceOrderStatus(): OrderStatus? {
        return repository.advanceActiveOrderStatus()
    }

    fun stepBackOrderStatus(): OrderStatus? {
        return repository.stepBackActiveOrderStatus()
    }

    fun setActiveOrderDirect(order: Order) {
        repository.setActiveOrderDirect(order)
    }

    fun clearActiveOrder() {
        repository.clearActiveOrder()
    }

    fun reorder(order: Order) {
        repository.reorder(order)
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
        repository.updateProfile(
            name, email, rollNumber, department, year, semester, division, block, room, bench
        )
    }

    fun topUpWallet(amount: Double = 100.0) {
        repository.topUpWallet(amount)
    }

    fun setSimulatedTime(timeString: String, minutes: Int, countdownMins: Int, countdownSecs: Int) {
        repository.setSimulatedTime(timeString, minutes, countdownMins, countdownSecs)
    }

    fun setScheduleDayType(dayType: DayScheduleType) {
        repository.setScheduleDayType(dayType)
    }

    fun toggleSmartStop() {
        repository.toggleSmartStop()
    }

    fun resetDemoData() {
        repository.resetDemoData()
    }

    fun toggleDemoControls(show: Boolean? = null) {
        _showDemoControls.value = show ?: !_showDemoControls.value
    }

    fun setClassroomDialogVisible(show: Boolean) {
        _showClassroomDialog.value = show
    }

    fun selectSavedDropoff(id: String) {
        repository.selectSavedDropoff(id)
    }

    fun addSavedDropoff(title: String, block: String, room: String, bench: String = "Door Pickup") {
        repository.addSavedDropoff(title, block, room, bench)
    }

    fun deleteSavedDropoff(id: String) {
        repository.deleteSavedDropoff(id)
    }

    fun updateClassroom(block: String, room: String, bench: String) {
        val current = profile.value
        repository.updateProfile(
            name = current.name,
            email = current.email,
            rollNumber = current.rollNumber,
            department = current.department,
            year = current.academicYear,
            semester = current.semester,
            division = current.division,
            block = block,
            room = room,
            bench = bench
        )
    }

    class Factory(private val repository: CampusCraveRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CampusCraveViewModel(repository) as T
        }
    }
}
