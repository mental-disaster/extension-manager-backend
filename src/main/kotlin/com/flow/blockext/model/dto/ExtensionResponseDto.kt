package com.flow.blockext.model.dto

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType

data class ExtensionResponseDto(
    val name: String,
    val isBlocked: Boolean,
    val type: ExtensionType,
)

fun Extension.toDto(): ExtensionResponseDto =
    ExtensionResponseDto(
        name = this.name,
        isBlocked = this.isBlocked,
        type = ExtensionType.valueOf(this.type.name),
    )