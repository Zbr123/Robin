package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "resources/Features/audit.feature",
        glue = "stepDefinitions",
        tags = "@Audit and @UI",
        plugin = {
                "pretty",
                "html:target/cucumber-audit.html",
                "json:target/cucumber-audit.json"
        },
        monochrome = true
)
public class AuditTestRunner extends AbstractTestNGCucumberTests {
}
