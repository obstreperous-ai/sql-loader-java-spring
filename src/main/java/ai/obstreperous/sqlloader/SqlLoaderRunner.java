package ai.obstreperous.sqlloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    private SqlExecutor sqlExecutor;

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
            sqlExecutor.executeSqlFile(sqlFilePath);
            logger.info("SQL script executed successfully");
            System.exit(0);
        } catch (Exception e) {
            logger.error("Failed to execute SQL script", e);
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
