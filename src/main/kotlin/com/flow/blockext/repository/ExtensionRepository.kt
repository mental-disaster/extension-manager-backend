package com.flow.blockext.repository

import com.flow.blockext.exception.ExtensionDuplicateException
import com.flow.blockext.exception.ExtensionQueryException
import com.flow.blockext.model.entity.Extension
import com.flow.blockext.model.enums.ExtensionType
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class ExtensionRepository(
    private val jdbcTemplate: JdbcTemplate,
) {

    fun findAll(): List<Extension> {
        return runCatching {
            jdbcTemplate.query(
                "SELECT * FROM extension",
                extensionRowMapper,
            )
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자 목록 조회 중 오류가 발생했습니다.", throwable)
        }
    }

    fun findById(id: Long): Extension {
        return runCatching {
            jdbcTemplate.queryForObject(
                "SELECT * FROM extension WHERE id = ?",
                extensionRowMapper,
                id,
            )
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자를 조회하는 중 오류가 발생했습니다.", throwable)
        } ?: throw ExtensionQueryException("ID=${id} 확장자를 찾을 수 없습니다.")
    }

    fun insert(name: String, type: ExtensionType, isBlocked: Boolean): Long {
        val insertSql = """
            INSERT INTO extension(name, type, is_blocked)
            VALUES (?, ?, ?)
        """.trimIndent()

        try {
            jdbcTemplate.update(insertSql, name, type.name, isBlocked)
        } catch (ex: DataAccessException) {
            val rootMsg = ex.rootCause?.message ?: ex.message ?: ""

            if (rootMsg.contains("UNIQUE constraint failed", ignoreCase = true)) {
                throw ExtensionDuplicateException("이미 등록된 확장자입니다.", ex)
            }

            throw ExtensionQueryException("확장자 생성 중 오류가 발생했습니다.", ex)
        }

        return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long::class.java)
            ?: throw ExtensionQueryException("생성된 확장자 ID를 조회할 수 없습니다.")
    }

    private val extensionRowMapper = RowMapper { rs, _: Int ->
        Extension(
            id = rs.getLong("id"),
            name = rs.getString("name"),
            type = ExtensionType.valueOf(rs.getString("type")),
            isBlocked = rs.getBoolean("is_blocked"),
            createdAt = rs.getString("created_at"),
            updatedAt = rs.getString("updated_at"),
        )
    }
}
