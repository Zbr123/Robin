package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import utils.DriverManager;

public class Hooks {

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
            } catch (WebDriverException ignored) {
                // Session might already be closed/invalid.
            }
        }
        boolean keepBrowserOpen = Boolean.parseBoolean(System.getProperty("keepBrowserOpen", "false"));
        if (!keepBrowserOpen) {
            DriverManager.quitDriver();
        }
    }
}

