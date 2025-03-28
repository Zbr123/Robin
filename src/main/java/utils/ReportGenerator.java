package utils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ReportGenerator {

    public static void main(String[] args) {
        generateReport();  // <- Call the actual logic from here
    }

    public static void generateReport() {
        File reportOutputDirectory = new File("target/enhanced-report");
        List<String> jsonFiles = Arrays.asList("target/cucumber.json");//test

        Configuration config = new Configuration(reportOutputDirectory, "RobinAutomation");
        config.setBuildNumber("1.0");
        config.addClassifications("Platform", "GitLab CI");
        config.addClassifications("Browser", "Chrome");
        config.addClassifications("Branch", "main");

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, config);
        reportBuilder.generateReports();
    }
}
