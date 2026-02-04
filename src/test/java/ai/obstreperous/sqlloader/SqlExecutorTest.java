package ai.obstreperous.sqlloader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for SqlExecutor.
 * Tests focus on the executeSqlFile method directly without running the full CLI.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
class SqlExecutorTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @MockitoBean
    private SqlLoaderRunner sqlLoaderRunner;

    @Autowired
    private SqlExecutor sqlExecutor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldExecuteSimpleCreateTableScript(@TempDir Path tempDir) throws IOException, SQLException {
        // Given: A SQL file with CREATE TABLE statement
        Path sqlFile = tempDir.resolve("create_table.sql");
        String sql = """
                CREATE TABLE test_users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL
                );
                """;
        Files.writeString(sqlFile, sql);

        // When: SQL file is executed
        sqlExecutor.executeSqlFile(sqlFile.toString());

        // Then: Table should be created
        List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_name = 'test_users'");
        assertThat(tables).hasSize(1);
    }

    @Test
    void shouldExecuteInsertDataScript(@TempDir Path tempDir) throws IOException, SQLException {
        // Given: A table exists
        jdbcTemplate.execute("""
                CREATE TABLE test_products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    price DECIMAL(10, 2)
                );
                """);

        // And: A SQL file with INSERT statements
        Path sqlFile = tempDir.resolve("insert_data.sql");
        String sql = """
                INSERT INTO test_products (name, price) VALUES ('Product A', 19.99);
                INSERT INTO test_products (name, price) VALUES ('Product B', 29.99);
                INSERT INTO test_products (name, price) VALUES ('Product C', 39.99);
                """;
        Files.writeString(sqlFile, sql);

        // When: SQL file is executed
        sqlExecutor.executeSqlFile(sqlFile.toString());

        // Then: Data should be inserted
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM test_products", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @Test
    void shouldHandleEmptySqlFile(@TempDir Path tempDir) throws IOException, SQLException {
        // Given: An empty SQL file
        Path sqlFile = tempDir.resolve("empty.sql");
        Files.writeString(sqlFile, "");

        // When: SQL file is executed
        sqlExecutor.executeSqlFile(sqlFile.toString());

        // Then: No exception should be thrown (handled gracefully)
    }

    @Test
    void shouldHandleWhitespaceOnlySqlFile(@TempDir Path tempDir) throws IOException, SQLException {
        // Given: A SQL file with only whitespace
        Path sqlFile = tempDir.resolve("whitespace.sql");
        Files.writeString(sqlFile, "   \n\n   \t   ");

        // When: SQL file is executed
        sqlExecutor.executeSqlFile(sqlFile.toString());

        // Then: No exception should be thrown
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        // Given: A non-existent file path
        String nonExistentPath = "/tmp/nonexistent.sql";

        // When/Then: Executing should throw IOException
        assertThatThrownBy(() -> sqlExecutor.executeSqlFile(nonExistentPath))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("SQL file not found");
    }

    @Test
    void shouldThrowExceptionOnInvalidSql(@TempDir Path tempDir) throws IOException {
        // Given: A SQL file with invalid SQL
        Path sqlFile = tempDir.resolve("invalid.sql");
        Files.writeString(sqlFile, "INVALID SQL STATEMENT;");

        // When/Then: Executing should throw SQLException
        assertThatThrownBy(() -> sqlExecutor.executeSqlFile(sqlFile.toString()))
                .isInstanceOf(SQLException.class);
    }
}
