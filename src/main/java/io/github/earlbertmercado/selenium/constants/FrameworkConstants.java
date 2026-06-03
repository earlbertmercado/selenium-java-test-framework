package io.github.earlbertmercado.selenium.constants;

public final class FrameworkConstants {

    private FrameworkConstants() {}

//    private static final String RESOURCES_PATH = System.getProperty("user.dir") + "/src/main/resources";
//    private static final String CONFIG_FILE_PATH = RESOURCES_PATH + "/config/config.properties";
    private static final String REPORT_OUTPUT_PATH = "reports/extent-report.html";

//    public static String getConfigFilePath() {
//        return CONFIG_FILE_PATH;
//    }

    public static String getReportOutputPath() {
        return REPORT_OUTPUT_PATH;
    }
}
