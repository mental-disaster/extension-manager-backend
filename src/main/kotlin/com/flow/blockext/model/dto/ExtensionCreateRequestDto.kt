package com.flow.blockext.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ExtensionCreateRequestDto(
    @field:NotBlank
    @field:Size(max = 20)
    val name: String,
)
