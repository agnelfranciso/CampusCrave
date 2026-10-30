package com.campuscrave.app.data.model

data class Category(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val itemCount: Int,
    val imageUrl: String = ""
)

