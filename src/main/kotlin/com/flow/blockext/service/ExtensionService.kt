package com.flow.blockext.service

import com.flow.blockext.repository.ExtensionRepository
import org.springframework.stereotype.Service

@Service
class ExtensionService(
    private val extensionRepository: ExtensionRepository,
) {
}