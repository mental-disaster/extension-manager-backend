package com.flow.blockext.controller

import com.flow.blockext.model.dto.ExtensionBlockUpdateRequestDto
import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.dto.ExtensionResponseDto
import com.flow.blockext.model.dto.toDto
import com.flow.blockext.service.ExtensionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/extensions")
@CrossOrigin(origins = ["*"])
@Tag(name = "Extension Policy", description = "확장자 차단 정책 관리 API")
class ExtensionController(
    private val extensionService: ExtensionService,
) {

    @GetMapping
    @Operation(summary = "확장자 목록 조회", description = "고정·커스텀 확장자 전체 목록을 조회합니다.")
    fun findAll(): List<ExtensionResponseDto> = extensionService.findAll()
        .map { it.toDto() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "커스텀 확장자 생성", description = "새로운 커스텀 확장자를 추가합니다.")
    fun create(
        @Valid @RequestBody request: ExtensionCreateRequestDto,
    ): ExtensionResponseDto = extensionService.create(request).toDto()

    @PatchMapping("/{name}/block")
    @Operation(summary = "확장자 차단 여부 수정", description = "특정 확장자의 차단 상태를 변경합니다.")
    fun updateBlockStatus(
        @PathVariable("name") name: String,
        @RequestBody req: ExtensionBlockUpdateRequestDto,
    ): ExtensionResponseDto = extensionService.updateBlockStatus(name, req.isBlocked).toDto()

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "커스텀 확장자 삭제", description = "사용자가 추가한 커스텀 확장자를 삭제합니다.")
    fun delete(@PathVariable("name") name: String) {
        extensionService.deleteCustomByName(name)
    }
}
