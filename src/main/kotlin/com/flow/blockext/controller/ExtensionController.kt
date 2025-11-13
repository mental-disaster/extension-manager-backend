package com.flow.blockext.controller

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.service.ExtensionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/extensions")
class ExtensionController(
    private val extensionService: ExtensionService,
) {

    @GetMapping("")
    fun findAll(): List<Extension> {
        return extensionService.findAll()
    }
}