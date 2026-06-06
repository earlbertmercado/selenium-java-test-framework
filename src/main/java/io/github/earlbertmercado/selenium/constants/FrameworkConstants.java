package io.github.earlbertmercado.selenium.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}
    private static final String REPORT_OUTPUT_PATH = "reports/extent-report.html";

    public static String getReportOutputPath() {
        return REPORT_OUTPUT_PATH;
    }
}
