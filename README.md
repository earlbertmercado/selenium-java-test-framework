# Selenium Java Test Framework

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36)
![Selenium](https://img.shields.io/badge/Selenium-4.44.0-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-7.12.0-orange)

A Selenium WebDriver automation framework in Java for an E-commerce web UI testing, built with TestNG, Maven, and Extent Reports.

## Project Overview

This project provides a maintainable UI test automation setup using:
- Page Object Model (POM)
- TestNG for test orchestration
- Maven for dependency and build management
- Log4j2 for logging
- Extent Reports for HTML reporting
- Optional Selenium Grid execution via Docker Compose

The default test suite validates core flows:
- Login
- Inventory listing
- Item detail

## Tech Stack

- Java 21
- Maven
- Selenium Java 4.44.0
- TestNG 7.12.0
- Log4j2 2.26.0
- Extent Reports 5.1.2
- Jackson Databind 2.21.3 (test scope)

## Prerequisites (Java/Maven/Browser)

Install the following before running tests:
- JDK 21
- Maven 3.9+
- One or more browsers installed locally (Chrome, Edge, Firefox) for local mode
- Docker Desktop (optional, for Selenium Grid)

Verify installation:

```powershell
java -version
mvn -version
```

## Local Setup And Install

1. Clone the repository.
2. Open the project root.
3. Install dependencies and compile:

```powershell
mvn clean install -DskipTests
```

## Run Tests (Suite/Single Test)

### Run default TestNG suite

The Maven Surefire plugin is configured to use:
- src/test/resources/runner/testng.xml

```powershell
mvn clean test
```

### Run with runtime overrides

```powershell
mvn clean test -Dbrowser_name=edge -Dheadless=false
```

### Run a single test class

```powershell
mvn clean test -Dtest=LoginTest
mvn clean test -Dtest=InventoryTest
mvn clean test -Dtest=ItemDetailTest
```

## Configuration (config.properties)

Main runtime configuration file:
- src/main/resources/config/config.properties

Current keys:
- base_url
- browser_name
- browser_width
- browser_height
- headless
- execution_mode (local or grid)
- grid_url
- timeout

Notes:
- Use local browser execution with execution_mode=local.
- Use Selenium Grid with execution_mode=grid and a reachable grid_url.

## Project Structure

```text
selenium-java-test-framework/
|-- assets/                                   # README images
|-- logs/                                     # Generated Log4j2 log files from test executions
|-- reports/                                  # Test execution reports (ExtentReports output)
|-- src/
|   |-- main/
|   |   |-- java/io/github/earlbertmercado/selenium/
|   |   |   |-- constants/                    # App and framework constants
|   |   |   |-- driver/                       # Browser/Driver manager and factory logic
|   |   |   |-- exceptions/                   # Custom exception classes
|   |   |   |-- pages/                        # Page Object classes
|   |   |   |-- reports/                      # Reporting helpers/listeners
|   |   |   `-- utils/                        # Config reader, waits, utilities
|   |   `-- resources/                        # Global properties and Log4j2 config
|   `-- test/
|       |-- java/io/github/earlbertmercado/selenium/
|       |   |-- base/                         # Base Test class (Setup/Teardown)
|       |   |-- dataprovider/                 # Test data providers
|       |   |-- listeners/                    # TestNG listeners
|       |   `-- tests/                        # Functional test classes
|       `-- resources/                        # Test runner (testng.xml) and test data
|-- Jenkinsfile                               # CI/CD pipeline definition
|-- docker-compose.yaml                       # Selenium Grid infrastructure
`-- pom.xml                                   # Project dependencies and build config
```

## Reports And Logs

Generated artifacts:
- Extent report: reports/extent-report.html
- Surefire/TestNG reports: target/surefire-reports/
- Runtime logs: logs/

## CI/CD (Jenkins, Docker Compose)

### Jenkins

The Jenkins pipeline:
- Uses JDK21 and Maven3 tools
- Supports parameters:
  - BROWSER (CHROME, EDGE, FIREFOX)
  - HEADLESS (true, false)
  - TEST_CLASS (All Tests, InventoryTest, ItemDetailTest, LoginTest)
- Runs build and tests via Maven
- Attaches the Extent report email if available

### Docker Compose (Selenium Grid)

The provided docker-compose.yaml starts:
- selenium-hub
- chrome node
- edge node
- firefox node

Start grid:

```powershell
docker compose up -d
```

Stop grid:

```powershell
docker compose down
```

Grid URL default:
- http://localhost:4444/wd/hub

## Contributing Guidelines

1. Create a feature branch.
2. Follow existing package structure and coding style.
3. Add or update tests for behavior changes.
4. Run test suite locally before creating a pull request.
5. Keep pull requests focused and include clear descriptions.

## Troubleshooting

- Tests not launching browser:
  - Confirm browser_name in config.properties.
  - Ensure local browser is installed for local mode.

- Grid run failures:
  - Ensure Docker Desktop is running.
  - Verify grid_url and that hub is reachable.

- No report generated:
  - Check test execution did not fail before listener/report hooks.
  - Check reports/ and target/surefire-reports/ directories.

- Build issues in IDE:
  - Reimport Maven project.
  - Confirm JDK 21 is selected by your IDE.

## Roadmap / Future Improvements

- Add parallel execution profiles for local and grid runs
- Add environment-based configuration (dev/stage/prod)
- Add GitHub Actions pipeline in addition to Jenkins
