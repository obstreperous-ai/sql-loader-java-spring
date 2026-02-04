package ai.obstreperous.sqlloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SQL Loader - A lean CLI utility for running SQL data load scripts.
 * Designed for minimal Java containers in Kubernetes pods.
 */
@SpringBootApplication
public class SqlLoaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlLoaderApplication.class, args);
    }
}
