package com.flow.blockext.controller

import com.flow.blockext.service.ExtensionService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/extensions")
class ExtensionController(
    private val extensionService: ExtensionService,
) {
}