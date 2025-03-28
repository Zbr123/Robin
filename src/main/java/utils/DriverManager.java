//package utils;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.Duration;
//
//public class DriverManager {
//    private static WebDriver driver;
//
//    public static WebDriver getDriver() {
//        if (driver == null) {
//            System.out.println("🟢 Setting up ChromeDriver in headless mode for CI...");
//
//            WebDriverManager.chromedriver().setup();
//
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--headless=new"); // Improved headless mode
//            options.addArguments("--disable-gpu");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--disable-extensions");
//            options.addArguments("--disable-software-rasterizer");
//            options.addArguments("--window-size=1920,1080");
//
//            // 🔥 FIX: Ensure unique user data directory
//            options.addArguments("--user-data-dir=/tmp/chrome-user-data");
//
//            driver = new ChromeDriver(options);
//            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
//
//            System.out.println("✅ ChromeDriver ready.");
//        }
//        return driver;
//    }
//
//    public static void quitDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//            System.out.println("🛑 ChromeDriver closed.");
//        }
//    }
//
//    public static void takeScreenshot(String fileName) {
//        if (driver != null) {
//            try {
//                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//                File targetFile = new File("target/" + fileName);
//                FileUtils.copyFile(scrFile, targetFile);
//                System.out.println("📸 Screenshot saved at: " + targetFile.getAbsolutePath());
//            } catch (IOException | WebDriverException e) {
//                System.err.println("❌ Failed to save screenshot: " + e.getMessage());
//            }
//        }
//    }
//}

package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverManager {
    private static WebDriver driver;
    private static final Logger logger = Logger.getLogger(DriverManager.class.getName());

    public static WebDriver getDriver() {
        if (driver == null) {
            logger.info("🟢 Setting up ChromeDriver in headless mode for CI...");

            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Try without `=new` for compatibility
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-software-rasterizer");
            options.addArguments("--window-size=1920,1080");

            // 🔥 FIX: Ensure unique user data directory
            String userDataDir = System.getProperty("user.dir") + "/tmp/chrome-user-data";
            options.addArguments("--user-data-dir=" + userDataDir);

            driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(90));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30)); // Optional, adjust for elements loading

            logger.info("✅ ChromeDriver ready.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("🛑 ChromeDriver closed.");
        }
    }

    public static void takeScreenshot(String fileName) {
        if (driver != null) {
            try {
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File targetFile = new File("target/" + fileName);
                FileUtils.copyFile(scrFile, targetFile);
                logger.info("📸 Screenshot saved at: " + targetFile.getAbsolutePath());
            } catch (IOException | WebDriverException e) {
                logger.severe("❌ Failed to save screenshot: " + e.getMessage());
            }
        }
    }
}
