package ai.obstreperous.sqlloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CLI runner that executes SQL scripts from command line arguments.
 * 
 * Usage: java -jar sql-loader.jar <sql-file-path>
 * Database configuration via Spring Boot properties or environment variables.
 */
@Component
public class SqlLoaderRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SqlLoaderRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            logger.error("No SQL file path provided");
            System.err.println("Usage: java -jar sql-loader.jar <sql-file-path>");
            System.err.println("Database configuration via environment variables or application.properties:");
            System.err.println("  SPRING_DATASOURCE_URL (or spring.datasource.url)");
            System.err.println("  SPRING_DATASOURCE_USERNAME (or spring.datasource.username)");
            System.err.println("  SPRING_DATASOURCE_PASSWORD (or spring.datasource.password)");
            System.exit(1);
            return;
        }

        String sqlFilePath = args[0];
        logger.info("Starting SQL loader for file: {}", sqlFilePath);

        try {
            executeSqlFile(sqlFilePath);
            logger.info("SQL script executed successfully");
            System.exit(0);
        } catch (Exception e) {
            logger.error("Failed to execute SQL script", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

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
