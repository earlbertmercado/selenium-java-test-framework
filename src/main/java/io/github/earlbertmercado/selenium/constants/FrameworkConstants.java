package io.github.earlbertmercado.selenium.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class FrameworkConstants {

    private static final String REPORT_OUTPUT_PATH = "reports/extent-report.html";

    public static String getReportOutputPath() {
        return REPORT_OUTPUT_PATH;
    }
}
