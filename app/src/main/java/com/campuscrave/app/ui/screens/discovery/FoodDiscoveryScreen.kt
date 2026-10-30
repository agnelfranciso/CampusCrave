package com.campuscrave.app.ui.screens.discovery

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campuscrave.app.data.model.Category
import com.campuscrave.app.data.model.FoodItem
import com.campuscrave.app.data.model.ScheduleState
import com.campuscrave.app.ui.components.FoodCard
import com.campuscrave.app.ui.components.TopCampusHeader
import com.campuscrave.app.ui.theme.CanvasBackground
import com.campuscrave.app.ui.theme.ElectricEmerald
import com.campuscrave.app.ui.theme.EmeraldContainer
import com.campuscrave.app.ui.theme.EmeraldDark
import com.campuscrave.app.ui.theme.EmeraldSecondary
import com.campuscrave.app.ui.theme.ObsidianPrimary
import com.campuscrave.app.ui.theme.OutlineBorder
import com.campuscrave.app.ui.theme.SurfaceCard
import com.campuscrave.app.ui.theme.SurfaceContainerLow
import com.campuscrave.app.ui.theme.TextMuted
import com.campuscrave.app.ui.theme.TextPrimary
import com.campuscrave.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDiscoveryScreen(
    scheduleState: ScheduleState,
    categories: List<Category>,
    selectedCategory: String,
    searchQuery: String,
    selectedDietFilter: String,
    filteredFoodItems: List<FoodItem>,
    wishlistIds: Set<String>,
    onCategorySelect: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onDietFilterChange: (String) -> Unit,
    onFoodClick: (FoodItem) -> Unit,
    onAddToCart: (FoodItem) -> Unit,
    onWishlistToggle: (String) -> Unit,
    onViewCartClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Interactive Slider Filter Sheet state
    var showFilterSheet by remember { mutableStateOf(false) }
    var maxPriceFilter by remember { mutableStateOf(250f) }
    var maxPrepTimeFilter by remember { mutableStateOf(15f) }
    var isPureVegOnly by remember { mutableStateOf(false) }
    var isHalalOnly by remember { mutableStateOf(false) }
    var isHighProteinOnly by remember { mutableStateOf(false) }

    val isAnyFilterActive = maxPriceFilter < 240f || maxPrepTimeFilter < 14f || isPureVegOnly || isHalalOnly || isHighProteinOnly

    val finalFoodItems = remember(
        filteredFoodItems,
        maxPriceFilter,
        maxPrepTimeFilter,
        isPureVegOnly,
        isHalalOnly,
        isHighProteinOnly
    ) {
        filteredFoodItems.filter { item ->
            item.price <= maxPriceFilter &&
            item.prepTimeMinutes <= maxPrepTimeFilter.toInt() &&
            (!isPureVegOnly || item.isPureVeg) &&
            (!isHalalOnly || item.isHalal) &&
            (!isHighProteinOnly || item.isHighProtein)
        }
    }

    LaunchedEffect(Unit) {
        delay(200)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    fun showToast(msg: String) {
        toastMessage = msg
        scope.launch {
            delay(2000)
            toastMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(CanvasBackground)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopCampusHeader(
                title = "Search & Filter",
                scheduleState = scheduleState,
                onAvatarClick = onAvatarClick
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 110.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Search Bar Field with clear & filter button
                item(span = { GridItemSpan(2) }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(999.dp),
                                spotColor = ObsidianPrimary.copy(alpha = 0.05f)
                            )
                            .clip(RoundedCornerShape(999.dp))
                            .border(1.dp, OutlineBorder, RoundedCornerShape(999.dp)),
                        color = SurfaceCard
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                placeholder = {
                                    Text(
                                        "Search meals, biryani, brews, snacks...",
                                        fontSize = 13.sp,
                                        color = TextMuted
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Search
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        keyboardController?.hide()
                                    }
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                )
                            )

                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = { onSearchQueryChange("") },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isAnyFilterActive) ElectricEmerald else ObsidianPrimary)
                                    .clickable { showFilterSheet = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Filter Sliders",
                                    tint = if (isAnyFilterActive) ObsidianPrimary else SurfaceCard,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // 2. Category Product Types Reel
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Product types",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "View menu",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { category ->
                                val isSelected = category.id == selectedCategory
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(
                                            1.dp,
                                            if (isSelected) ObsidianPrimary else OutlineBorder,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .clickable { onCategorySelect(category.id) },
                                    color = if (isSelected) ObsidianPrimary else SurfaceCard
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        val icon = when (category.id) {
                                            "all" -> Icons.Default.Restaurant
                                            "hot_meals" -> Icons.Default.LunchDining
                                            "snacks" -> Icons.Default.Fastfood
                                            "brews" -> Icons.Default.LocalCafe
                                            "bakery" -> Icons.Default.BakeryDining
                                            else -> Icons.Default.Restaurant
                                        }
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = category.name,
                                            tint = if (isSelected) SurfaceCard else EmeraldDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = category.name,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) SurfaceCard else TextPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Delivery Window & Active Meta
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "${filteredFoodItems.size} items",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "for 12:30 slot",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = null,
                                tint = EmeraldSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Express Rush",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                            )
                        }
                    }
                }

                // 4. Diet & Filter Chips Carousel
                item(span = { GridItemSpan(2) }) {
                    val dietFilters = listOf(
                        "all" to "All",
                        "veg" to "Pure Veg",
                        "protein" to "High Protein",
                        "under_100" to "Under ₹100",
                        "fast" to "<3 min prep"
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(dietFilters) { (key, label) ->
                            val isSelected = selectedDietFilter == key
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .border(
                                        1.dp,
                                        if (isSelected) ObsidianPrimary else OutlineBorder,
                                        RoundedCornerShape(999.dp)
                                    )
                                    .clickable { onDietFilterChange(key) },
                                color = if (isSelected) ObsidianPrimary else SurfaceCard
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) SurfaceCard else TextPrimary
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // 5. Food Grid
                if (finalFoodItems.isEmpty()) {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(44.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "No canteen items match your filters",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Try increasing max price or adjusting preparation time sliders",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        maxPriceFilter = 250f
                                        maxPrepTimeFilter = 15f
                                        isPureVegOnly = false
                                        isHalalOnly = false
                                        isHighProteinOnly = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = ObsidianPrimary),
                                    shape = RoundedCornerShape(999.dp)
                                ) {
                                    Text("Reset Sliders", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(finalFoodItems, key = { it.id }) { item ->
                        FoodCard(
                            foodItem = item,
                            isWishlisted = wishlistIds.contains(item.id),
                            onWishlistToggle = { onWishlistToggle(item.id) },
                            onAddToCart = {
                                onAddToCart(item)
                                showToast("Added ${item.name} to tray")
                            },
                            onCardClick = { onFoodClick(item) }
                        )
                    }
                }
            }
        }

        // Express Floating Toast Alert
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { 80 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { 80 }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp, start = 20.dp, end = 20.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(999.dp)),
                shape = RoundedCornerShape(999.dp),
                color = ObsidianPrimary
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ElectricEmerald)
                        )
                        Text(
                            text = toastMessage ?: "",
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = SurfaceCard,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Text(
                        text = "View cart →",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = ElectricEmerald,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable(onClick = onViewCartClick)
                    )
                }
            }
        }

        // Interactive Filter Bottom Sheet with Price & Prep Time Sliders
        if (showFilterSheet) {
            val filterSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = filterSheetState,
                containerColor = SurfaceCard,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ObsidianPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = null,
                                    tint = ElectricEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Menu Filter Sliders",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Refine campus food by budget & speed",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                                )
                            }
                        }

                        IconButton(onClick = { showFilterSheet = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextPrimary
                            )
                        }
                    }

                    // 1. Max Price Slider
                    Text(
                        text = "BUDGET CAP",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Maximum Price",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(EmeraldContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Up to ₹${maxPriceFilter.toInt()}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldDark
                                )
                            )
                        }
                    }

                    Slider(
                        value = maxPriceFilter,
                        onValueChange = { maxPriceFilter = it },
                        valueRange = 30f..250f,
                        steps = 21,
                        colors = SliderDefaults.colors(
                            thumbColor = ObsidianPrimary,
                            activeTrackColor = ElectricEmerald,
                            inactiveTrackColor = SurfaceContainerLow
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. Prep Speed Slider
                    Text(
                        text = "SPEED & PREP TIME",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Max Kitchen Prep Time",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(EmeraldContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "< ${maxPrepTimeFilter.toInt()} mins",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldDark
                                )
                            )
                        }
                    }

                    Slider(
                        value = maxPrepTimeFilter,
                        onValueChange = { maxPrepTimeFilter = it },
                        valueRange = 2f..15f,
                        steps = 12,
                        colors = SliderDefaults.colors(
                            thumbColor = ObsidianPrimary,
                            activeTrackColor = ElectricEmerald,
                            inactiveTrackColor = SurfaceContainerLow
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Dietary Preferences Toggles
                    Text(
                        text = "DIETARY PREFERENCES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pure Veg Toggle
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isPureVegOnly) ObsidianPrimary else SurfaceContainerLow)
                                .clickable { isPureVegOnly = !isPureVegOnly }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = if (isPureVegOnly) ElectricEmerald else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Pure Veg",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isPureVegOnly) SurfaceCard else TextPrimary
                                    )
                                )
                            }
                        }

                        // Halal Toggle
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isHalalOnly) ObsidianPrimary else SurfaceContainerLow)
                                .clickable { isHalalOnly = !isHalalOnly }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = if (isHalalOnly) ElectricEmerald else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Halal",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isHalalOnly) SurfaceCard else TextPrimary
                                    )
                                )
                            }
                        }

                        // High Protein Toggle
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isHighProteinOnly) ObsidianPrimary else SurfaceContainerLow)
                                .clickable { isHighProteinOnly = !isHighProteinOnly }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = if (isHighProteinOnly) ElectricEmerald else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Protein",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isHighProteinOnly) SurfaceCard else TextPrimary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                maxPriceFilter = 250f
                                maxPrepTimeFilter = 15f
                                isPureVegOnly = false
                                isHalalOnly = false
                                isHighProteinOnly = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { showFilterSheet = false },
                            colors = ButtonDefaults.buttonColors(containerColor = ObsidianPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1.4f).height(48.dp)
                        ) {
                            Text(
                                text = "Show Results (${finalFoodItems.size})",
                                fontWeight = FontWeight.Bold,
                                color = SurfaceCard
                            )
                        }
                    }
                }
            }
        }
    }
}

