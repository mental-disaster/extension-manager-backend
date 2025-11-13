package com.flow.blockext.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ExtensionRepository (
    private var jdbcTemplate: JdbcTemplate
) {
}