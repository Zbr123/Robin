//package Runners;
//
//import io.cucumber.testng.AbstractTestNGCucumberTests;
//import io.cucumber.testng.CucumberOptions;
//
//@CucumberOptions(
//        features = "src/test/resources/features",
//        glue = "stepDefinitions",
//        plugin = {
//                "pretty",
//                "html:target/cucumber-reports.html",
//                "json:target/cucumber.json"
//        },
//        monochrome = true
//)
//public class TestRunner extends AbstractTestNGCucumberTests {
//}

package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "resources/Features",  // ✅ Matches your actual folder
        glue = "stepDefinitions",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}

