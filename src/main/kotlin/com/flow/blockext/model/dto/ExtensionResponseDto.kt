package com.flow.blockext.model.dto

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "확장자 정보 응답")
data class ExtensionResponseDto(
    @Schema(description = "확장자명", example = "exe")
    val name: String,
    @Schema(description = "차단 여부", example = "true")
    val isBlocked: Boolean,
    @Schema(description = "확장자 유형(FIXED: 사전 정의, CUSTOM: 사용자 정의)", example = "FIXED")
    val type: ExtensionType,
)

fun Extension.toDto(): ExtensionResponseDto =
    ExtensionResponseDto(
        name = this.name,
        isBlocked = this.isBlocked,
        type = ExtensionType.valueOf(this.type.name),
    )
