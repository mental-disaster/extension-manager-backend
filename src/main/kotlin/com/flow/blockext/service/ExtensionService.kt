package com.flow.blockext.service

import com.flow.blockext.exception.extension.ExtensionLimitExceededException
import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.repository.ExtensionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ExtensionService(
    private val extensionRepository: ExtensionRepository,
) {

    fun findAll(): List<Extension> = extensionRepository.findAll()

    @Transactional
    @Synchronized
    fun create(request: ExtensionCreateRequestDto): Extension {
        val name = request.name.trim().lowercase(Locale.getDefault()).takeIf { it.isNotEmpty() }
            ?: throw IllegalArgumentException("name 은 필수입니다.")

        val customExtCount = extensionRepository.countByType(ExtensionType.CUSTOM)

        if (customExtCount >= 200) {
            throw ExtensionLimitExceededException("사용자 확장자는 최대 200개까지만 등록 가능합니다.")
        }

        val id = extensionRepository.insert(name, ExtensionType.CUSTOM, true)
        return extensionRepository.findById(id)
    }

    @Transactional
    fun updateBlockStatus(name: String, isBlocked: Boolean): Extension {
        extensionRepository.updateIsBlockedByNameAndType(name, ExtensionType.FIXED, isBlocked)

        return extensionRepository.findByName(name)
    }

    fun deleteCustomByName(name: String) {
        extensionRepository.deleteByNameAndType(name, ExtensionType.CUSTOM)
    }
}
