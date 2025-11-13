package com.flow.blockext.repository

import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ExtensionRepository (
    private var jdbcTemplate: JdbcTemplate
) {

    fun findAll(): List<Extension> {
        val sql = "SELECT * FROM extension".trimIndent()

        return jdbcTemplate.query(sql) { rs, _ ->
            Extension(
                id = rs.getLong("id"),
                name = rs.getString("name"),
                type = ExtensionType.valueOf(rs.getString("type")),
                isBlocked = rs.getBoolean("is_blocked"),
                createdAt = rs.getString("created_at"),
                updatedAt = rs.getString("updated_at")
            )
        }
    }
}