package com.flow.blockext.repository

import com.flow.blockext.exception.extension.ExtensionDuplicateException
import com.flow.blockext.exception.extension.ExtensionQueryException
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

    fun countByType(type: ExtensionType): Long {
        return runCatching {
            jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM extension WHERE type = ?",
                Long::class.java,
                type,
            )!!
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자 타입 개수 조회 중 오류가 발생했습니다.", throwable)
        }
    }

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
            ) ?: throw ExtensionQueryException("ID=${id} 생성된 확장자 조회에 실패했습니다.")
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자를 id로 조회하는 중 오류가 발생했습니다.", throwable)
        }
    }

    fun findByName(name: String): Extension {
        return runCatching {
            jdbcTemplate.queryForObject(
                "SELECT * FROM extension WHERE name = ?",
                extensionRowMapper,
                name,
            ) ?: throw ExtensionQueryException("name=${name} 수정된 확장자 조회에 실패했습니다.")
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자를 이름으로 조회하는 중 오류가 발생했습니다.", throwable)
        }
    }

    fun insert(name: String, type: ExtensionType, isBlocked: Boolean): Long {
        try {
            val inserted = jdbcTemplate.update(
                "INSERT INTO extension(name, type, is_blocked) VALUES (?, ?, ?)",
                name,
                type,
                isBlocked
            )

            if (inserted <= 0) {
                throw ExtensionQueryException("생성된 확장자가 없습니다.")
            }

            return jdbcTemplate.queryForObject("SELECT last_insert_rowid()", Long::class.java)
                ?: throw ExtensionQueryException("생성된 확장자 ID를 조회할 수 없습니다.")
        } catch (e: DataAccessException) {
            val rootMsg = e.rootCause?.message ?: e.message ?: ""

            if (rootMsg.contains("UNIQUE constraint failed", ignoreCase = true)) {
                throw ExtensionDuplicateException("이미 등록된 이름의 확장자입니다.", e)
            }

            throw ExtensionQueryException("확장자 생성 중 오류가 발생했습니다.", e)
        }
    }

    fun updateIsBlockedByNameAndType(name: String, type: ExtensionType, isBlocked: Boolean): Int {
        try {
            val updated = jdbcTemplate.update(
                "UPDATE extension SET is_blocked = ? WHERE name = ? AND type = ?",
                isBlocked,
                name,
                type,
            )

            if (updated <= 0) {
                throw ExtensionQueryException("ID=${name} 수정 대상이 존재하지 않습니다.")
            }

            return updated
        } catch (e: DataAccessException) {
            throw ExtensionQueryException("확장자 수정 중 오류가 발생했습니다.", e)
        }
    }

    fun deleteByNameAndType(name: String, type: ExtensionType): Int {
        return runCatching {
            jdbcTemplate.update(
                "DELETE FROM extension WHERE name = ? AND type = ?",
                name,
                type,
            )
        }.getOrElse { throwable ->
            throw ExtensionQueryException("확장자를 삭제하는 중 오류가 발생했습니다.", throwable)
        }
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
