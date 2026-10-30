package com.campuscrave.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campuscrave.app.ui.navigation.BottomTab
import com.campuscrave.app.ui.theme.ElectricEmerald
import com.campuscrave.app.ui.theme.EmeraldContainer
import com.campuscrave.app.ui.theme.EmeraldDark
import com.campuscrave.app.ui.theme.ObsidianPrimary
import com.campuscrave.app.ui.theme.OutlineBorder
import com.campuscrave.app.ui.theme.SurfaceCard
import com.campuscrave.app.ui.theme.TextMuted
import com.campuscrave.app.ui.theme.TextPrimary

@Composable
fun FloatingBottomNav(
    currentTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    cartItemCount: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(999.dp),
                    spotColor = ObsidianPrimary.copy(alpha = 0.16f)
                ),
            shape = RoundedCornerShape(999.dp),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, OutlineBorder.copy(alpha = 0.7f))
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomTab.items.forEach { tab ->
                    val isSelected = tab == currentTab
                    val icon = getTabIcon(tab)

                    val animBgColor by animateColorAsState(
                        targetValue = if (isSelected) EmeraldContainer else Color.Transparent,
                        animationSpec = tween(durationMillis = 200),
                        label = "tabBg"
                    )
                    val animTint by animateColorAsState(
                        targetValue = if (isSelected) EmeraldDark else TextMuted,
                        animationSpec = tween(durationMillis = 200),
                        label = "tabTint"
                    )
                    val animScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.08f else 1.0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        ),
                        label = "tabScale"
                    )

                    Box(
                        modifier = Modifier
                            .size(width = 54.dp, height = 40.dp)
                            .scale(animScale)
                            .clip(RoundedCornerShape(999.dp))
                            .background(animBgColor)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onTabSelected(tab)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box {
                            Icon(
                                imageVector = icon,
                                contentDescription = tab.title,
                                tint = animTint,
                                modifier = Modifier.size(22.dp)
                            )

                            // Cart Badge with white outline ring
                            if (tab == BottomTab.Cart && cartItemCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(ElectricEmerald)
                                        .border(1.5.dp, SurfaceCard, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (cartItemCount > 9) "9+" else cartItemCount.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SurfaceCard
                                        )
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

private fun getTabIcon(tab: BottomTab): ImageVector {
    return when (tab) {
        BottomTab.Explore -> Icons.Default.LocalMall
        BottomTab.Search -> Icons.AutoMirrored.Filled.ManageSearch
        BottomTab.Cart -> Icons.Default.ShoppingBag
        BottomTab.Track -> Icons.Default.NearMe
        BottomTab.Profile -> Icons.Default.AccountCircle
    }
}
