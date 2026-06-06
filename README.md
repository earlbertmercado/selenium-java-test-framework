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
- (other pages to follow)

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

### Run by environment

```powershell
mvn clean test -Denv=test
mvn clean test -Denv=stage
mvn clean test -Denv=prod
```

### Override test data directory

```powershell
mvn clean test -Dtestdata.dir=C:/ci-data/ui-tests
```

Expected structure when using `testdata.dir`:
- Required for `-Denv=test`: `<testdata.dir>/test/users.json`
- Required for `-Denv=stage`: `<testdata.dir>/stage/users.json`
- Required for `-Denv=prod`: `<testdata.dir>/prod/users.json`

### Run a single test class

```powershell
mvn clean test -Dtest=LoginTest
```

### Parallel execution

```powershell
mvn clean test -Dtest=LoginTest -Dparallel=methods -DthreadCount=4
```

## Configuration (Environment-based)

Runtime configuration load order:
1. Classpath defaults (versioned with code)
  - src/main/resources/config/test.properties
  - src/main/resources/config/stage.properties
  - src/main/resources/config/prod.properties
2. System properties (`-Dkey=value`) override file values

How environment selection works:
- Use `-Denv=test|stage|prod` (default is `test`).
- Test data is loaded from `testdata/<env>/users.json`.
- Use `-Dtestdata.dir=<path>` to override the test data root.
- You can override any key with `-D<key>=<value>`.

Supported keys:
- base_url
- browser_name
- browser_width
- browser_height
- headless
- execution_mode (local or grid)
- grid_url
- timeout

Secrets and credentials:
- Do not store secrets in source-controlled properties/json files.
- Use reference keys like `app_username_ref=SAUCE_USERNAME`.
- At runtime, provide `SAUCE_USERNAME` and `SAUCE_PASSWORD` via environment variables or Maven `-D` system properties.

## Project Structure

```text
selenium-java-test-framework/
|-- logs/                                     # Generated Log4j2 log files from test executions
|-- reports/                                  # Test execution reports (ExtentReports output)
|-- testdata/                                 # Externalized environment-specific test data
|   |-- test/
|   |   `-- users.json
|   |-- stage/
|   `-- prod/
|-- src/
|   |-- main/
|   |   |-- java/io/github/earlbertmercado/selenium/
|   |   |   |-- constants/                    # App and framework constants
|   |   |   |-- driver/                       # Browser/Driver manager and factory logic
|   |   |   |-- exceptions/                   # Custom exception classes
|   |   |   |-- pages/                        # Page Object classes
|   |   |   |-- reports/                      # Reporting helpers/listeners
|   |   |   `-- utils/                        # Config reader, waits, utilities
|   |   `-- resources/
|   |       |-- config/                       # Baseline environment defaults (test/stage/prod)
|   |       `-- log4j2.xml
|   `-- test/
|       |-- java/io/github/earlbertmercado/selenium/
|       |   |-- base/                         # Base Test class (Setup/Teardown)
|       |   |-- dataprovider/                 # Test data providers
|       |   |-- listeners/                    # TestNG listeners
|       |   `-- tests/                        # Functional test classes
|       `-- resources/                        # Test runner (testng.xml)
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
  - BROWSER (Chrome, Edge, Firefox)
  - HEADLESS (true, false)
  - PAGE (All, Inventory, ItemDetail, Login)
  - TEST_EXECUTION (Sequential, Parallel-Methods, Parallel-Classes, Parallel-Tests)
  - THREAD_COUNT (2–12, used for parallel execution)
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
  - Confirm browser_name in the active environment config file under src/main/resources/config/ (test.properties, stage.properties, or prod.properties).
  - Ensure local browser is installed for local mode.

- Grid run failures:
  - Ensure Docker Desktop is running.
  - Verify grid_url and that hub is reachable.

- Test data file not found:
  - Ensure `users.json` exists under `testdata/<env>/` for the selected environment.
  - Example: for `-Denv=stage`, create `testdata/stage/users.json`.

- No report generated:
  - Check test execution did not fail before listener/report hooks.
  - Check reports/ and target/surefire-reports/ directories.

- Build issues in IDE:
  - Reimport Maven project.
  - Confirm JDK 21 is selected by your IDE.

## Roadmap / Future Improvements

- Add GitHub Actions pipeline in addition to Jenkins
