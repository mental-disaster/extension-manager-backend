package com.flow.blockext.service

import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.repository.ExtensionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExtensionService(
    private val extensionRepository: ExtensionRepository,
) {

    fun findAll(): List<Extension> = extensionRepository.findAll()

    @Transactional
    fun create(request: ExtensionCreateRequestDto): Extension {
        val name = request.name.trim().takeIf { it.isNotEmpty() }
            ?: throw IllegalArgumentException("name 은 필수입니다.")

        //TODO: name(확장자)에 대한 추가적인 검증 필요 (ex. 20자 이하, 총 갯수 200개 이하 등)

        val id = extensionRepository.insert(name, ExtensionType.CUSTOM, true)
        return extensionRepository.findById(id)
    }
}
