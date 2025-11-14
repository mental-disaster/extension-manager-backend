package com.flow.blockext.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ExtensionCreateRequestDto(
    @field:NotBlank(message = "확장자명 비워둘 수 없습니다.")
    @field:Size(max = 20, message = "확장자명은 20자 이하로 입력 가능합니다.")
    @field:Pattern(regexp = "[a-z0-9]+", message = "확장자명은 알파벳 소문자와 숫자만 입력 가능합니다.")
    @Schema(description = "생성할 커스텀 확장자명", example = "zip")
    val name: String,
)
