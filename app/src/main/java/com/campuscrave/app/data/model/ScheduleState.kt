package com.campuscrave.app.data.model

enum class DayScheduleType(val displayName: String) {
    REGULAR("Mon–Thu (Normal)"),
    FRIDAY("Friday (Extended)")
}

enum class CampusCanteenPhase(
    val title: String,
    val subtitle: String,
    val isOrderingOpen: Boolean,
    val isDeliveryActive: Boolean,
    val badgeText: String
) {
    UPCOMING(
        "Kitchen Preparing",
        "Pre-orders open at 12:20 PM",
        isOrderingOpen = false,
        isDeliveryActive = false,
        badgeText = "OPENS 12:20 PM"
    ),
    PRE_ORDER(
        "Kitchen Window Open",
        "Lunch pre-orders batching right now",
        isOrderingOpen = true,
        isDeliveryActive = false,
        badgeText = "LIVE PRE-ORDER"
    ),
    LUNCH_RUSH(
        "Lunch Rush Active",
        "Deliveries in corridor transit",
        isOrderingOpen = true,
        isDeliveryActive = true,
        badgeText = "ACTIVE DISPATCH"
    ),
    ORDERS_CLOSED(
        "New Orders Closed",
        "Cutoff reached • Live deliveries fulfilling",
        isOrderingOpen = false,
        isDeliveryActive = true,
        badgeText = "CUTOFF REACHED"
    ),
    SMART_STOP_CLOSED(
        "SmartStop Capacity Reached",
        "Kitchen at full throughput • Existing orders being delivered",
        isOrderingOpen = false,
        isDeliveryActive = true,
        badgeText = "SMARTSTOP PAUSE"
    ),
    AFTER_HOURS(
        "Kitchen Closed",
        "Lunch interval concluded • See you tomorrow at 12:20 PM",
        isOrderingOpen = false,
        isDeliveryActive = false,
        badgeText = "CLOSED"
    )
}

data class ScheduleState(
    val dayType: DayScheduleType = DayScheduleType.REGULAR,
    val simulatedTimeText: String = "12:22 PM",
    val simulatedMinutesFromMidnight: Int = 12 * 60 + 22,
    val smartStopForced: Boolean = false,
    val customCountdownMinutes: Int = 8,
    val customCountdownSeconds: Int = 42
) {
    val phase: CampusCanteenPhase
        get() {
            if (smartStopForced) return CampusCanteenPhase.SMART_STOP_CLOSED

            val preOrderOpen = 12 * 60 + 20
            val lunchStart = 12 * 60 + 30
            val cutoff = if (dayType == DayScheduleType.REGULAR) 13 * 60 else 13 * 60 + 30
            val lunchEnd = if (dayType == DayScheduleType.REGULAR) 13 * 60 + 20 else 13 * 60 + 50

            return when {
                simulatedMinutesFromMidnight < preOrderOpen -> CampusCanteenPhase.UPCOMING
                simulatedMinutesFromMidnight < lunchStart -> CampusCanteenPhase.PRE_ORDER
                simulatedMinutesFromMidnight < cutoff -> CampusCanteenPhase.LUNCH_RUSH
                simulatedMinutesFromMidnight < lunchEnd -> CampusCanteenPhase.ORDERS_CLOSED
                else -> CampusCanteenPhase.AFTER_HOURS
            }
        }

    val countdownDisplay: String
        get() = "%02d:%02d".format(customCountdownMinutes, customCountdownSeconds)

    val targetNotice: String
        get() = when (phase) {
            CampusCanteenPhase.UPCOMING -> "Opens in 12:20 PM batch"
            CampusCanteenPhase.PRE_ORDER -> "to 12:30 Bell"
            CampusCanteenPhase.LUNCH_RUSH -> "to ${if (dayType == DayScheduleType.REGULAR) "1:00 PM" else "1:30 PM"} Cutoff"
            CampusCanteenPhase.ORDERS_CLOSED -> "Existing orders delivering"
            CampusCanteenPhase.SMART_STOP_CLOSED -> "Kitchen at max capacity"
            CampusCanteenPhase.AFTER_HOURS -> "Opens tomorrow 12:20 PM"
        }
}

