package com.flow.blockext.service

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.repository.ExtensionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class ExtensionServiceTest {

    private lateinit var repository: ExtensionRepository
    private lateinit var service: ExtensionService

    @BeforeEach
    fun setUp() {
        repository = Mockito.mock(ExtensionRepository::class.java)
        service = ExtensionService(repository)
    }

    @Test
    fun `findAll delegates to repository and returns result`() {
        val extensions = listOf(
            Extension(
                id = 1L,
                name = "exe",
                isBlocked = true,
                type = ExtensionType.FIXED,
                createdAt = "2024-01-01 00:00:00",
                updatedAt = "2024-01-02 00:00:00",
            ),
        )
        Mockito.`when`(repository.findAll()).thenReturn(extensions)

        val result = service.findAll()

        assertThat(result).isEqualTo(extensions)
        verify(repository).findAll()
        verifyNoMoreInteractions(repository)
    }
}
