# GitHub Copilot Instructions

## Project Overview

This is a lean CLI utility in Java using Spring Boot, Maven, and JDBC. The tool runs SQL data load scripts from the command line in a minimal Java container (no extras installed) for use in K8s pods.

**Core Principles:**
- Do one job well
- No bloat - keep dependencies minimal
- Test-first development
- High code quality
- Designed for minimal Java containers in Kubernetes

## Technology Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Build Tool:** Maven
- **Database:** JDBC
- **Container:** Minimal Java container (no extras)
- **Deployment:** Kubernetes pods

## Build and Test Commands

- `mvn clean install` — Build the project
- `mvn test` — Run all tests
- `mvn verify` — Run tests and integration tests
- `mvn clean` — Clean build artifacts

## Code Quality Standards

### General Guidelines
- Write tests first (TDD approach)
- Keep code simple and focused
- Avoid unnecessary dependencies or libraries
- Optimize for minimal container size
- Follow Java naming conventions

### Testing
- Write unit tests for all new functionality
- Aim for high test coverage
- Use meaningful test names that describe behavior
- Mock external dependencies appropriately
- Tests should be fast and deterministic

### Dependencies
- Only add dependencies that are absolutely necessary
- Prefer lightweight libraries over heavy frameworks
- Consider container size impact when adding dependencies
- Document why each dependency is needed

### Code Style
- Follow standard Java conventions
- Use clear, descriptive variable and method names
- Keep methods small and focused on single responsibility
- Add comments only when code intent is not clear
- Avoid code duplication

#### Immutability and Final
- **Always use `final` for fields** whenever possible to ensure immutability
- **Always use `final` for method parameters** to prevent reassignment
- **Always use `final` for local variables** whenever possible to improve readability and prevent bugs
- Immutability reduces bugs and makes code easier to reason about

#### Dependency Injection
- **Prefer constructor injection over field injection** (avoid `@Autowired` on fields)
- Constructor injection makes dependencies explicit and enables immutability
- Makes classes easier to test and understand

#### Code Quality and Reflection
- **Always reflect after tests pass** - look for opportunities to improve
- Apply DRY (Don't Repeat Yourself) principles to eliminate duplication
- Refactor for readability - code should be self-documenting
- Prefer functional and declarative approaches over imperative code where appropriate

#### Lightweight and Functional Style
- Favor a minimal, lightweight, functional idiomatic style
- Use Java's functional features (lambdas, streams) when they improve clarity
- Keep classes small and focused on a single responsibility
- Avoid over-engineering - simple solutions are preferred

## Architecture Guidelines

### CLI Design
- Keep the CLI interface simple and intuitive
- Support standard input/output streams
- Exit codes should follow conventions (0 for success, non-zero for errors)
- Provide clear error messages

### SQL Script Execution
- Focus on reliable SQL script execution
- Handle errors gracefully
- Support database connection configuration via environment variables or command-line arguments
- Keep database operations transactional where appropriate

### Container Considerations
- Assume minimal Java runtime (no development tools)
- Keep runtime dependencies minimal
- Application should start quickly
- Use environment variables for configuration
- Follow 12-factor app principles where applicable

## Workflow

- All changes should include tests
- Run `mvn clean install` before committing
- Ensure all tests pass before pushing
- Keep commits focused and atomic
- Write clear commit messages

## What to Avoid

- Adding unnecessary features or dependencies
- Complex abstractions when simple code suffices
- Large frameworks when smaller libraries work
- Development tools in runtime dependencies
- Magic numbers or unclear configuration
