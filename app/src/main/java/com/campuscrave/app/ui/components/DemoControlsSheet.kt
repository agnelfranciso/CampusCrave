package com.campuscrave.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campuscrave.app.data.model.DayScheduleType
import com.campuscrave.app.data.model.Order
import com.campuscrave.app.data.model.ScheduleState
import com.campuscrave.app.ui.theme.AmberContainer
import com.campuscrave.app.ui.theme.AmberWarning
import com.campuscrave.app.ui.theme.CoralRed
import com.campuscrave.app.ui.theme.ElectricEmerald
import com.campuscrave.app.ui.theme.EmeraldContainer
import com.campuscrave.app.ui.theme.EmeraldDark
import com.campuscrave.app.ui.theme.ObsidianPrimary
import com.campuscrave.app.ui.theme.OutlineBorder
import com.campuscrave.app.ui.theme.SurfaceCard
import com.campuscrave.app.ui.theme.SurfaceContainerLow
import com.campuscrave.app.ui.theme.TextMuted
import com.campuscrave.app.ui.theme.TextPrimary
import com.campuscrave.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoControlsSheet(
    scheduleState: ScheduleState,
    activeOrder: Order?,
    onDismiss: () -> Unit,
    onSetTime: (String, Int, Int, Int) -> Unit,
    onToggleDayType: (DayScheduleType) -> Unit,
    onToggleSmartStop: () -> Unit,
    onAdvanceOrderStatus: () -> Unit,
    onStepBackOrderStatus: () -> Unit,
    onResetDemoData: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceCard,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                            text = "CampusCrave Demo Lab",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Evaluator Controls & State Simulator",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextPrimary
                    )
                }
            }

            // Current Active State Indicator Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (scheduleState.phase.isOrderingOpen) EmeraldContainer else AmberContainer
                    )
                    .border(
                        1.dp,
                        if (scheduleState.phase.isOrderingOpen) ElectricEmerald.copy(alpha = 0.5f) else AmberWarning,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = scheduleState.phase.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = scheduleState.phase.subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (scheduleState.phase.isOrderingOpen) ObsidianPrimary else CoralRed
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = scheduleState.phase.badgeText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SurfaceCard,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Time Warp Jump Buttons
            Text(
                text = "1. SIMULATE CLOCK TIME",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TimePresetButton(
                    label = "11:30 AM – Morning (Pre-orders Closed)",
                    sublabel = "Kitchen preparing • Countdown to 12:20 PM",
                    isSelected = scheduleState.simulatedTimeText == "11:30 AM",
                    onClick = { onSetTime("11:30 AM", 11 * 60 + 30, 50, 0) }
                )
                TimePresetButton(
                    label = "12:22 PM – Pre-order Window Open (Recommended)",
                    sublabel = "Pre-orders batching • 8 mins to 12:30 Bell",
                    isSelected = scheduleState.simulatedTimeText == "12:22 PM",
                    onClick = { onSetTime("12:22 PM", 12 * 60 + 22, 8, 42) }
                )
                TimePresetButton(
                    label = "12:35 PM – Lunch Rush Active",
                    sublabel = "Deliveries on way • Cutoff at 1:00 PM",
                    isSelected = scheduleState.simulatedTimeText == "12:35 PM",
                    onClick = { onSetTime("12:35 PM", 12 * 60 + 35, 25, 0) }
                )
                TimePresetButton(
                    label = "1:05 PM – Order Cutoff Passed",
                    sublabel = "New orders prevented • Live tracking active",
                    isSelected = scheduleState.simulatedTimeText == "1:05 PM",
                    onClick = { onSetTime("1:05 PM", 13 * 60 + 5, 0, 0) }
                )
                TimePresetButton(
                    label = "1:45 PM – Interval Concluded (Closed)",
                    sublabel = "Kitchen closed until tomorrow 12:20 PM",
                    isSelected = scheduleState.simulatedTimeText == "1:45 PM",
                    onClick = { onSetTime("1:45 PM", 13 * 60 + 45, 0, 0) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Schedule Day Type Switcher
            Text(
                text = "2. OPERATING SCHEDULE TYPE",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScheduleTabPill(
                    title = "Mon–Thu",
                    subtitle = "12:20–1:20 PM (Cutoff 1:00 PM)",
                    isSelected = scheduleState.dayType == DayScheduleType.REGULAR,
                    onClick = { onToggleDayType(DayScheduleType.REGULAR) },
                    modifier = Modifier.weight(1f)
                )

                ScheduleTabPill(
                    title = "Friday",
                    subtitle = "12:20–1:50 PM (Cutoff 1:30 PM)",
                    isSelected = scheduleState.dayType == DayScheduleType.FRIDAY,
                    onClick = { onToggleDayType(DayScheduleType.FRIDAY) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3. SmartStop Capacity Toggle
            Text(
                text = "3. SMARTSTOP CAPACITY MECHANISM",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, OutlineBorder, RoundedCornerShape(16.dp)),
                color = SurfaceContainerLow
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SmartStop Emergency Halt",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Simulate early kitchen order closure when runner & kitchen queue hits ceiling",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = scheduleState.smartStopForced,
                        onCheckedChange = { onToggleSmartStop() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceCard,
                            checkedTrackColor = CoralRed,
                            uncheckedThumbColor = SurfaceCard,
                            uncheckedTrackColor = OutlineBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Live Order Status Stepper
            Text(
                text = "4. LIVE ORDER STATUS STEPPER",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, OutlineBorder, RoundedCornerShape(16.dp)),
                color = SurfaceContainerLow
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (activeOrder != null) {
                        Text(
                            text = "Current Order: #${activeOrder.orderId}",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                        )
                        Text(
                            text = "Stage ${activeOrder.status.stepIndex}/8: ${activeOrder.status.title}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onStepBackOrderStatus,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SurfaceCard,
                                    contentColor = TextPrimary
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Step Back", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = onAdvanceOrderStatus,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(999.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ObsidianPrimary,
                                    contentColor = SurfaceCard
                                )
                            ) {
                                Text("Advance Stage", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    tint = ElectricEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No active order in flight. Place an order to test stage progression or review order history.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Reset Demo Data
            Button(
                onClick = {
                    onResetDemoData()
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceContainerLow,
                    contentColor = CoralRed
                )
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Reset All Demo Data & Cart",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
private fun TimePresetButton(
    label: String,
    sublabel: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (isSelected) ObsidianPrimary else OutlineBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        color = if (isSelected) ObsidianPrimary else SurfaceCard
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SurfaceCard else TextPrimary
                )
            )
            Text(
                text = sublabel,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = if (isSelected) ElectricEmerald else TextMuted
                )
            )
        }
    }
}

@Composable
private fun ScheduleTabPill(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                if (isSelected) ObsidianPrimary else OutlineBorder,
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        color = if (isSelected) ObsidianPrimary else SurfaceCard
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) SurfaceCard else TextPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = if (isSelected) ElectricEmerald else TextMuted
                )
            )
        }
    }
}

