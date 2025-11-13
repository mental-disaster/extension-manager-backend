package com.flow.blockext.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.flow.blockext.exception.ExtensionDuplicateException
import com.flow.blockext.exception.ExtensionQueryException
import com.flow.blockext.exception.GlobalExceptionHandler
import com.flow.blockext.model.dto.ExtensionCreateRequestDto
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import com.flow.blockext.service.ExtensionService
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ExtensionController::class])
@Import(GlobalExceptionHandler::class)
class ExtensionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var extensionService: ExtensionService

    @Test
    fun `GET returns extension list`() {
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
    fun `GET handles query exception`() {
        Mockito.doThrow(ExtensionQueryException("조회 실패"))
            .`when`(extensionService).findAll()

        mockMvc.perform(get("/api/extensions"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.code", equalTo("EXTENSION_QUERY_FAILED")))
            .andExpect(jsonPath("$.message", equalTo("조회 실패")))
    }

    @Test
    fun `POST creates extension`() {
        val request = ExtensionCreateRequestDto(
            name = "zip",
        )
        val created = Extension(
            id = 10L,
            name = request.name,
            isBlocked = true,
            type = ExtensionType.CUSTOM,
            createdAt = "2024-01-05 00:00:00",
            updatedAt = "2024-01-05 00:00:00",
        )
        given(extensionService.create(request)).willReturn(created)

        mockMvc.perform(
            post("/api/extensions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name", equalTo(request.name)))
            .andExpect(jsonPath("$.type", equalTo(ExtensionType.CUSTOM)))
            .andExpect(jsonPath("$.isBlocked", equalTo(true)))
    }

    @Test
    fun `POST name is null returns 400`() {
        val payload = mapOf(
            "type" to "CUSTOM",
            "isBlocked" to true,
        )

        mockMvc.perform(
            post("/api/extensions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code", equalTo("INVALID_REQUEST")))
    }

    @Test
    fun `POST name exceeds length returns 400`() {
        val payload = mapOf(
            "name" to "a".repeat(21),
        )

        mockMvc.perform(
            post("/api/extensions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code", equalTo("INVALID_REQUEST")))
    }

    @Test
    fun `POST duplicate name returns 409`() {
        val request = ExtensionCreateRequestDto(
            name = "exe",
        )
        Mockito.doThrow(ExtensionDuplicateException("Already exists"))
            .`when`(extensionService).create(request)

        mockMvc.perform(
            post("/api/extensions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code", equalTo("EXTENSION_DUPLICATE")))
            .andExpect(jsonPath("$.message", equalTo("Already exists")))
    }
}
