package com.flow.blockext.controller

import com.flow.blockext.model.dto.ExtensionBlockUpdateRequestDto
import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.dto.ExtensionResponseDto
import com.flow.blockext.model.dto.toDto
import com.flow.blockext.service.ExtensionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/extensions")
class ExtensionController(
    private val extensionService: ExtensionService,
) {

    @GetMapping
    fun findAll(): List<ExtensionResponseDto> = extensionService.findAll()
        .map { it.toDto() }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: ExtensionCreateRequestDto,
    ): ExtensionResponseDto = extensionService.create(request).toDto()

    @PatchMapping("/{name}/block")
    fun updateBlockStatus(
        @PathVariable("name") name: String,
        @RequestBody req: ExtensionBlockUpdateRequestDto,
    ): ExtensionResponseDto = extensionService.updateBlockStatus(name, req.isBlocked).toDto()

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("name") name: String) {
        extensionService.deleteCustomByName(name)
    }
}
