package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "resources/Features/patientRegistration.feature",
        glue = "stepDefinitions",
        tags = "@PatientRegistration and @UI",
        plugin = {
                "pretty",
                "html:target/cucumber-patient-registration.html",
                "json:target/cucumber-patient-registration.json"
        },
        monochrome = true
)
public class PatientRegistrationTestRunner extends AbstractTestNGCucumberTests {
}
