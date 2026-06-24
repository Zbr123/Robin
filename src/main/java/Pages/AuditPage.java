package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Charge Review and Auditing (UC_05) — claim edit/update assertions on Patient Access.
 */
public class AuditPage {

    private static final By markAsReadyToBillButton =
            By.xpath("//button[span[text()='Mark As Ready To Bill']]");
    private static final By generateBillButton = By.id("AccumedHaadActivityListForm:generateBillBtn");
    private static final By encounterSection = By.xpath("//legend[normalize-space()='Encounter']");
    private static final By patientAndEncounterTab =
            By.xpath("//a[contains(text(),'Patient and Encounter')]");

    private final WebDriver driver;
    private final LoginPage loginPage;
    private String lastServiceCode;
    private String lastExpectedOrderStatus;

    public AuditPage(WebDriver driver, LoginPage loginPage) {
        this.driver = driver;
        this.loginPage = loginPage;
    }

    public void rememberServiceCode(String serviceCode) {
        if (serviceCode != null && serviceCode.contains("|")) {
            this.lastServiceCode = serviceCode.split("\\|", 2)[0].trim();
        } else {
            this.lastServiceCode = serviceCode != null ? serviceCode.trim() : "";
        }
    }

    public String getLastServiceCode() {
        return lastServiceCode;
    }

    /**
     * After Mark As Ready To Bill, close the visit dialog and search the created visit in Checked-in Visits.
     */
    public void returnToPatientAccessAndSearchCreatedVisit() {
        String mrn = loginPage.getLastRegisteredMrn();
        if (mrn == null || mrn.isBlank()) {
            throw new IllegalStateException("Patient MRN was not captured during patient registration.");
        }
        loginPage.returnToPatientAccessVisitList();
        loginPage.searchVisitInPatientAccess(mrn);
    }

    public void openCreatedVisitFromPatientAccessList() {
        String mrn = loginPage.getLastRegisteredMrn();
        loginPage.openVisitFromPatientAccessListByMrn(mrn);
    }

    public boolean isVisitReadyToBillInPatientAccessList(String mrn) {
        return loginPage.isVisitReadyToBillInPatientAccessList(mrn);
    }

    /** Step 2 — visit is open on Patient Access and marked Ready to Bill. */
    public boolean isVisitOpenedAndMarkedReadyToBill() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(d -> isPatientAccessVisitOpen() && isReadyToBillIndicatorVisible());
            System.out.println("Visit is open and marked Ready to Bill.");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Ready to Bill visit not detected. Patient Access open="
                    + isPatientAccessVisitOpen() + ", readyIndicator=" + isReadyToBillIndicatorVisible());
            return false;
        }
    }

    /** Step 3 — edit a service on an open Ready to Bill claim via Diagnosis and Interventions. */
    public void modifyClaimByEditingService(Map<String, String> fields) {
        lastExpectedOrderStatus = fields.get("Order Status");
        if (lastExpectedOrderStatus == null) {
            lastExpectedOrderStatus = fields.get("order status");
        }
        loginPage.editActivityInDiagnosisTab(fields);
        System.out.println("Modified claim by editing service from Patient Access.");
    }

    /** Claim update completed without validation or growl errors; service remains on the claim. */
    public boolean isClaimModificationSuccessful() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(d -> !isBlockingOverlayVisible() && !hasVisibleErrorMessages());
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // evaluate final state below
        }
        boolean noErrors = !hasVisibleErrorMessages();
        boolean visitOpen = isPatientAccessVisitOpen();
        boolean servicePresent = lastServiceCode == null
                || lastServiceCode.isBlank()
                || loginPage.isServiceCodeDisplayedInActivityList(lastServiceCode);
        boolean orderStatusUpdated = isExpectedOrderStatusDisplayed();
        System.out.println("Claim modification check — noErrors=" + noErrors
                + ", visitOpen=" + visitOpen + ", servicePresent=" + servicePresent
                + ", orderStatusUpdated=" + orderStatusUpdated);
        return noErrors && visitOpen && servicePresent && orderStatusUpdated;
    }

    private boolean isExpectedOrderStatusDisplayed() {
        if (lastExpectedOrderStatus == null || lastExpectedOrderStatus.isBlank()) {
            return true;
        }
        loginPage.clickDiagnosisAndInterventions();
        return loginPage.isActivityOrderStatusDisplayedInList(0, lastExpectedOrderStatus.trim());
    }

    private boolean isPatientAccessVisitOpen() {
        try {
            if (driver.findElement(encounterSection).isDisplayed()) {
                return true;
            }
        } catch (Exception ignored) {
            // fall through
        }
        try {
            return driver.findElement(patientAndEncounterTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isReadyToBillIndicatorVisible() {
        for (WebElement element : driver.findElements(By.xpath(
                "//*[contains(normalize-space(.),'Ready to Bill')"
                        + " or contains(normalize-space(.),'Ready To Bill')"
                        + " or contains(normalize-space(.),'READY TO BILL')]"))) {
            try {
                if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        List<WebElement> markButtons = driver.findElements(markAsReadyToBillButton);
        boolean markButtonHidden = markButtons.isEmpty();
        if (!markButtonHidden) {
            try {
                markButtonHidden = !markButtons.get(0).isDisplayed();
            } catch (StaleElementReferenceException ignored) {
                markButtonHidden = true;
            }
        }
        if (markButtonHidden) {
            for (WebElement btn : driver.findElements(generateBillButton)) {
                try {
                    if (btn.isDisplayed()) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return false;
    }

    private boolean hasVisibleErrorMessages() {
        By[] locators = {
                By.cssSelector("div.ui-message-error-detail"),
                By.cssSelector("div.ui-message-error"),
                By.cssSelector("span.ui-growl-title"),
                By.cssSelector("div.ui-messages-error span")
        };
        for (By locator : locators) {
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (!element.isDisplayed()) {
                        continue;
                    }
                    String text = element.getText().trim().toLowerCase();
                    if (text.isEmpty()) {
                        continue;
                    }
                    if (text.contains("error") || text.contains("fail") || text.contains("invalid")
                            || element.getAttribute("class").contains("ui-message-error")
                            || element.getAttribute("class").contains("ui-growl-title")) {
                        System.out.println("Visible error on claim: " + element.getText().trim());
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return false;
    }

    private boolean isBlockingOverlayVisible() {
        for (WebElement overlay : driver.findElements(By.cssSelector(
                "#statusDialog_modal, .ui-widget-overlay.ui-dialog-mask, .ui-blockui, .ui-blockui-document"))) {
            try {
                if (overlay.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // continue
            }
        }
        return false;
    }
}
