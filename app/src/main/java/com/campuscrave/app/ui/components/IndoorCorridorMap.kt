package com.campuscrave.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
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
import com.campuscrave.app.ui.theme.OutlineVariant
import com.campuscrave.app.ui.theme.SurfaceCard
import com.campuscrave.app.ui.theme.SurfaceContainerLow
import com.campuscrave.app.ui.theme.TextMuted
import com.campuscrave.app.ui.theme.TextPrimary
import com.campuscrave.app.ui.theme.TextSecondary

@Composable
fun IndoorCorridorMap(
    runnerName: String = "Marcus K.",
    destinationText: String = "Room 402 Door",
    orderStatus: OrderStatus = OrderStatus.OUT_FOR_DELIVERY,
    modifier: Modifier = Modifier
) {
    // Pulse animation for runner GPS beacon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Calculate progression fraction along route based on live order status
    val targetProgress = when (orderStatus) {
        OrderStatus.ORDER_PLACED -> 0.08f
        OrderStatus.ACCEPTED_BY_CANTEEN -> 0.18f
        OrderStatus.PREPARING -> 0.32f
        OrderStatus.READY_FOR_COLLECTION -> 0.45f
        OrderStatus.RUNNER_ASSIGNED -> 0.58f
        OrderStatus.COLLECTED -> 0.72f
        OrderStatus.OUT_FOR_DELIVERY -> 0.88f
        OrderStatus.DELIVERED -> 1.0f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "animatedProgress"
    )

    val currentDistanceText = when (orderStatus) {
        OrderStatus.ORDER_PLACED, OrderStatus.ACCEPTED_BY_CANTEEN -> "Central Canteen (380m)"
        OrderStatus.PREPARING, OrderStatus.READY_FOR_COLLECTION -> "Packaging Bay (310m)"
        OrderStatus.RUNNER_ASSIGNED, OrderStatus.COLLECTED -> "Entering Block Quad (160m)"
        OrderStatus.OUT_FOR_DELIVERY -> "Level 4 Corridor (25m away)"
        OrderStatus.DELIVERED -> "Arrived at Classroom Door (0m)"
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, OutlineBorder, RoundedCornerShape(20.dp)),
        color = SurfaceCard
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Live Waypoint Node Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(ElectricEmerald)
                    )
                    Text(
                        text = "LIVE WAYPOINT TELEMETRY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.sp,
                            color = EmeraldDark
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = currentDistanceText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Blueprint
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF0F172A)) // Blueprint Dark Indigo
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Grid
                    val step = 28f
                    var gx = 0f
                    while (gx < width) {
                        drawLine(
                            color = Color(0xFF1E293B),
                            start = Offset(gx, 0f),
                            end = Offset(gx, height),
                            strokeWidth = 1f
                        )
                        gx += step
                    }
                    var gy = 0f
                    while (gy < height) {
                        drawLine(
                            color = Color(0xFF1E293B),
                            start = Offset(0f, gy),
                            end = Offset(width, gy),
                            strokeWidth = 1f
                        )
                        gy += step
                    }

                    // Waypoint Coordinates across campus corridor
                    // Point 0: Central Kitchen (bottom-left)
                    // Point 1: Quad breezeway (center-left)
                    // Point 2: Block Stairwell / Lift (center)
                    // Point 3: Level Corridor Walkway (center-right)
                    // Point 4: Room Door Drop Target (top-right)
                    val p0 = Offset(width * 0.10f, height * 0.78f)
                    val p1 = Offset(width * 0.32f, height * 0.78f)
                    val p2 = Offset(width * 0.32f, height * 0.40f)
                    val p3 = Offset(width * 0.65f, height * 0.40f)
                    val p4 = Offset(width * 0.88f, height * 0.25f)

                    val fullPath = listOf(p0, p1, p2, p3, p4)

                    // Draw outer background walkway track
                    for (i in 0 until fullPath.size - 1) {
                        drawLine(
                            color = Color(0xFF334155),
                            start = fullPath[i],
                            end = fullPath[i + 1],
                            strokeWidth = 18f
                        )
                    }

                    // Draw dashed guide line
                    val dash = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    for (i in 0 until fullPath.size - 1) {
                        drawLine(
                            color = Color(0xFF64748B),
                            start = fullPath[i],
                            end = fullPath[i + 1],
                            strokeWidth = 2.5f,
                            pathEffect = dash
                        )
                    }

                    // Calculate live runner position along piecewise linear segments
                    val totalSegments = 4
                    val progress = animatedProgress.coerceIn(0f, 1f)
                    val scaledProgress = progress * totalSegments
                    val segIndex = scaledProgress.toInt().coerceIn(0, totalSegments - 1)
                    val segFraction = (scaledProgress - segIndex).coerceIn(0f, 1f)

                    val startPt = fullPath[segIndex]
                    val endPt = fullPath[segIndex + 1]
                    val runnerPt = Offset(
                        x = startPt.x + (endPt.x - startPt.x) * segFraction,
                        y = startPt.y + (endPt.y - startPt.y) * segFraction
                    )

                    // Draw completed traversed path in Electric Emerald
                    for (i in 0 until segIndex) {
                        drawLine(
                            color = ElectricEmerald,
                            start = fullPath[i],
                            end = fullPath[i + 1],
                            strokeWidth = 4f
                        )
                    }
                    drawLine(
                        color = ElectricEmerald,
                        start = startPt,
                        end = runnerPt,
                        strokeWidth = 4f
                    )

                    // Draw Waypoint Node Circles
                    listOf(p0, p1, p2, p3, p4).forEachIndexed { i, pt ->
                        val isReached = progress >= (i.toFloat() / totalSegments)
                        drawCircle(
                            color = if (isReached) ElectricEmerald else Color(0xFF475569),
                            radius = 6f,
                            center = pt
                        )
                    }

                    // Draw Runner Pulse Radar Beacon
                    drawCircle(
                        color = ElectricEmerald.copy(alpha = 0.25f),
                        radius = 24f * pulseScale,
                        center = runnerPt
                    )
                    drawCircle(
                        color = ElectricEmerald.copy(alpha = 0.45f),
                        radius = 16f,
                        center = runnerPt
                    )
                    drawCircle(
                        color = ObsidianPrimary,
                        radius = 11f,
                        center = runnerPt
                    )
                    drawCircle(
                        color = ElectricEmerald,
                        radius = 6f,
                        center = runnerPt
                    )
                }

                // Node Badges
                // Start Node (Canteen)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 8.dp, bottom = 6.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF1E293B))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Central Canteen",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Destination Door Node
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 8.dp, top = 6.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(if (orderStatus == OrderStatus.DELIVERED) ElectricEmerald else Color(0xFF1E293B))
                        .border(
                            1.dp,
                            if (orderStatus == OrderStatus.DELIVERED) ObsidianPrimary else Color(0xFF334155),
                            RoundedCornerShape(999.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DoorFront,
                            contentDescription = null,
                            tint = if (orderStatus == OrderStatus.DELIVERED) ObsidianPrimary else ElectricEmerald,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = destinationText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (orderStatus == OrderStatus.DELIVERED) ObsidianPrimary else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Step Progress Checklist
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WaypointCheckChip(
                    label = "Canteen Hub",
                    isDone = orderStatus.stepIndex >= 2
                )
                WaypointCheckChip(
                    label = "Quad Stairs",
                    isDone = orderStatus.stepIndex >= 5
                )
                WaypointCheckChip(
                    label = "Corridor Node",
                    isDone = orderStatus.stepIndex >= 7
                )
                WaypointCheckChip(
                    label = "Room Door",
                    isDone = orderStatus.stepIndex >= 8
                )
            }
        }
    }
}

@Composable
private fun WaypointCheckChip(label: String, isDone: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(if (isDone) EmeraldContainer else SurfaceContainerLow),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = EmeraldDark,
                    modifier = Modifier.size(10.dp)
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal,
                color = if (isDone) TextPrimary else TextMuted
            )
        )
    }
}
