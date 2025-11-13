package com.flow.blockext.controller

import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.dto.ExtensionResponseDto
import com.flow.blockext.model.dto.toDto
import com.flow.blockext.service.ExtensionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

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
}
