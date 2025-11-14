package com.flow.blockext.repository

import com.flow.blockext.exception.ExtensionDuplicateException
import com.flow.blockext.exception.ExtensionQueryException
import com.flow.blockext.model.enums.ExtensionType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource

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
    fun `countByType return row count`() {
        val result = repository.countByType(ExtensionType.CUSTOM)

        assertThat(result).isEqualTo(1)
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

        assertThatThrownBy { repository.findAll() }
            .isInstanceOf(ExtensionQueryException::class.java)
    }

    @Test
    fun `findById returns entity`() {
        val id = repository.insert("bat", ExtensionType.CUSTOM, true)

        val entity = repository.findById(id)

        assertThat(entity.name).isEqualTo("bat")
        assertThat(entity.type).isEqualTo(ExtensionType.CUSTOM)
        assertThat(entity.isBlocked).isTrue()
    }

    @Test
    fun `findById throws when not found`() {
        assertThatThrownBy { repository.findById(99999L) }
            .isInstanceOf(ExtensionQueryException::class.java)
    }

    @Test
    fun `insert returns generated id`() {
        val id = repository.insert("bat", ExtensionType.CUSTOM, true)

        assertThat(id).isGreaterThan(0)
        val count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM extension WHERE id = ?",
            Long::class.java,
            id,
        )
        assertThat(count).isEqualTo(1)
    }

    @Test
    fun `insert throws duplicate exception`() {
        assertThatThrownBy { repository.insert("exe", ExtensionType.FIXED, true) }
            .isInstanceOf(ExtensionDuplicateException::class.java)
    }

    private fun resetSchema() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS extension")
        jdbcTemplate.execute(
            """
            CREATE TABLE IF NOT EXISTS extension (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                name            VARCHAR(20) NOT NULL UNIQUE CHECK(length(name) <= 20),
                type            TEXT NOT NULL CHECK(type IN ('FIXED', 'CUSTOM')) DEFAULT 'CUSTOM',
                is_blocked      BOOLEAN NOT NULL DEFAULT TRUE,
                created_at      TEXT NOT NULL DEFAULT (datetime('now')),
                updated_at      TEXT NOT NULL DEFAULT (datetime('now'))
            )
            """.trimIndent(),
        )
    }

    private fun seedData() {
        jdbcTemplate.batchUpdate(
            "INSERT INTO extension(name, type, is_blocked) VALUES (?, ?, ?)",
            listOf(
                arrayOf("exe", ExtensionType.FIXED, true),
                arrayOf("zip", ExtensionType.CUSTOM, false),
            ),
        )
    }
}
