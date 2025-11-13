package com.flow.blockext.service

import com.flow.blockext.exception.ExtensionDuplicateException
import com.flow.blockext.exception.ExtensionQueryException
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
    fun `create returns entity`() {
        val request = ExtensionCreateRequestDto("exe")
        val created = Extension(1L, "exe", true, ExtensionType.FIXED, "", "")
        given(repository.insert("exe", ExtensionType.FIXED, true)).willReturn(1L)
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
    fun `create propagates duplicate exception`() {
        val request = ExtensionCreateRequestDto("exe")
        given(repository.insert("exe", ExtensionType.FIXED, true)).willThrow(ExtensionDuplicateException("dup"))

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(ExtensionDuplicateException::class.java)
    }

    @Test
    fun `create propagates query exception from findById`() {
        val request = ExtensionCreateRequestDto("exe")
        given(repository.insert("exe", ExtensionType.FIXED, true)).willReturn(5L)
        given(repository.findById(5L)).willThrow(ExtensionQueryException("조회 실패"))

        assertThatThrownBy { service.create(request) }
            .isInstanceOf(ExtensionQueryException::class.java)
    }
}
