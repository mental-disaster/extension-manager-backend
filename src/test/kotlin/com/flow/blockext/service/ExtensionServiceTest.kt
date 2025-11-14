package com.flow.blockext.service

import com.flow.blockext.exception.extension.ExtensionDuplicateException
import com.flow.blockext.exception.extension.ExtensionLimitExceededException
import com.flow.blockext.exception.extension.ExtensionQueryException
import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.repository.ExtensionRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito

class ExtensionServiceTest {

    private lateinit var repository: ExtensionRepository
    private lateinit var service: ExtensionService

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(ExtensionRepository::class.java)
        service = ExtensionService(repository)
    }

    @Test
    fun `findAll return extension list`() {
        val extensions = listOf(
            Extension(
                id = 1L,
                name = "exe",
                isBlocked = true,
                type = ExtensionType.FIXED,
                createdAt = "2024-01-01 00:00:00",
                updatedAt = "2024-01-02 00:00:00",
            ),
            Extension(
                id = 2L,
                name = "bat",
                isBlocked = false,
                type = ExtensionType.CUSTOM,
                createdAt = "2024-01-03 00:00:00",
                updatedAt = "2024-01-04 00:00:00",
            ),
        )
        given(repository.findAll()).willReturn(extensions)

        val result = service.findAll()

        assertThat(result).isEqualTo(extensions)
    }

    @Test
    fun `create returns entity`() {
        val request = ExtensionCreateRequestDto("exe")
        val created = Extension(1L, "exe", true, ExtensionType.FIXED, "", "")
        given(repository.insert("exe", ExtensionType.CUSTOM, true)).willReturn(1L)
        given(repository.findById(1L)).willReturn(created)

        val result = service.create(request)

        assertThat(result).isEqualTo(created)
    }

    @Test
    fun `create throws when name blank`() {
        val request = ExtensionCreateRequestDto("   ")

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("name")
    }

    @Test
    fun `create propagates limit exceeded exception`() {
        val request = ExtensionCreateRequestDto("exe")
        given(repository.countByType(ExtensionType.CUSTOM)).willReturn(200L)

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(ExtensionLimitExceededException::class.java)
    }

    @Test
    fun `create propagates duplicate exception`() {
        val request = ExtensionCreateRequestDto("exe")
        given(repository.insert("exe", ExtensionType.CUSTOM, true)).willThrow(ExtensionDuplicateException("dup"))

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(ExtensionDuplicateException::class.java)
    }

    @Test
    fun `create propagates query exception from findById`() {
        val request = ExtensionCreateRequestDto("exe")
        given(repository.insert("exe", ExtensionType.CUSTOM, true)).willReturn(5L)
        given(repository.findById(5L)).willThrow(ExtensionQueryException("조회 실패"))

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(ExtensionQueryException::class.java)
    }

    @Test
    fun `updateBlockStatus returns entity`() {
        val updated = Extension(
            id = 10L,
            name = "exe",
            isBlocked = true,
            type = ExtensionType.FIXED,
            createdAt = "2024-01-05 00:00:00",
            updatedAt = "2024-01-05 00:00:00",
        )
        given(repository.updateIsBlockedByNameAndType("exe", ExtensionType.FIXED, true)).willReturn(1)
        given(repository.findByName("exe")).willReturn(updated)

        val result = service.updateBlockStatus("exe", true)

        assertThat(result).isEqualTo(updated)
    }
}
