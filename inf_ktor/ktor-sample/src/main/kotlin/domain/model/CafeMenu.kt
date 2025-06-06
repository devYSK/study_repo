package com.example.domain.model

import com.example.domain.BaseModel
import com.example.shared.CafeMenuCategory
import kotlinx.serialization.Serializable

@Serializable
data class CafeMenu(
    val name: String,
    val price: Int,
    val category: CafeMenuCategory,
    val image: String,
    override var id: Long? = null,
): BaseModel
