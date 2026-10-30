package com.campuscrave.app.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.campuscrave.app.ui.components.CampusCravePassDialog
import com.campuscrave.app.ui.components.DemoControlsSheet
import com.campuscrave.app.ui.components.EditClassroomDialog
import com.campuscrave.app.ui.components.FloatingBottomNav
import com.campuscrave.app.ui.screens.auth.StudentAccessScreen
import com.campuscrave.app.ui.screens.cart.CartCheckoutScreen
import com.campuscrave.app.ui.screens.confirmation.OrderConfirmationScreen
import com.campuscrave.app.ui.screens.details.FoodDetailsScreen
import com.campuscrave.app.ui.screens.discovery.FoodDiscoveryScreen
import com.campuscrave.app.ui.screens.history.OrderHistoryScreen
import com.campuscrave.app.ui.screens.home.HomeScreen
import com.campuscrave.app.ui.screens.profile.StudentProfileScreen
import com.campuscrave.app.ui.screens.tracking.LiveTrackingScreen
import com.campuscrave.app.ui.screens.welcome.WelcomeScreen
import com.campuscrave.app.ui.viewmodel.CampusCraveViewModel

@Composable
fun CampusCraveNavGraph(
    viewModel: CampusCraveViewModel,
    navController: NavHostController = rememberNavController()
) {
    val currentTab by viewModel.selectedTab.collectAsState()
    val cartItemCount by viewModel.cartCount.collectAsState()
    val scheduleState by viewModel.scheduleState.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val foodItems by viewModel.foodItems.collectAsState()
    val filteredFoodItems by viewModel.filteredFoodItems.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val cartSubtotal by viewModel.cartSubtotal.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()
    val activeOrder by viewModel.activeOrder.collectAsState()
    val orderHistory by viewModel.orderHistory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedDietFilter by viewModel.selectedDietFilter.collectAsState()
    val showDemoControls by viewModel.showDemoControls.collectAsState()
    val showClassroomDialog by viewModel.showClassroomDialog.collectAsState()
    val showCravePassDialog by viewModel.showCravePassDialog.collectAsState()
    val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (hasCompletedOnboarding) Screen.Main.route else Screen.Welcome.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(280)
            ) + fadeIn(animationSpec = tween(280))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(280)
            ) + fadeOut(animationSpec = tween(280))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(280)
            ) + fadeIn(animationSpec = tween(280))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(280)
            ) + fadeOut(animationSpec = tween(280))
        }
    ) {
        // Welcome Screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(Screen.StudentAccess.route)
                },
                onLoginClick = {
                    viewModel.completeOnboarding()
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Student Access / Registration Screen
        composable(Screen.StudentAccess.route) {
            StudentAccessScreen(
                profile = profile,
                onBackClick = {
                    navController.popBackStack()
                },
                onVerifyClick = { name, email, roll, dept, year, sem, div, block, room, bench ->
                    viewModel.completeOnboarding()
                    viewModel.updateProfile(
                        name = name,
                        email = email,
                        rollNumber = roll,
                        department = dept,
                        year = year,
                        semester = sem,
                        division = div,
                        block = block,
                        room = room,
                        bench = bench
                    )
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Main Screen with Floating Bottom Tabs
        composable(Screen.Main.route) {
            // System back gesture from inside Main tabs returns to previous tab or Explore instead of closing app
            BackHandler(enabled = currentTab != BottomTab.Explore) {
                if (!viewModel.navigateBackTab()) {
                    viewModel.setSelectedTab(BottomTab.Explore)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Crossfade(
                    targetState = currentTab,
                    animationSpec = tween(durationMillis = 220),
                    label = "TabContentCrossfade"
                ) { tab ->
                    when (tab) {
                        BottomTab.Explore -> {
                            HomeScreen(
                                profile = profile,
                                scheduleState = scheduleState,
                                categories = categories,
                                selectedCategory = selectedCategory,
                                foodItems = foodItems,
                                wishlistIds = wishlistIds,
                                onCategorySelect = { viewModel.selectCategory(it) },
                                onSearchClick = { viewModel.setSelectedTab(BottomTab.Search) },
                                onFoodClick = { item ->
                                    viewModel.selectFoodItem(item)
                                    navController.navigate(Screen.FoodDetails.createRoute(item.id))
                                },
                                onAddToCart = { item -> viewModel.addToCart(item) },
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onEditLocationClick = { viewModel.setClassroomDialogVisible(true) },
                                onAvatarClick = { viewModel.setSelectedTab(BottomTab.Profile) },
                                onExplorePassClick = { viewModel.setCravePassDialogVisible(true) }
                            )
                        }
                        BottomTab.Search -> {
                            FoodDiscoveryScreen(
                                scheduleState = scheduleState,
                                categories = categories,
                                selectedCategory = selectedCategory,
                                searchQuery = searchQuery,
                                selectedDietFilter = selectedDietFilter,
                                filteredFoodItems = filteredFoodItems,
                                wishlistIds = wishlistIds,
                                onCategorySelect = { viewModel.selectCategory(it) },
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onDietFilterChange = { viewModel.setDietFilter(it) },
                                onFoodClick = { item ->
                                    viewModel.selectFoodItem(item)
                                    navController.navigate(Screen.FoodDetails.createRoute(item.id))
                                },
                                onAddToCart = { item -> viewModel.addToCart(item) },
                                onWishlistToggle = { viewModel.toggleWishlist(it) },
                                onViewCartClick = { viewModel.setSelectedTab(BottomTab.Cart) },
                                onAvatarClick = { viewModel.setSelectedTab(BottomTab.Profile) }
                            )
                        }
                        BottomTab.Cart -> {
                            CartCheckoutScreen(
                                profile = profile,
                                scheduleState = scheduleState,
                                cartItems = cartItems,
                                cartSubtotal = cartSubtotal,
                                cartTotal = cartTotal,
                                onUpdateQuantity = { id, delta -> viewModel.updateCartQuantity(id, delta) },
                                onClearCart = { viewModel.clearCart() },
                                onPlaceOrder = { paymentMethod, note ->
                                    val order = viewModel.placeOrder(paymentMethod, note)
                                    navController.navigate(Screen.OrderConfirmation.createRoute(order.orderId))
                                },
                                onEditLocationClick = { viewModel.setClassroomDialogVisible(true) },
                                onBrowseFoodClick = {
                                    if (!viewModel.navigateBackTab()) {
                                        viewModel.setSelectedTab(BottomTab.Explore)
                                    }
                                },
                                onAvatarClick = { viewModel.setSelectedTab(BottomTab.Profile) }
                            )
                        }
                        BottomTab.Track -> {
                            LiveTrackingScreen(
                                order = activeOrder,
                                scheduleState = scheduleState,
                                onAdvanceOrderStatus = { viewModel.advanceOrderStatus() },
                                onOrderHistoryClick = { navController.navigate(Screen.OrderHistory.route) },
                                onBrowseFoodClick = { viewModel.setSelectedTab(BottomTab.Explore) },
                                onAvatarClick = { viewModel.setSelectedTab(BottomTab.Profile) },
                                onClearActiveOrder = { viewModel.clearActiveOrder() }
                            )
                        }
                        BottomTab.Profile -> {
                            StudentProfileScreen(
                                profile = profile,
                                scheduleState = scheduleState,
                                onTopUpWallet = { viewModel.topUpWallet(it) },
                                onUpdateClassroom = { block, room, bench ->
                                    viewModel.updateProfile(
                                        name = profile.name,
                                        email = profile.email,
                                        rollNumber = profile.rollNumber,
                                        department = profile.department,
                                        year = profile.academicYear,
                                        semester = profile.semester,
                                        division = profile.division,
                                        block = block,
                                        room = room,
                                        bench = bench
                                    )
                                },
                                onOrderHistoryClick = { navController.navigate(Screen.OrderHistory.route) },
                                onOpenDemoControls = { viewModel.toggleDemoControls(true) },
                                onLogoutClick = {
                                    viewModel.resetOnboarding()
                                    navController.navigate(Screen.Welcome.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                onExplorePassClick = { viewModel.setCravePassDialogVisible(true) },
                                onOpenDropoffDialog = { viewModel.setClassroomDialogVisible(true) }
                            )
                        }
                    }
                }

                // Floating Bottom Island Nav Bar:
                // Visible on Explore, Search, Track, Profile.
                // On Cart screen: visible when empty, slides out when cart has items!
                val isBottomNavVisible = (currentTab != BottomTab.Cart) || cartItems.isEmpty()
                AnimatedVisibility(
                    visible = isBottomNavVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) + fadeIn(animationSpec = tween(200)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = spring(
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) + fadeOut(animationSpec = tween(150)),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    FloatingBottomNav(
                        currentTab = currentTab,
                        onTabSelected = { viewModel.setSelectedTab(it) },
                        cartItemCount = cartItemCount
                    )
                }
            }
        }

        // Food Details Screen
        composable(
            route = Screen.FoodDetails.route,
            arguments = listOf(navArgument("foodId") { type = NavType.StringType })
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId")
            val item = foodItems.find { it.id == foodId } ?: foodItems.first()
            val isWishlisted = wishlistIds.contains(item.id)

            FoodDetailsScreen(
                foodItem = item,
                allFoodItems = foodItems,
                isWishlisted = isWishlisted,
                cartItemCount = cartItemCount,
                scheduleState = scheduleState,
                onBackClick = { navController.popBackStack() },
                onWishlistToggle = { viewModel.toggleWishlist(item.id) },
                onCartClick = {
                    viewModel.setSelectedTab(BottomTab.Cart)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                },
                onAddToCart = { food, qty, customOpts ->
                    viewModel.addToCart(food, qty, customOpts)
                }
            )
        }

        // Order Confirmation Screen
        composable(
            route = Screen.OrderConfirmation.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            val order = activeOrder ?: orderHistory.find { it.orderId == orderId } ?: orderHistory.first()

            OrderConfirmationScreen(
                order = order,
                onTrackOrderClick = {
                    viewModel.setSelectedTab(BottomTab.Track)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onContinueBrowsingClick = {
                    viewModel.setSelectedTab(BottomTab.Explore)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }

        // Order History Screen
        composable(Screen.OrderHistory.route) {
            OrderHistoryScreen(
                orders = orderHistory,
                onBackClick = { navController.popBackStack() },
                onViewOrderDetails = { order ->
                    viewModel.setActiveOrderDirect(order)
                    viewModel.setSelectedTab(BottomTab.Track)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                },
                onReorder = { order ->
                    viewModel.reorder(order)
                    viewModel.setSelectedTab(BottomTab.Cart)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                },
                onBrowseFoodClick = {
                    viewModel.setSelectedTab(BottomTab.Explore)
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) { inclusive = false }
                    }
                }
            )
        }
    }

    // Modal Bottom Sheet for Demo Controls
    if (showDemoControls) {
        DemoControlsSheet(
            scheduleState = scheduleState,
            activeOrder = activeOrder,
            onDismiss = { viewModel.toggleDemoControls(false) },
            onSetTime = { label, hour, min, sec ->
                viewModel.setSimulatedTime(label, hour * 60 + min, min, sec)
            },
            onToggleDayType = { dayType ->
                viewModel.setScheduleDayType(dayType)
            },
            onToggleSmartStop = {
                viewModel.toggleSmartStop()
            },
            onAdvanceOrderStatus = {
                viewModel.advanceOrderStatus()
            },
            onStepBackOrderStatus = {
                viewModel.stepBackOrderStatus()
            },
            onResetDemoData = {
                viewModel.resetDemoData()
            }
        )
    }

    // Modal Dialog for Editing & Selecting Delivery Classroom
    if (showClassroomDialog) {
        EditClassroomDialog(
            profile = profile,
            onDismiss = { viewModel.setClassroomDialogVisible(false) },
            onSelectSavedDropoff = { id ->
                viewModel.selectSavedDropoff(id)
                viewModel.setClassroomDialogVisible(false)
            },
            onAddSavedDropoff = { title, block, room, bench ->
                viewModel.addSavedDropoff(title, block, room, bench)
                viewModel.setClassroomDialogVisible(false)
            },
            onDeleteSavedDropoff = { id ->
                viewModel.deleteSavedDropoff(id)
            },
            onSave = { block, room, bench ->
                viewModel.updateClassroom(block, room, bench)
                viewModel.setClassroomDialogVisible(false)
            }
        )
    }

    // Modal Dialog for Campus CravePass
    if (showCravePassDialog) {
        CampusCravePassDialog(
            currentPlan = profile.cravePassPlan,
            onDismiss = { viewModel.setCravePassDialogVisible(false) },
            onActivatePlan = { plan ->
                viewModel.activateCravePass(plan)
            }
        )
    }
}
