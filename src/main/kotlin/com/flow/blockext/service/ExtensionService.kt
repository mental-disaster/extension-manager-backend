package com.flow.blockext.service

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.repository.ExtensionRepository
import org.springframework.stereotype.Service

@Service
class ExtensionService(
    private val extensionRepository: ExtensionRepository,
) {

    fun findAll(): List<Extension> {
        return extensionRepository.findAll()
    }
}