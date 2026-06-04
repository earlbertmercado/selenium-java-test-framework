# Project Improvement Recommendations

## Scope

This document captures a focused static scan of the Selenium Java test framework and prioritizes suggested improvements.

## High Priority

1. Align execution mode values across code, config, and docs.
- Observed in src/main/resources/config/config.properties (execution_mode) and src/main/java/io/github/earlbertmercado/selenium/driver/DriverFactory.java.
- Current mismatch: docs/config mention grid, while driver switch handles remote.
- Recommendation: support both values (grid and remote) or standardize all usage to one canonical value.

2. Remove duplicate TestNG listener registration.
- Observed in src/test/resources/runner/testng.xml and src/test/java/io/github/earlbertmercado/selenium/base/BaseTest.java.
- Risk: duplicate lifecycle callbacks, duplicate report entries/screenshots.
- Recommendation: keep listener registration in one place only, preferably testng.xml.

3. Harden configuration file loading.
- Observed in src/main/java/io/github/earlbertmercado/selenium/utils/ConfigReader.java.
- Risk: missing config resource can trigger unclear startup failures.
- Recommendation: validate InputStream is not null before properties.load, then throw a clear framework exception with path details.

## Medium Priority

4. Use state-specific cart button locators.
- Observed in src/main/java/io/github/earlbertmercado/selenium/pages/ItemDetailPage.java and src/main/java/io/github/earlbertmercado/selenium/pages/InventoryPage.java.
- Risk: both add/remove use the same .btn_inventory selector, which is fragile if DOM/order changes.
- Recommendation: locate buttons by data-test, explicit text, or item-name scoping.

5. Reorder visibility checks before text retrieval.
- Observed in src/main/java/io/github/earlbertmercado/selenium/pages/ItemDetailPage.java.
- Risk: getText may fail before visibility condition can return false.
- Recommendation: call isVisible first, then read text if visible.

6. Avoid raw findElements for multi-field item extraction.
- Observed in src/main/java/io/github/earlbertmercado/selenium/pages/InventoryPage.java.
- Risk: inconsistent list sizes on slow renders can cause flaky item validation.
- Recommendation: use wait-backed collection methods and verify list sizes before iterating.

7. Optimize Jenkins pipeline flow.
- Observed in Jenkinsfile.
- Current sequence does clean install -DskipTests, then clean test.
- Risk: redundant compile phases and longer CI runtime.
- Recommendation: run a single efficient lifecycle path for PR validation (for example mvn -B test), or separate build and test with no repeated clean compile.

## Low Priority

8. Improve naming and page object lifecycle clarity.
- Observed in src/main/java/io/github/earlbertmercado/selenium/pages/InventoryPage.java and src/test/java/io/github/earlbertmercado/selenium/tests/ItemDetailTest.java.
- Examples:
  - clickSpecificItem(By locatorString) parameter naming can be clearer.
  - itemDetailPage is instantiated before navigation; can be created after click to reflect true page transition.

## Suggested Execution Plan

1. Reliability baseline fixes (execution_mode alignment, ConfigReader null safety, single listener registration).
2. Locator hardening and visibility/check flow updates in page objects.
3. CI optimization in Jenkinsfile.
4. Optional readability cleanup and naming consistency.

## Validation Checklist

- Run compile check: mvn -q -DskipTests compile
- Run regression suite: mvn clean test
- Run targeted classes:
  - mvn clean test -Dtest=LoginTest
  - mvn clean test -Dtest=InventoryTest
  - mvn clean test -Dtest=ItemDetailTest
- Verify report generation at reports/extent-report.html
