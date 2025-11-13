package com.flow.blockext.model.dto

import com.flow.blockext.model.enums.ExtensionType

data class ExtensionDto(
    val name: String,
    val isBlocked: Boolean,
    val type: ExtensionType,
)