package com.flow.blockext.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "Extension Policy API",
        version = "v1",
        description = "확장자 차단 정책을 조회·관리하기 위한 백엔드 API입니다.",
        contact = Contact(name = "Flow", email = "support@flow.com"),
    ),
)
class OpenApiConfig
