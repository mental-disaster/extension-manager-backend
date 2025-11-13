package com.flow.blockext.controller

import com.flow.blockext.exception.ExtensionQueryException
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.service.ExtensionService
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ExtensionController::class])
class ExtensionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var extensionService: ExtensionService

    @Test
    fun `findAll returns extension list`() {
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
        given(extensionService.findAll()).willReturn(extensions)

        mockMvc.perform(get("/api/extensions").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name", equalTo("exe")))
            .andExpect(jsonPath("$[0].isBlocked", equalTo(true)))
            .andExpect(jsonPath("$[0].type", equalTo("FIXED")))
            .andExpect(jsonPath("$[1].name", equalTo("bat")))
            .andExpect(jsonPath("$[1].isBlocked", equalTo(false)))
            .andExpect(jsonPath("$[1].type", equalTo("CUSTOM")))
    }

    @Test
    fun `findAll handles query exception`() {
        Mockito.doThrow(ExtensionQueryException("조회 실패"))
            .`when`(extensionService).findAll()

        mockMvc.perform(get("/api/extensions"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.code", equalTo("EXTENSION_QUERY_FAILED")))
            .andExpect(jsonPath("$.message", equalTo("조회 실패")))
    }
}
