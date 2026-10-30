package com.campuscrave.app.data.model

data class CustomizationOption(
    val id: String,
    val name: String,
    val additionalPrice: Double = 0.0,
    val isDefault: Boolean = false
)

data class CustomizationGroup(
    val id: String,
    val title: String,
    val subtitle: String = "Select 1 option",
    val isRequired: Boolean = false,
    val maxSelectable: Int = 1,
    val options: List<CustomizationOption> = emptyList()
)

data class FoodItem(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val unit: String,
    val subtitle: String,
    val imageUrl: String,
    val isAvailable: Boolean = true,
    val stockCount: Int = 10,
    val prepTimeMinutes: Int = 5,
    val isHot: Boolean = false,
    val isBestseller: Boolean = false,
    val isPureVeg: Boolean = false,
    val isHalal: Boolean = true,
    val isHighProtein: Boolean = false,
    val isGlutenFree: Boolean = false,
    val rating: Double = 4.8,
    val reviewCount: Int = 124,
    val calories: String = "380 kcal",
    val protein: String = "18g",
    val fat: String = "12g",
    val sodium: String = "310mg",
    val isDemandCraft: Boolean = false,
    val craftDescription: String = "",
    val customizationGroups: List<CustomizationGroup> = emptyList()
) {
    val discountPercent: Int?
        get() = if (originalPrice != null && originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else null
}
