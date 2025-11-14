package com.flow.blockext.model.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ExtensionBlockUpdateRequestDto(
    @Schema(description = "true면 해당 확장자를 차단 상태로 설정", example = "true")
    val isBlocked: Boolean,
)
