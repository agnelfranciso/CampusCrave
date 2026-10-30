package com.campuscrave.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campuscrave.app.data.model.OrderStatus
import com.campuscrave.app.ui.theme.ElectricEmerald
import com.campuscrave.app.ui.theme.EmeraldContainer
import com.campuscrave.app.ui.theme.EmeraldDark
import com.campuscrave.app.ui.theme.EmeraldSecondary
import com.campuscrave.app.ui.theme.ObsidianPrimary
import com.campuscrave.app.ui.theme.OutlineBorder
import com.campuscrave.app.ui.theme.SurfaceCard
import com.campuscrave.app.ui.theme.SurfaceContainer
import com.campuscrave.app.ui.theme.SurfaceContainerHigh
import com.campuscrave.app.ui.theme.SurfaceContainerLow
import com.campuscrave.app.ui.theme.TextMuted
import com.campuscrave.app.ui.theme.TextPrimary
import com.campuscrave.app.ui.theme.TextSecondary

@Composable
fun StatusTimeline(
    currentStatus: OrderStatus,
    modifier: Modifier = Modifier
) {
    val statuses = OrderStatus.entries

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "activePulse"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        statuses.forEachIndexed { index, status ->
            val isCompleted = status.stepIndex < currentStatus.stepIndex
            val isActive = status == currentStatus
            val isUpcoming = status.stepIndex > currentStatus.stepIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Left Icon Node & Connector Line
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(36.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .scale(if (isActive) pulseScale else 1.0f)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> EmeraldSecondary
                                    isActive -> ElectricEmerald
                                    else -> SurfaceContainerHigh
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            isCompleted -> Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = SurfaceCard,
                                modifier = Modifier.size(15.dp)
                            )
                            isActive -> Icon(
                                imageVector = getStatusIcon(status),
                                contentDescription = null,
                                tint = ObsidianPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            else -> Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(TextMuted)
                            )
                        }
                    }

                    // Vertical Connector line (except for last node)
                    if (index < statuses.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(28.dp)
                                .background(
                                    if (isCompleted) EmeraldSecondary else OutlineBorder
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Text Status Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = if (index < statuses.size - 1) 16.dp else 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = status.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Medium,
                                color = when {
                                    isActive -> TextPrimary
                                    isCompleted -> TextPrimary
                                    else -> TextMuted
                                }
                            )
                        )

                        if (isActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(EmeraldContainer)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldDark,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    Text(
                        text = status.description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isActive) EmeraldDark else TextMuted,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}

private fun getStatusIcon(status: OrderStatus): ImageVector {
    return when (status) {
        OrderStatus.ORDER_PLACED -> Icons.Default.ReceiptLong
        OrderStatus.ACCEPTED_BY_CANTEEN -> Icons.Default.Done
        OrderStatus.PREPARING -> Icons.Default.Restaurant
        OrderStatus.READY_FOR_COLLECTION -> Icons.Default.Inventory2
        OrderStatus.RUNNER_ASSIGNED -> Icons.Default.PersonPin
        OrderStatus.COLLECTED -> Icons.Default.ShoppingBag
        OrderStatus.OUT_FOR_DELIVERY -> Icons.Default.DirectionsRun
        OrderStatus.DELIVERED -> Icons.Default.Verified
        else -> Icons.Default.Done
    }
}

