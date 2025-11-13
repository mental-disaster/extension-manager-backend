package com.flow.blockext.model.dto

import jakarta.validation.constraints.NotBlank

data class ExtensionCreateRequestDto(
    @field:NotBlank
    val name: String,
)
