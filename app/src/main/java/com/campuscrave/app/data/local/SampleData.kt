package com.campuscrave.app.data.local

import com.campuscrave.app.data.model.CartItem
import com.campuscrave.app.data.model.Category
import com.campuscrave.app.data.model.CustomizationGroup
import com.campuscrave.app.data.model.CustomizationOption
import com.campuscrave.app.data.model.DemoRunner
import com.campuscrave.app.data.model.FoodItem
import com.campuscrave.app.data.model.Order
import com.campuscrave.app.data.model.OrderStatus

object SampleData {

    val categories = listOf(
        Category("all", "All", "", 21),
        Category("hot_meals", "Hot Meals", "", 7),
        Category("soft_drinks", "Soft Drinks", "", 7),
        Category("snacks", "Snacks", "", 3),
        Category("brews", "Brews", "", 2),
        Category("bakery", "Bakery", "", 2)
    )

    val foodItems = listOf(
        FoodItem(
            id = "cb_thalassery",
            name = "Thalassery Chicken Biryani",
            categoryId = "hot_meals",
            description = "Signature campus favorite: fragrant kaima rice slow dum cooked with tender chicken pieces, ghee roasted cashews, kismis, served with cooling onion raita and date lemon pickle.",
            price = 130.0,
            originalPrice = 160.0,
            unit = "450g",
            subtitle = "Kaima Rice • Tender Chicken",
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 12,
            prepTimeMinutes = 4,
            isHot = true,
            isBestseller = true,
            isPureVeg = false,
            isHalal = true,
            isHighProtein = true,
            rating = 4.9,
            reviewCount = 312,
            calories = "620 kcal",
            protein = "34g",
            fat = "18g",
            sodium = "540mg",
            customizationGroups = listOf(
                CustomizationGroup(
                    id = "spice_level",
                    title = "Spice Level",
                    subtitle = "Select spice strength",
                    isRequired = true,
                    options = listOf(
                        CustomizationOption("spice_mild", "Mild Spice"),
                        CustomizationOption("spice_med", "Medium Spice (Campus Classic)", isDefault = true),
                        CustomizationOption("spice_hot", "Fiery Hot Spice")
                    )
                ),
                CustomizationGroup(
                    id = "accompaniment",
                    title = "Add-ons & Sides",
                    subtitle = "Optional additions",
                    maxSelectable = 3,
                    options = listOf(
                        CustomizationOption("side_raita", "Mint & Onion Raita", isDefault = true),
                        CustomizationOption("side_egg", "Extra Boiled Egg", additionalPrice = 15.0),
                        CustomizationOption("side_gravy", "Extra Chicken Salna Gravy", additionalPrice = 20.0)
                    )
                )
            )
        ),
        FoodItem(
            id = "cb_demandcraft_claypot",
            name = "Claypot Dum Biryani",
            categoryId = "hot_meals",
            description = "Artisanal specialty: individual terracotta claypot packed with marinated farm chicken, aged basmati and saffron milk, charcoal sealed with dough for maximum depth of flavor.",
            price = 185.0,
            originalPrice = 220.0,
            unit = "550g Claypot",
            subtitle = "DemandCraft™ • Sealed Dum Pot",
            imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 5,
            prepTimeMinutes = 28,
            isHot = true,
            isBestseller = false,
            isPureVeg = false,
            isHalal = true,
            isHighProtein = true,
            rating = 5.0,
            reviewCount = 89,
            calories = "740 kcal",
            protein = "42g",
            fat = "24g",
            sodium = "580mg",
            isDemandCraft = true,
            craftDescription = "Handcrafted in individual claypots over charcoal embers. Requires 28 minutes fresh cooking time. Feasibility check checks arrival before class starts.",
            customizationGroups = listOf(
                CustomizationGroup(
                    id = "spice_craft",
                    title = "Dum Pot Profile",
                    options = listOf(
                        CustomizationOption("pot_malabar", "Traditional Malabar Style", isDefault = true),
                        CustomizationOption("pot_hyderabadi", "Spicy Hyderabadi Cut", additionalPrice = 10.0)
                    )
                ),
                CustomizationGroup(
                    id = "sides_craft",
                    title = "Claypot Accoutrements",
                    options = listOf(
                        CustomizationOption("side_pickle_raita", "Claypot Raita + Pickle Jar", isDefault = true),
                        CustomizationOption("side_tandoor_leg", "Add Tandoori Chicken Leg", additionalPrice = 65.0)
                    )
                )
            )
        ),
        FoodItem(
            id = "beef_biryani",
            name = "Malabar Beef Biryani",
            categoryId = "hot_meals",
            description = "Slow cooked spiced beef cubes infused with shallots, crushed green chillies and traditional garam masala layered between aromatic ghee rice.",
            price = 140.0,
            originalPrice = 175.0,
            unit = "480g",
            subtitle = "Slow-Cooked Beef • Dum Rice",
            imageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 8,
            prepTimeMinutes = 5,
            isHot = true,
            isBestseller = true,
            isPureVeg = false,
            isHalal = true,
            isHighProtein = true,
            rating = 4.8,
            reviewCount = 245,
            calories = "680 kcal",
            protein = "38g",
            fat = "22g",
            sodium = "590mg",
            customizationGroups = listOf(
                CustomizationGroup(
                    id = "beef_cut",
                    title = "Preparation Preference",
                    options = listOf(
                        CustomizationOption("beef_regular", "Tender Boneless Cuts", isDefault = true),
                        CustomizationOption("beef_extra_roast", "Extra Roast Onion Gravy", additionalPrice = 20.0)
                    )
                )
            )
        ),
        FoodItem(
            id = "porotta_beef",
            name = "Porotta & Beef Roast",
            categoryId = "hot_meals",
            description = "Two golden flaky, crispy multi-layered Malabar porottas served with rich dark roasted beef fry spiced with toasted coconut slivers and curry leaves.",
            price = 90.0,
            originalPrice = 110.0,
            unit = "2 pcs + 200g",
            subtitle = "Flaky Porotta • Kerala Roast",
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 15,
            prepTimeMinutes = 3,
            isHot = true,
            isBestseller = true,
            isPureVeg = false,
            isHalal = true,
            rating = 4.9,
            reviewCount = 410,
            calories = "540 kcal",
            protein = "28g",
            fat = "24g",
            sodium = "480mg",
            customizationGroups = listOf(
                CustomizationGroup(
                    id = "porotta_texture",
                    title = "Porotta Texture",
                    options = listOf(
                        CustomizationOption("porotta_crispy", "Flaky Crispy (Campus Style)", isDefault = true),
                        CustomizationOption("porotta_soft", "Soft & Layered")
                    )
                ),
                CustomizationGroup(
                    id = "extra_bread",
                    title = "Additional Breads",
                    options = listOf(
                        CustomizationOption("none_bread", "Standard (2 Porottas)", isDefault = true),
                        CustomizationOption("add_1_porotta", "+1 Extra Porotta", additionalPrice = 15.0),
                        CustomizationOption("add_2_porotta", "+2 Extra Porottas", additionalPrice = 30.0)
                    )
                )
            )
        ),
        FoodItem(
            id = "porotta_demandcraft_platter",
            name = "Stuffed Malabar Porotta Platter",
            categoryId = "hot_meals",
            description = "Artisan stuffed porotta pocket layered with shredded pepper beef, caramelized shallots, beaten country egg and folded onto a sizzling cast-iron skillet.",
            price = 155.0,
            originalPrice = 190.0,
            unit = "Large Platter",
            subtitle = "DemandCraft™ • Skillet Stuffed",
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 4,
            prepTimeMinutes = 25,
            isHot = true,
            isHalal = true,
            isHighProtein = true,
            rating = 4.9,
            reviewCount = 67,
            calories = "610 kcal",
            protein = "32g",
            fat = "26g",
            sodium = "520mg",
            isDemandCraft = true,
            craftDescription = "Made-to-order skillet toasted stuffed pocket. Requires 25 mins crafting. Verified against lecture bell feasibility.",
            customizationGroups = listOf(
                CustomizationGroup(
                    id = "stuffing_choice",
                    title = "Platter Stuffing",
                    options = listOf(
                        CustomizationOption("stuff_beef_egg", "Pepper Beef & Egg Stuffing", isDefault = true),
                        CustomizationOption("stuff_chicken_cheese", "Spicy Chicken & Mozzarella", additionalPrice = 15.0)
                    )
                )
            )
        ),
        FoodItem(
            id = "porotta_chicken",
            name = "Porotta & Chicken Curry",
            categoryId = "hot_meals",
            description = "Two freshly made Kerala layered porottas paired with hearty chicken curry cooked in thick coconut and coriander gravy.",
            price = 85.0,
            originalPrice = 100.0,
            unit = "2 pcs + 220g",
            subtitle = "Coconut Gravy • 2 Porottas",
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 14,
            prepTimeMinutes = 3,
            isHot = true,
            isPureVeg = false,
            isHalal = true,
            rating = 4.7,
            reviewCount = 188,
            calories = "510 kcal",
            protein = "26g",
            fat = "20g",
            sodium = "450mg"
        ),
        FoodItem(
            id = "kerala_meals",
            name = "Campus Special Meals",
            categoryId = "hot_meals",
            description = "Wholesome student lunch: steaming Kerala red matta rice, dal tadka, aromatic sambar, cabbage thoran, moru kachiathu, crisp pappadam and homemade pickle.",
            price = 70.0,
            originalPrice = 85.0,
            unit = "Full Thali Tray",
            subtitle = "Matta Rice • Sambar • Thoran",
            imageUrl = "https://images.unsplash.com/photo-1610057099443-fde8c4d50f91?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 20,
            prepTimeMinutes = 2,
            isHot = true,
            isPureVeg = true,
            rating = 4.6,
            reviewCount = 142,
            calories = "460 kcal",
            protein = "14g",
            fat = "8g",
            sodium = "380mg"
        ),

        // SOFT DRINKS (Authentic Canteen Refreshments)
        FoodItem(
            id = "soft_coke",
            name = "Coca-Cola Classic Chilled",
            categoryId = "soft_drinks",
            description = "Classic chilled Coca-Cola sleek aluminum can. Pairs brilliantly with spicy biryanis and desk rolls.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "250ml Can",
            subtitle = "Chilled Can • Original Taste",
            imageUrl = "https://images.unsplash.com/photo-1554866585-cd94860890b7?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 28,
            prepTimeMinutes = 1,
            isBestseller = true,
            isPureVeg = true,
            rating = 4.8,
            reviewCount = 340,
            calories = "105 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "20mg"
        ),
        FoodItem(
            id = "soft_pepsi",
            name = "Pepsi Refreshing Cola",
            categoryId = "soft_drinks",
            description = "Chilled 250ml can of crisp, bold Pepsi cola. Delivered ice cold to your campus door for instant refreshment.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "250ml Can",
            subtitle = "Icy Cold • Bold Fizz",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_544,w_504/NI_CATALOG/IMAGES/CIW/2026/10/21/aeef94c1-be5d-420f-9dd4-61f915e92a6c_889262IJHL.png",
            isAvailable = true,
            stockCount = 32,
            prepTimeMinutes = 1,
            isBestseller = true,
            isPureVeg = true,
            rating = 4.8,
            reviewCount = 260,
            calories = "100 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "25mg"
        ),
        FoodItem(
            id = "soft_mirinda_can",
            name = "Mirinda Orange Can",
            categoryId = "soft_drinks",
            description = "Vibrant citrus orange carbonated beverage with fizzy fruit tang that livens up lecture afternoons.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "250ml Can",
            subtitle = "Tangy Orange • Sparkling Fizz",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_544,w_504/NI_CATALOG/IMAGES/ciw/2026/2/11/f49cf2dd-6e26-4b7e-9937-2ccc97467f18_NZJUCY5CM5_MN_11022026.png",
            isAvailable = true,
            stockCount = 25,
            prepTimeMinutes = 1,
            isPureVeg = true,
            rating = 4.7,
            reviewCount = 185,
            calories = "120 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "30mg"
        ),
        FoodItem(
            id = "soft_mountain_dew",
            name = "Mountain Dew",
            categoryId = "soft_drinks",
            description = "High exhilaration citrus rush charged with invigorating caffeine. The ultimate assignment marathon companion.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "250ml Can",
            subtitle = "Citrus Blast • High Caffeine",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_544,w_504/NI_CATALOG/IMAGES/ciw/2026/2/11/f49cf2dd-6e26-4b7e-9937-2ccc97467f18_NZJUCY5CM5_MN_11022026.png",
            isAvailable = true,
            stockCount = 20,
            prepTimeMinutes = 1,
            isPureVeg = true,
            rating = 4.6,
            reviewCount = 135,
            calories = "120 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "35mg"
        ),
        FoodItem(
            id = "soft_appy_fizz",
            name = "Appy Fizz",
            categoryId = "soft_drinks",
            description = "Sparkling apple juice drink with crisp, bubbly carbonation and authentic crisp sweet tart finish.",
            price = 35.0,
            originalPrice = 40.0,
            unit = "250ml Bottle",
            subtitle = "Sparkling Apple • Bubbly Crisp",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_544,w_504/NI_CATALOG/IMAGES/ciw/2026/2/11/f49cf2dd-6e26-4b7e-9937-2ccc97467f18_NZJUCY5CM5_MN_11022026.png",
            isAvailable = true,
            stockCount = 28,
            prepTimeMinutes = 1,
            isPureVeg = true,
            rating = 4.7,
            reviewCount = 220,
            calories = "110 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "20mg"
        ),
        FoodItem(
            id = "soft_mirinda_bottle",
            name = "Mirinda Orange Bottle",
            categoryId = "soft_drinks",
            description = "Crisp, bold orange refreshment in a handy bottle. Great pairing with hot samosas and canteen snacks.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "500ml Bottle",
            subtitle = "Refreshing Orange • Sharing Bottle",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_544,w_504/NI_CATALOG/IMAGES/ciw/2026/2/11/f49cf2dd-6e26-4b7e-9937-2ccc97467f18_NZJUCY5CM5_MN_11022026.png",
            isAvailable = true,
            stockCount = 22,
            prepTimeMinutes = 1,
            isPureVeg = true,
            rating = 4.8,
            reviewCount = 170,
            calories = "140 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "30mg"
        ),
        FoodItem(
            id = "soft_7up",
            name = "7Up Refreshing Lemon",
            categoryId = "soft_drinks",
            description = "Crisp and clear bubbly lemon-lime refreshment to recharge your energy between college lecture periods.",
            price = 40.0,
            originalPrice = 45.0,
            unit = "250ml Can",
            subtitle = "Lemon-Lime • Crisp Fizz",
            imageUrl = "https://instamart-media-assets.swiggy.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,h_600/NI_CATALOG/IMAGES/ciw/2026/2/11/a867904e-404d-4a1b-b973-813e2b3f84f9_HDH341YGOK_MN_11022026.png",
            isAvailable = true,
            stockCount = 26,
            prepTimeMinutes = 1,
            isPureVeg = true,
            rating = 4.8,
            reviewCount = 195,
            calories = "95 kcal",
            protein = "0g",
            fat = "0g",
            sodium = "25mg"
        ),

        // SNACKS
        FoodItem(
            id = "loaded_fries",
            name = "Loaded Desk Fries",
            categoryId = "snacks",
            description = "Crispy golden crinkle cut french fries covered with warm melted cheddar cheese sauce, seasoned beef or chicken bits and fresh chopped chives.",
            price = 80.0,
            originalPrice = 100.0,
            unit = "250g",
            subtitle = "Melted Cheddar • Crispy",
            imageUrl = "https://images.unsplash.com/photo-1585109649139-366815a0d713?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 9,
            prepTimeMinutes = 3,
            isHot = true,
            rating = 4.7,
            reviewCount = 98,
            calories = "420 kcal",
            protein = "10g",
            fat = "22g",
            sodium = "390mg"
        ),
        FoodItem(
            id = "samosa_trio",
            name = "Spicy Samosa Trio",
            categoryId = "snacks",
            description = "Three golden flaky, crispy triangle samosas stuffed with spiced potatoes, green peas and cumin seeds, served with tangy tamarind dip and fresh mint chutney.",
            price = 40.0,
            originalPrice = 50.0,
            unit = "3 pcs",
            subtitle = "Fresh Pastry • Mint Chutney",
            imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 14,
            prepTimeMinutes = 2,
            isHot = true,
            isBestseller = true,
            isPureVeg = true,
            rating = 4.8,
            reviewCount = 280,
            calories = "320 kcal",
            protein = "7g",
            fat = "14g",
            sodium = "310mg"
        ),
        FoodItem(
            id = "chitato_chips",
            name = "Chitato Supreme Chips",
            categoryId = "snacks",
            description = "Crunchy golden rippled potato crisps infused with signature savory cheese flavor. Whole farm potatoes with thick crinkles built for study cravings.",
            price = 35.0,
            originalPrice = 45.0,
            unit = "68g",
            subtitle = "Spicy BBQ • Cheese Crunch",
            imageUrl = "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 18,
            prepTimeMinutes = 1,
            isBestseller = false,
            isPureVeg = true,
            rating = 4.6,
            reviewCount = 85,
            calories = "360 kcal",
            protein = "4g",
            fat = "22g",
            sodium = "310mg"
        ),

        // BREWS
        FoodItem(
            id = "iced_matcha",
            name = "Iced Matcha Oat Latte",
            categoryId = "brews",
            description = "Ceremonial Uji matcha whisked fresh and layered over creamy oat milk with chilled ice cubes in an ergonomic spill-proof lecture cup.",
            price = 60.0,
            originalPrice = 75.0,
            unit = "450ml",
            subtitle = "Uji Blend • Creamy Oat",
            imageUrl = "https://images.unsplash.com/photo-1536256263959-770b48d82b0a?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 9,
            prepTimeMinutes = 2,
            isBestseller = true,
            isPureVeg = true,
            isGlutenFree = true,
            rating = 4.9,
            reviewCount = 192,
            calories = "180 kcal",
            protein = "5g",
            fat = "4g",
            sodium = "95mg"
        ),
        FoodItem(
            id = "nitro_cold_brew",
            name = "Nitro Cold Brew Coffee",
            categoryId = "brews",
            description = "16-hour slow steeped South Indian Arabica beans charged with nitrogen for a velvety micro-foam head without added sugar or milk.",
            price = 50.0,
            originalPrice = 65.0,
            unit = "330ml Can",
            subtitle = "Zero Sugar • High Caffeine",
            imageUrl = "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 16,
            prepTimeMinutes = 1,
            isPureVeg = true,
            isGlutenFree = true,
            rating = 4.7,
            reviewCount = 160,
            calories = "5 kcal",
            protein = "1g",
            fat = "0g",
            sodium = "10mg"
        ),

        // BAKERY & QUICK BITES
        FoodItem(
            id = "chai_parippu_vada",
            name = "Hot Masala Chai & Vada",
            categoryId = "bakery",
            description = "Classic Kerala campus evening tea: steaming fragrant cardamom and ginger milk tea accompanied by two crunchy spiced chana dal vada fritters.",
            price = 30.0,
            originalPrice = 40.0,
            unit = "150ml + 2 pcs",
            subtitle = "Cardamom Chai • Crunchy Vada",
            imageUrl = "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 25,
            prepTimeMinutes = 1,
            isHot = true,
            isPureVeg = true,
            rating = 4.9,
            reviewCount = 520,
            calories = "260 kcal",
            protein = "8g",
            fat = "9g",
            sodium = "210mg"
        ),
        FoodItem(
            id = "peri_peri_roll",
            name = "Peri Peri Desk Roll",
            categoryId = "bakery",
            description = "Warm toasted tortilla wrap stuffed with spiced cottage cheese cubes, charred bell peppers, caramelized onions and spicy peri-peri sauce.",
            price = 75.0,
            originalPrice = 90.0,
            unit = "180g",
            subtitle = "Toasted Wrap • Spicy Paneer",
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800&auto=format&fit=crop&q=80",
            isAvailable = true,
            stockCount = 10,
            prepTimeMinutes = 2,
            isHot = true,
            isPureVeg = true,
            rating = 4.6,
            reviewCount = 115,
            calories = "380 kcal",
            protein = "16g",
            fat = "15g",
            sodium = "360mg"
        )
    )

