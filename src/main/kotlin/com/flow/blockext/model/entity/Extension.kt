package com.flow.blockext.model.entity

import com.flow.blockext.model.enums.ExtensionType

data class Extension(
    val id: Long,
    val name: String,
    val isBlocked: Boolean,
    val type: ExtensionType,
    val createdAt: String,
    val updatedAt: String,
)