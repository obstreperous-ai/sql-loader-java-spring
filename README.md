# SQL Loader

[![CI/CD](https://github.com/obstreperous-ai/sql-loader-java-spring/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/obstreperous-ai/sql-loader-java-spring/actions/workflows/ci-cd.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A lean CLI utility in Java using Spring Boot, Maven, and JDBC that runs SQL data load scripts from the command line. Designed for minimal Java containers in Kubernetes pods.

**Core Principles:**
- Do one job well
- No bloat - keep dependencies minimal
- Test-first development
- High code quality
- Optimized for minimal Java containers

## Features

- ✅ Execute SQL scripts from command line
- ✅ JDBC-based for broad database compatibility
- ✅ Environment variable configuration
- ✅ Minimal dependencies (Spring Boot + JDBC + PostgreSQL driver)
- ✅ Built for Kubernetes/container environments
- ✅ Comprehensive test coverage with Testcontainers

## Requirements

- Java 17 or later
- PostgreSQL database (or any JDBC-compatible database with appropriate driver)

## Installation

### Download Pre-built JAR

Download the latest release from the [Releases](https://github.com/obstreperous-ai/sql-loader-java-spring/releases) page.

### Build from Source

```bash
# Clone the repository
git clone https://github.com/obstreperous-ai/sql-loader-java-spring.git
cd sql-loader-java-spring

# Build with Maven
mvn clean install

# Or use Taskfile
task build
```

The executable JAR will be in `target/sql-loader-0.1.0-SNAPSHOT.jar`.

## Usage

### Basic Usage

```bash
java -jar sql-loader-0.1.0-SNAPSHOT.jar <sql-file-path>
```

### Configuration

Database connection is configured via environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/mydb
export SPRING_DATASOURCE_USERNAME=myuser
export SPRING_DATASOURCE_PASSWORD=mypassword

java -jar sql-loader-0.1.0-SNAPSHOT.jar /path/to/script.sql
```

### Kubernetes Example

```yaml
apiVersion: batch/v1
kind: Job
metadata:
  name: sql-data-loader
spec:
  template:
    spec:
      containers:
      - name: sql-loader
        image: eclipse-temurin:17-jre-alpine
        command: ["java", "-jar", "/app/sql-loader.jar", "/scripts/data.sql"]
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:postgresql://postgres-service:5432/mydb"
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-credentials
              key: password
        volumeMounts:
        - name: sql-scripts
          mountPath: /scripts
        - name: app-jar
          mountPath: /app
      volumes:
      - name: sql-scripts
        configMap:
          name: sql-scripts
      - name: app-jar
        emptyDir: {}
      restartPolicy: Never
```

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `SPRING_DATASOURCE_URL` | JDBC connection URL | Yes |
| `SPRING_DATASOURCE_USERNAME` | Database username | Yes |
| `SPRING_DATASOURCE_PASSWORD` | Database password | Yes |

## Development

### Prerequisites

- Java 17
- Maven 3.8+
- Docker (for Testcontainers in tests)

### Setup Development Environment

#### Using Dev Containers (Recommended)

This project includes a `.devcontainer` configuration for GitHub Codespaces and VS Code:

1. Open in GitHub Codespaces, or
2. Open in VS Code with the Dev Containers extension

The environment will be automatically configured with Java 17 and Maven.

#### Local Setup

```bash
# Install dependencies
mvn dependency:go-offline

# Or with Taskfile
task deps
```

### Building

```bash
# Clean and build
mvn clean install

# Or with Taskfile
task build

# Package without tests
mvn clean package -DskipTests

# Or with Taskfile
task package
```

### Testing

The project uses JUnit 5 and Testcontainers for testing with an embedded PostgreSQL database.

```bash
# Run all tests
mvn test

# Or with Taskfile
task test

# Run integration tests
mvn verify

# Or with Taskfile
task verify
```

### Project Structure

```
.
├── .devcontainer/          # Dev container configuration
│   └── devcontainer.json
├── .github/
│   ├── copilot-instructions.md  # Copilot/AI guidance
│   ├── dependabot.yml           # Dependency updates
│   └── workflows/
│       └── ci-cd.yml            # CI/CD pipeline
├── src/
│   ├── main/
│   │   ├── java/ai/obstreperous/sqlloader/
│   │   │   ├── SqlLoaderApplication.java  # Main Spring Boot app
│   │   │   └── SqlLoaderRunner.java       # CLI runner
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/ai/obstreperous/sqlloader/
│           ├── SqlLoaderApplicationTest.java
│           └── SqlLoaderRunnerTest.java
├── Taskfile.yml            # Task automation
├── pom.xml                 # Maven configuration
└── README.md
```

### Available Tasks

Using [Task](https://taskfile.dev/) (recommended):

```bash
task --list                 # Show all available tasks
task build                  # Build and test
task test                   # Run tests
task package                # Package executable JAR
task run SQL_FILE=path.sql  # Run the application
task help                   # Show usage help
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Write tests first (TDD approach)
4. Implement your changes
5. Ensure all tests pass (`task test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Quality

- Follow test-first development (TDD)
- Maintain high test coverage
- Keep dependencies minimal
- Follow Java naming conventions
- Write clear, focused code

See [.github/copilot-instructions.md](.github/copilot-instructions.md) for detailed development guidelines.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

- 📖 [Documentation](https://github.com/obstreperous-ai/sql-loader-java-spring)
- 🐛 [Issue Tracker](https://github.com/obstreperous-ai/sql-loader-java-spring/issues)
- 💬 [Discussions](https://github.com/obstreperous-ai/sql-loader-java-spring/discussions)

## Acknowledgments

Built with:
- [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/) - Database connectivity
- [Testcontainers](https://www.testcontainers.org/) - Testing with containers
