package com.flow.blockext.repository

import com.flow.blockext.exception.ExtensionQueryException
import com.flow.blockext.model.enums.ExtensionType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

class ExtensionRepositoryTest {

    private lateinit var dataSource: SingleConnectionDataSource
    private lateinit var jdbcTemplate: JdbcTemplate
    private lateinit var repository: ExtensionRepository

    @BeforeEach
    fun setUp() {
        dataSource = SingleConnectionDataSource(
            "jdbc:sqlite:file:memdb1?mode=memory&cache=shared",
            true,
        ).apply {
            setDriverClassName("org.sqlite.JDBC")
        }
        jdbcTemplate = JdbcTemplate(dataSource)
        repository = ExtensionRepository(jdbcTemplate)

        resetSchema()
        seedData()
    }

    @AfterEach
    fun tearDown() {
        dataSource.destroy()
    }

    @Test
    fun `findAll reads data`() {
        val result = repository.findAll()

        assertThat(result).hasSize(2)

        val exe = result.first { it.name == "exe" }
        assertThat(exe.type).isEqualTo(ExtensionType.FIXED)
        assertThat(exe.isBlocked).isTrue()

        val custom = result.first { it.name == "zip" }
        assertThat(custom.type).isEqualTo(ExtensionType.CUSTOM)
        assertThat(custom.isBlocked).isFalse()
    }

    @Test
    fun `findAll throws domain exception when query fails`() {
        jdbcTemplate.execute("DROP TABLE extension")

        assertThrows(ExtensionQueryException::class.java) {
            repository.findAll()
        }
    }

    private fun resetSchema() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS extension")
        jdbcTemplate.execute(
            """
            CREATE TABLE IF NOT EXISTS extension (
                id              INTEGER PRIMARY KEY AUTOINCREMENT ,
                name            VARCHAR(20) NOT NULL UNIQUE ,
                type            TEXT NOT NULL CHECK(type IN ('FIXED', 'CUSTOM')) DEFAULT 'CUSTOM',
                is_blocked      BOOLEAN NOT NULL DEFAULT TRUE,
                created_at      TEXT NOT NULL DEFAULT (datetime('now')),
                updated_at      TEXT NOT NULL DEFAULT (datetime('now'))
            );
            """.trimIndent(),
        )
    }

    private fun seedData() {
        jdbcTemplate.batchUpdate(
            "INSERT INTO extension(name, type, is_blocked) VALUES (?, ?, ?)",
            listOf(
                arrayOf("exe", ExtensionType.FIXED, TRUE),
                arrayOf("zip", ExtensionType.CUSTOM, FALSE),
            ),
        )
    }
}
