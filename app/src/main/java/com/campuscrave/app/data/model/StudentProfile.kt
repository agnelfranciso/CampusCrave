package com.campuscrave.app.data.model

data class SavedDropoff(
    val id: String,
    val title: String,
    val block: String, // ADM, EAB, WAB, SAB
    val room: String,  // 402, 301, etc.
    val bench: String = "Door Pickup",
    val isDefault: Boolean = false
) {
    val blockFullName: String
        get() = when (block.uppercase()) {
            "ADM" -> "Administrative Block"
            "EAB" -> "Eastern Academic Block"
            "WAB" -> "Western Academic Block"
            "SAB" -> "Southern Academic Block"
            else -> "$block Block"
        }

    val displayString: String
        get() = "$block, Room $room (Door Delivery)"
}

data class StudentProfile(
    val name: String = "John Doe",
    val email: String = "john.doe@campus.edu",
    val rollNumber: String = "CS-2024-1042",
    val department: String = "Computer Science & Engineering",
    val academicYear: String = "3rd Year",
    val semester: String = "6th Semester",
    val division: String = "Section A",
    val block: String = "EAB", // Eastern Academic Block
    val room: String = "402",  // 3-digit room format
    val bench: String = "Door 402", // Classroom Door Delivery only
    val floor: String = "4th Floor",
    val campusZone: String = "Eastern Academic Block",
    val pinCode: String = "5824",
    val walletBalance: Double = 420.0,
    val cravePoints: Int = 320,
    val totalOrders: Int = 14,
    val rating: Double = 4.9,
    val isVerified: Boolean = true,
    val hasCravePass: Boolean = false,
    val cravePassPlan: String = "", // "Essential" (₹49) or "Pro" (₹99)
    val savedDropoffs: List<SavedDropoff> = listOf(
        SavedDropoff(
            id = "drop_1",
            title = "Primary Classroom",
            block = "EAB",
            room = "402",
            bench = "Door 402",
            isDefault = true
        ),
        SavedDropoff(
            id = "drop_2",
            title = "Systems Project Lab",
            block = "WAB",
            room = "301",
            bench = "Door 301",
            isDefault = false
        ),
        SavedDropoff(
            id = "drop_3",
            title = "Central Library Door",
            block = "ADM",
            room = "101",
            bench = "Door 101",
            isDefault = false
        ),
        SavedDropoff(
            id = "drop_4",
            title = "Seminar Hall Door",
            block = "SAB",
            room = "201",
            bench = "Door 201",
            isDefault = false
        )
    )
) {
    val fullClassroomString: String
        get() = "$block, Room $room (Door Delivery)"

    val fullLocationDescription: String
        get() = "$block, $floor • Room $room Door Pickup"
}
