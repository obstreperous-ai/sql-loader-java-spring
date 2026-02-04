package ai.obstreperous.sqlloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Service for executing SQL scripts.
 * This service handles the core business logic of reading and executing SQL files.
 * 
 * SECURITY NOTE: SQL files are executed as-is without additional sanitization.
 * Only execute SQL files from trusted sources. This tool is designed for 
 * controlled environments like Kubernetes init containers where SQL scripts
 * are provided by trusted configuration management systems.
 */
@Service
public class SqlExecutor {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecutor.class);

    @Autowired
    private DataSource dataSource;

    /**
     * Executes SQL commands from a file.
     *
     * @param sqlFilePath path to the SQL file
     * @throws IOException if file cannot be read
     * @throws SQLException if SQL execution fails
     */
    public void executeSqlFile(String sqlFilePath) throws IOException, SQLException {
        Path path = Paths.get(sqlFilePath);
        
        if (!Files.exists(path)) {
            throw new IOException("SQL file not found: " + sqlFilePath);
        }

        String sqlContent = Files.readString(path);
        
        if (sqlContent.trim().isEmpty()) {
            logger.warn("SQL file is empty: {}", sqlFilePath);
            return;
        }

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            logger.debug("Executing SQL from: {}", sqlFilePath);
            statement.execute(sqlContent);
            logger.debug("SQL execution completed");
        }
    }
}
