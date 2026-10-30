package com.campuscrave.app.ui.screens.cart

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.campuscrave.app.data.model.CartItem
import com.campuscrave.app.data.model.ScheduleState
import com.campuscrave.app.data.model.StudentProfile
import com.campuscrave.app.ui.theme.AmberContainer
import com.campuscrave.app.ui.theme.AmberWarning
import com.campuscrave.app.ui.theme.CanvasBackground
import com.campuscrave.app.ui.theme.CoralRed
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

@Composable
fun CartCheckoutScreen(
    profile: StudentProfile,
    scheduleState: ScheduleState,
    cartItems: List<CartItem>,
    cartSubtotal: Double,
    cartTotal: Double,
    onUpdateQuantity: (String, Int) -> Unit,
    onClearCart: () -> Unit,
    onPlaceOrder: (paymentMethod: String, runnerNote: String) -> Unit,
    onEditLocationClick: () -> Unit,
    onBrowseFoodClick: () -> Unit,
    onAvatarClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedPaymentMethod by remember { mutableStateOf("Campus Crave Wallet") }
    var runnerNote by remember { mutableStateOf("Drop quietly outside door") }

    // System Back Gesture handling: returns to previous screen instead of closing app
    BackHandler(enabled = true) {
        onBrowseFoodClick()
    }

    // Minimum Order state (under ₹100 restriction for runner viability)
    var showMinOrderDialog by remember { mutableStateOf(false) }
    val isMinOrderMet = cartSubtotal >= 100.0 || (profile.hasCravePass && profile.cravePassPlan == "Pro")

    // Payment confirmation simulation (5 seconds for online methods)
    var showPaymentVerifyingDialog by remember { mutableStateOf(false) }
    var paymentCountdown by remember { mutableIntStateOf(5) }

    // Shining transition over Place Order button (normalized -0.6f to 1.6f to fully cover 100% of any screen width)
    val infiniteTransition = rememberInfiniteTransition(label = "ButtonShine")
    val shineProgress by infiniteTransition.animateFloat(
        initialValue = -0.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShineProgress"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Dedicated Cart Top Navigation Bar with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(SurfaceCard)
                            .border(1.dp, OutlineBorder, RoundedCornerShape(999.dp))
                            .clickable(onClick = onBrowseFoodClick)
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Menu",
                            tint = TextPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Menu",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Column {
                        Text(
                            text = "CAMPUSCRAVE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp,
                                color = TextMuted
                            )
                        )
                        Text(
                            text = "Doorstep Basket",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                    }
                }

                if (cartItems.isNotEmpty()) {
                    TextButton(onClick = onClearCart) {
                        Text(
                            text = "Clear",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CoralRed
                            )
                        )
                    }
                }
            }

            if (cartItems.isEmpty()) {
                // Empty Cart State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerLow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Your Canteen Basket is Empty",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Explore hot biryanis, soft drinks, crispy rolls and brews to pre-order for today's lunch interval.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onBrowseFoodClick,
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianPrimary,
                            contentColor = SurfaceCard
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("Browse Canteen Menu", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Non-empty Cart Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top Basket Action Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "My Basket",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(SurfaceContainerLow)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${cartItems.sumOf { it.quantity }} items",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onClearCart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = CoralRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Clear All",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = CoralRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    // 0. Ordering Cutoff / Kitchen Closed Restriction Banner
                    if (!scheduleState.phase.isOrderingOpen) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            color = Color(0xFFFFF2F0),
                            border = BorderStroke(1.2.dp, CoralRed.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(CoralRed.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = CoralRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = scheduleState.phase.title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = CoralRed
                                            ),
                                            modifier = Modifier.weight(1f, fill = false)
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(999.dp))
                                                .background(CoralRed)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = scheduleState.phase.badgeText,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = scheduleState.phase.subtitle,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 12.sp,
                                            color = TextPrimary,
                                            lineHeight = 16.sp
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "You can still add and adjust items in your basket. Order checkout will unlock automatically when pre-orders open.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = TextMuted,
                                            lineHeight = 15.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // DemandCraft™ Notice in Cart if any item is DemandCraft
                    if (cartItems.any { it.foodItem.isDemandCraft }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            color = Color(0xFF221138),
                            border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFFD6BCFA),
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(
                                        text = "DemandCraft™ Item Included (~28 min craft)",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD6BCFA)
                                        )
                                    )
                                    Text(
                                        text = "Pre-order will be freshly cooked on demand and dropped outside your classroom door with 4-digit PIN lock.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = Color(0xFFE9D8FD)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Minimum Order Viability Warning Banner if below ₹100
                    if (!isMinOrderMet) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { showMinOrderDialog = true },
                            color = AmberContainer.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, AmberWarning.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = null,
                                    tint = AmberWarning,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Minimum Order ₹100 Required",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Add ₹${(100 - cartSubtotal).toInt()} more to enable student corridor runner delivery.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    )
                                }
                                Text(
                                    text = "Add Items",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    // 1. Food Items List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        cartItems.forEach { item ->
                            CartItemRow(
                                item = item,
                                onIncrement = { onUpdateQuantity(item.foodItem.id, 1) },
                                onDecrement = { onUpdateQuantity(item.foodItem.id, -1) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. Doorstep Delivery Location Card
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, OutlineBorder, RoundedCornerShape(18.dp)),
                        color = SurfaceCard
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = ObsidianPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Classroom Door Destination",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                }

                                Text(
                                    text = "Change",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = EmeraldSecondary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.clickable(onClick = onEditLocationClick)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = profile.fullLocationDescription,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            )

                            // Notice: Delivery at classroom door only (explained: no official layout of seats)
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),
                                color = SurfaceContainerLow,
                                border = BorderStroke(1.dp, OutlineBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DoorFront,
                                        contentDescription = null,
                                        tint = AmberWarning,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Column {
                                        Text(
                                            text = "Doorstep Pickup Required",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = "Classrooms do not have official fixed seat numbering or desk plans, so student runners deliver strictly to your room door. Please collect your food at the door with your 4-digit PIN.",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = TextSecondary,
                                                lineHeight = 15.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Special Instructions for Runner
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, OutlineBorder, RoundedCornerShape(18.dp)),
                        color = SurfaceCard
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NoteAlt,
                                    contentDescription = null,
                                    tint = ObsidianPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Delivery Instructions",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = runnerNote,
                                onValueChange = { runnerNote = it },
                                placeholder = { Text("E.g. Call when outside door, knock quietly...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ElectricEmerald,
                                    unfocusedBorderColor = OutlineBorder
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Bill Summary
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, OutlineBorder, RoundedCornerShape(18.dp)),
                        color = SurfaceCard
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )

                            Divider(color = OutlineBorder)

                            SummaryRow(
                                label = "Item Subtotal",
                                value = "₹${cartSubtotal.toInt()}"
                            )

                            val deliveryFee = when {
                                profile.hasCravePass && profile.cravePassPlan == "Pro" -> 0.0
                                profile.hasCravePass && profile.cravePassPlan == "Essential" -> 7.0
                                else -> 15.0
                            }

                            SummaryRow(
                                label = "Corridor Delivery Fee",
                                value = if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}",
                                badgeText = if (profile.hasCravePass) "${profile.cravePassPlan.uppercase()} PASS" else "DOOR DROP"
                            )

                            SummaryRow(
                                label = "Eco Spill Packaging",
                                value = "₹10"
                            )

                            Divider(color = OutlineBorder)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "To Pay",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "₹${cartTotal.toInt()}",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ObsidianPrimary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 5. Payment Options
                    Text(
                        text = "Select Payment Method",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PaymentOptionCard(
                            title = "Campus Crave Wallet",
                            subtitle = "Balance: ₹${profile.walletBalance.toInt()} • 1-Tap Instant Checkout",
                            icon = Icons.Default.Bolt,
                            isSelected = selectedPaymentMethod == "Campus Crave Wallet",
                            onClick = { selectedPaymentMethod = "Campus Crave Wallet" }
                        )

                        PaymentOptionCard(
                            title = "UPI (Simulated GPay / PhonePe / Paytm)",
                            subtitle = "5s Simulated Gateway Handshake Verification",
                            icon = Icons.Default.Contactless,
                            isSelected = selectedPaymentMethod == "UPI",
                            onClick = { selectedPaymentMethod = "UPI" }
                        )

                        PaymentOptionCard(
                            title = "Cash on Delivery / Kiosk Terminal",
                            subtitle = "Pay directly at classroom door handoff (Immediate Dispatch)",
                            icon = Icons.Default.Shield,
                            isSelected = selectedPaymentMethod == "Cash on Delivery",
                            onClick = { selectedPaymentMethod = "Cash on Delivery" }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Sticky Bottom Checkout Action Button
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, OutlineBorder)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                        val isOrderingAllowed = scheduleState.phase.isOrderingOpen
                        val isCheckoutEnabled = isOrderingAllowed && cartItems.isNotEmpty() && isMinOrderMet

                        Button(
                            onClick = {
                                if (!isMinOrderMet) {
                                    showMinOrderDialog = true
                                } else if (isOrderingAllowed && cartItems.isNotEmpty()) {
                                    val isOnlinePayment = selectedPaymentMethod != "Cash on Delivery"
                                    if (isOnlinePayment) {
                                        showPaymentVerifyingDialog = true
                                    } else {
                                        // Cash on Delivery skips 10-second payment confirmation step!
                                        onPlaceOrder(selectedPaymentMethod, runnerNote)
                                    }
                                }
                            },
                            enabled = isOrderingAllowed && cartItems.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(999.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCheckoutEnabled) ObsidianPrimary else SurfaceContainerLow,
                                contentColor = if (isCheckoutEnabled) SurfaceCard else TextMuted,
                                disabledContainerColor = SurfaceContainerLow,
                                disabledContentColor = TextMuted
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = if (isCheckoutEnabled) 6.dp else 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(999.dp))
                            ) {
                                // Dynamic Shining animation overlay that fully sweeps and covers 100% of button width & height
                                if (isCheckoutEnabled) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .drawWithCache {
                                                val w = size.width
                                                val h = size.height
                                                val currentX = shineProgress * w
                                                val bandWidth = w * 0.45f
                                                val brush = Brush.linearGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.White.copy(alpha = 0.08f),
                                                        ElectricEmerald.copy(alpha = 0.45f),
                                                        Color.White.copy(alpha = 0.70f),
                                                        ElectricEmerald.copy(alpha = 0.45f),
                                                        Color.White.copy(alpha = 0.08f),
                                                        Color.Transparent
                                                    ),
                                                    start = Offset(currentX - bandWidth, 0f),
                                                    end = Offset(currentX + bandWidth, h)
                                                )
                                                onDrawBehind {
                                                    drawRect(brush = brush)
                                                }
                                            }
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (!isOrderingAllowed) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = null,
                                                tint = TextMuted,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "Ordering Closed (${scheduleState.phase.badgeText})",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextMuted
                                                )
                                            )
                                        }
                                    } else if (!isMinOrderMet) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingBag,
                                                contentDescription = null,
                                                tint = AmberWarning,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "Min. Order ₹100 Required (+₹${(100 - cartSubtotal).toInt()})",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = AmberWarning
                                                )
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = AmberWarning,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = null,
                                                tint = ElectricEmerald,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Text(
                                                text = "Place Order & Reserve Slot",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = SurfaceCard
                                                )
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "₹${cartTotal.toInt()}",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = SurfaceCard
                                                )
                                            )
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                                contentDescription = null,
                                                tint = ElectricEmerald,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Minimum Order Dialog (orders under ₹100 cannot continue for runner viability)
    if (showMinOrderDialog) {
        Dialog(onDismissRequest = { showMinOrderDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.dp, OutlineBorder, RoundedCornerShape(24.dp)),
                color = SurfaceCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(AmberContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = null,
                            tint = AmberWarning,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Minimum Order ₹100 Required",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "To ensure economic viability for our student corridor delivery partners climbing stairwells across ADM, EAB, WAB, and SAB blocks, single room deliveries must be at least ₹100.\n\nPlease add ₹${(100 - cartSubtotal).toInt()} more to complete your order.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showMinOrderDialog = false
                            onBrowseFoodClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(999.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianPrimary,
                            contentColor = SurfaceCard
                        )
                    ) {
                        Text("Add Soft Drinks & Snacks", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    TextButton(onClick = { showMinOrderDialog = false }) {
                        Text(
                            text = "Back to Basket",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }

    // 5-Second Online Payment Confirmation Overlay
    if (showPaymentVerifyingDialog) {
        val animatedProgress by animateFloatAsState(
            targetValue = ((5 - paymentCountdown) / 5f).coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = 800, easing = LinearEasing),
            label = "PaymentProgress"
        )

        LaunchedEffect(Unit) {
            paymentCountdown = 5
            while (paymentCountdown > 0) {
                delay(1000)
                paymentCountdown--
            }
            delay(350)
            showPaymentVerifyingDialog = false
            onPlaceOrder(selectedPaymentMethod, runnerNote)
        }

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(26.dp))
                    .border(1.dp, OutlineBorder, RoundedCornerShape(26.dp)),
                color = SurfaceCard
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (paymentCountdown <= 1) EmeraldContainer else SurfaceContainerLow)
                            .border(
                                2.dp,
                                if (paymentCountdown <= 1) ElectricEmerald else ElectricEmerald.copy(alpha = 0.3f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (paymentCountdown <= 1) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(38.dp)
                            )
                        } else {
                            CircularProgressIndicator(
                                color = ElectricEmerald,
                                strokeWidth = 3.5.dp,
                                modifier = Modifier.size(46.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (paymentCountdown <= 1) "Payment Received & Accepted!" else "Confirming Student Payment...",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            fontSize = 19.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (paymentCountdown <= 1)
                            "Payment verified! Order accepted by Central Canteen."
                        else
                            "Securing student gateway transaction ($paymentCountdown s)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Progress bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(999.dp)),
                        color = ElectricEmerald,
                        trackColor = SurfaceContainerLow
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated live verification logs
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                        color = SurfaceContainerLow
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PaymentStepRow("1. Student UPI Node Gateway Handshake", isDone = paymentCountdown <= 4)
                            PaymentStepRow("2. Campus Token Authorization", isDone = paymentCountdown <= 3)
                            PaymentStepRow("3. Bank Settlement & Lock", isDone = paymentCountdown <= 1)
                            PaymentStepRow("4. Canteen Order Acceptance", isDone = paymentCountdown <= 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentStepRow(step: String, isDone: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (isDone) ElectricEmerald else TextMuted.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = ObsidianPrimary,
                    modifier = Modifier.size(11.dp)
                )
            }
        }
        Text(
            text = step,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isDone) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isDone) TextPrimary else TextMuted
            )
        )
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, OutlineBorder, RoundedCornerShape(16.dp)),
        color = SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Real Item photo thumbnail
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    SubcomposeAsyncImage(
                        model = item.foodItem.imageUrl,
                        contentDescription = item.foodItem.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = ElectricEmerald,
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.foodItem.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (item.customizationSummary.isNotBlank()) {
                        Text(
                            text = item.customizationSummary,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EmeraldDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = item.foodItem.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "₹${item.totalPrice.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = ObsidianPrimary
                        )
                    )
                }
            }

            // Stepper
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp)),
                color = SurfaceContainerLow
            ) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(SurfaceCard)
                            .clickable(onClick = onDecrement),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Minus",
                            tint = TextPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(ObsidianPrimary)
                            .clickable(onClick = onIncrement),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Plus",
                            tint = SurfaceCard,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    badgeText: String? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
            if (badgeText != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(EmeraldContainer)
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = EmeraldDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )
    }
}

@Composable
private fun PaymentOptionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) ElectricEmerald else OutlineBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        color = if (isSelected) EmeraldContainer.copy(alpha = 0.7f) else SurfaceCard
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) SurfaceCard else SurfaceContainerLow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) EmeraldDark else TextMuted,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) EmeraldDark else TextSecondary
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) ObsidianPrimary else SurfaceContainerLow),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = SurfaceCard,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
