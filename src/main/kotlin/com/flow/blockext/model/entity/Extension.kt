package com.flow.blockext.model.entity

import com.flow.blockext.model.enums.ExtensionType
import java.time.LocalDateTime

data class Extension(
    val id: Long,
    val name: String,
    val isBlocked: Boolean,
    val type: ExtensionType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)