    val demoRunners = listOf(
        DemoRunner(
            id = "runner_14",
            name = "Marcus K.",
            rating = 4.9,
            role = "Student Courier #14 • Mechanical 3rd Year",
            runsCompleted = 342,
            photoUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&auto=format&fit=crop&q=80",
            phone = "+91 98471 28491"
        ),
        DemoRunner(
            id = "runner_08",
            name = "Priya S.",
            rating = 4.95,
            role = "Student Courier #08 • Electronics 2nd Year",
            runsCompleted = 218,
            photoUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
            phone = "+91 94473 19283"
        ),
        DemoRunner(
            id = "runner_21",
            name = "Aravind R.",
            rating = 4.88,
            role = "Student Courier #21 • Computer Science 4th Year",
            runsCompleted = 415,
            photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80",
            phone = "+91 97455 83920"
        ),
        DemoRunner(
            id = "runner_05",
            name = "Sneha M.",
            rating = 4.92,
            role = "Student Courier #05 • Civil Eng 3rd Year",
            runsCompleted = 195,
            photoUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&auto=format&fit=crop&q=80",
            phone = "+91 94960 48123"
        )
    )

    val samplePastOrders = listOf(
        Order(
            orderId = "CC-84910",
            items = listOf(
                CartItem(foodItems[0], 1),
                CartItem(foodItems[7], 1)
            ),
            subtotal = 170.0,
            deliveryFee = 0.0,
            packagingFee = 10.0,
            totalAmount = 180.0,
            classroom = "EAB, Room 402 (Door Pickup)",
            paymentMethod = "Campus Crave Wallet",
            timestamp = "Yesterday • 12:35 PM",
            status = OrderStatus.DELIVERED,
            runnerName = demoRunners[0].name,
            runnerRating = demoRunners[0].rating,
            runnerRole = demoRunners[0].role,
            runnerPhotoUrl = demoRunners[0].photoUrl,
            runnerPhone = demoRunners[0].phone,
            runnerRunsCompleted = demoRunners[0].runsCompleted,
            deliveryPin = "4821"
        ),
        Order(
            orderId = "CC-84882",
            items = listOf(
                CartItem(foodItems[2], 1),
                CartItem(foodItems[8], 1)
            ),
            subtotal = 180.0,
            deliveryFee = 0.0,
            packagingFee = 10.0,
            totalAmount = 190.0,
            classroom = "WAB, Room 301 (Door Pickup)",
            paymentMethod = "UPI (Simulated)",
            timestamp = "28 Oct 2026 • 12:32 PM",
            status = OrderStatus.DELIVERED,
            runnerName = demoRunners[1].name,
            runnerRating = demoRunners[1].rating,
            runnerRole = demoRunners[1].role,
            runnerPhotoUrl = demoRunners[1].photoUrl,
            runnerPhone = demoRunners[1].phone,
            runnerRunsCompleted = demoRunners[1].runsCompleted,
            deliveryPin = "9124"
        ),
        Order(
            orderId = "CC-84755",
            items = listOf(
                CartItem(foodItems[3], 1),
                CartItem(foodItems[7], 1),
                CartItem(foodItems[13], 1)
            ),
            subtotal = 165.0,
            deliveryFee = 15.0,
            packagingFee = 10.0,
            totalAmount = 190.0,
            classroom = "ADM, Room 101 (Door Pickup)",
            paymentMethod = "Cash on Delivery",
            timestamp = "24 Oct 2026 • 12:40 PM",
            status = OrderStatus.DELIVERED,
            runnerName = demoRunners[2].name,
            runnerRating = demoRunners[2].rating,
            runnerRole = demoRunners[2].role,
            runnerPhotoUrl = demoRunners[2].photoUrl,
            runnerPhone = demoRunners[2].phone,
            runnerRunsCompleted = demoRunners[2].runsCompleted,
            deliveryPin = "7361"
        )
    )
}
