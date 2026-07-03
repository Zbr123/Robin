package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "resources/Features/billing.feature",
        glue = "stepDefinitions",
        tags = "@Billing and @UI",
        plugin = {
                "pretty",
                "html:target/cucumber-billing.html",
                "json:target/cucumber-billing.json"
        },
        monochrome = true
)
public class BillingTestRunner extends AbstractTestNGCucumberTests {
}
