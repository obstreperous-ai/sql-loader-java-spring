# Custom Agents

This directory contains custom GitHub Copilot agent profiles for the SQL Loader Java Spring project. These agents provide specialized expertise to help maintain a lean, high-quality CLI utility.

## Available Agents

### 1. Java & Spring Boot Expert (`java-spring-expert.md`)
**Use for**: Java code implementation, Spring Boot configuration, CLI application logic

- Expert in modern Java and Spring Boot best practices
- Specializes in minimal CLI applications without web features
- Focuses on clean code, proper dependency injection, and container optimization
- Handles error handling, exit codes, and command-line interface design

### 2. Maven Build Expert (`maven-build-expert.md`)
**Use for**: POM configuration, dependency management, build optimization

- Expert in Maven configuration and dependency management
- Specializes in creating lean builds for containerized applications
- Manages plugin configuration and build optimization
- Ensures minimal dependencies and fast build times

### 3. Database & JDBC Expert (`database-jdbc-expert.md`)
**Use for**: SQL script execution, JDBC code, database operations

- Expert in JDBC programming and SQL script execution
- Specializes in reliable database interactions for CLI applications
- Handles connection pooling, transactions, and resource management
- Ensures database compatibility and proper error handling

### 4. Testing & TDD Expert (`testing-tdd-expert.md`)
**Use for**: Writing tests, TDD workflow, test quality improvement

- Expert in test-driven development and testing best practices
- Specializes in JUnit 5, Mockito, and Spring Boot Test
- Implements test-first approach with proper test structure
- Ensures high test coverage and maintainable tests

## How to Use Custom Agents

When working on specific tasks, GitHub Copilot will automatically suggest the most appropriate custom agent based on the context. You can also explicitly request help from a specific agent:

- "Ask the Java Spring expert to implement..."
- "Have the Maven build expert optimize..."
- "Get the database JDBC expert to add..."
- "Request the testing TDD expert to write tests for..."

## Agent Design Principles

All agents are designed to support the project's core principles:

- **Lean**: Minimal dependencies and bloat-free code
- **High Quality**: Best practices, clean code, and proper testing
- **Container-Optimized**: Small footprint for Kubernetes deployment
- **Test-First**: TDD approach with comprehensive test coverage
- **Focused**: Each agent has a specific domain of expertise

## More Information

For more details on custom agents, see [GitHub Copilot Custom Agents Documentation](https://docs.github.com/en/enterprise-cloud@latest/copilot/how-tos/use-copilot-agents/coding-agent/create-custom-agents).
