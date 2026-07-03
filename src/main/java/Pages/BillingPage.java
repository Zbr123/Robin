package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * UC_16 — Billing / payment collection (Bill Management, categorized bills, paymodes).
 */
public class BillingPage {

    private static final By billManagementTableBody = By.id("phWLForm:phWLTbl_data");
    private static final By patientNameLinks = By.xpath(
            "//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patient_FullNameCol')]");
    private static final By encounterLegend = By.xpath("//legend[normalize-space()='Encounter']");
    private static final By activityListBody = By.xpath(
            "//tbody[contains(@id,'AccumedHaadActivityListForm:datalist')]");
    private static final By categorizedBillsTableBody = By.id("paymentForm:categorizedBillsTbl_data");
    private static final By selectAllCategorizedBillsCheckbox =
            By.id("paymentForm:categorizedBillsTbl_head_checkbox");
    private static final By addPaymentButton = By.id("paymentForm:addPaymentBtn");
    private static final By paymentTypeDropdown = By.xpath("//div[@id='paymentForm:payment:0:sfsfsdf']");
    private static final By paymentAmountField =
            By.xpath("//input[@id='paymentForm:payment:0:ForeigncurrencyAmount_input']");
    private static final By paymentSaveButton = By.xpath(
            "//button[contains(@onclick,'addPaymentCompleteBtn')][.//span[normalize-space()='Save']]");
    private static final By billingMenuLink = By.xpath(
            "//span[contains(@class,'layout-menuitem-text') and normalize-space()='Billing']/ancestor::a[1]");
    private static final By billManagementMenuLink = By.xpath(
            "//span[contains(@class,'layout-menuitem-text') and normalize-space()='Bill Management']/ancestor::a[1]");

    private final WebDriver driver;
    private final LoginPage loginPage;

    public BillingPage(WebDriver driver, LoginPage loginPage) {
        this.driver = driver;
        this.loginPage = loginPage;
    }

    public void loginAsCashier(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    public boolean isCashierHomeVisible() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.elementToBeClickable(billingMenuLink),
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//h4[text()='Patient Access'] | //a[span[text()='Patient Access']]"))));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    public void navigateToBillingScreen() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        waitForBlockingOverlaysToClose();
        WebElement billing = wait.until(ExpectedConditions.presenceOfElementLocated(billingMenuLink));
        clickElement(billing);
        System.out.println("Opened Billing menu.");
        WebElement billMgmt = wait.until(ExpectedConditions.presenceOfElementLocated(billManagementMenuLink));
        clickElement(billMgmt);
        wait.until(ExpectedConditions.visibilityOfElementLocated(billManagementTableBody));
        System.out.println("Billing / Bill Management screen is open.");
    }

    /** Opens a Ready to Bill claim from Patient Access, or falls back to Bill Management. */
    public void openClaimForPaymentCollection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        if (!tryOpenClaimFromPatientAccessReadyToBill(wait)) {
            if (!tryOpenClaimFromBillManagement(wait)) {
                throw new IllegalStateException(
                        "No claim available for payment collection in Patient Access or Bill Management.");
            }
        }
        waitForClaimToLoad(wait);
        generateBillsIfRequired(wait);
        scrollToPaymentSection();
        waitForCategorizedBillsTable(wait);
    }

    private boolean tryOpenClaimFromPatientAccessReadyToBill(WebDriverWait wait) {
        try {
            navigateToPatientAccessFromMenu(wait);
            By readyToBillLink = By.xpath(
                    "//tbody[@id='phWLForm:phWLTbl_data']//tr[.//span[contains(@id,'billing_Status')]"
                            + "[contains(translate(normalize-space(.),"
                            + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'READY TO BILL')]]"
                            + "//a[contains(@id,'patientNameLink') or contains(@id,'patient_FullNameCol')]");
            List<WebElement> links = driver.findElements(readyToBillLink);
            if (links.isEmpty()) {
                links = driver.findElements(By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a"));
            }
            if (links.isEmpty()) {
                System.out.println("No Ready to Bill / patient links on Patient Access list.");
                return false;
            }
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(links.get(0)));
            clickElement(link);
            System.out.println("Opened claim from Patient Access: " + link.getText().trim());
            return true;
        } catch (Exception e) {
            System.out.println("Patient Access claim open failed: " + e.getMessage());
            return false;
        }
    }

    private boolean tryOpenClaimFromBillManagement(WebDriverWait wait) {
        try {
            navigateToBillingScreen();
            WebElement link = findFirstBillManagementPatientLink(wait);
            if (link == null) {
                System.out.println("No patient links found on Bill Management list.");
                return false;
            }
            String name = link.getText().trim();
            clickElement(link);
            System.out.println("Opened claim from Bill Management: " + name);
            return true;
        } catch (Exception e) {
            System.out.println("Bill Management claim open failed: " + e.getMessage());
            return false;
        }
    }

    private WebElement findFirstBillManagementPatientLink(WebDriverWait wait) {
        By[] patterns = {
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patientNameLink')]"),
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patient_FullNameCol')]"),
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patient')]"),
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//tr[contains(@class,'ui-datatable-selectable')]//a")
        };
        for (By pattern : patterns) {
            for (WebElement link : driver.findElements(pattern)) {
                try {
                    if (link.isDisplayed() && link.isEnabled() && !link.getText().trim().isEmpty()) {
                        return link;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next link
                }
            }
        }
        List<WebElement> rows = driver.findElements(By.xpath(
                "//tbody[@id='phWLForm:phWLTbl_data']/tr[contains(@class,'ui-datatable-selectable')]"));
        for (WebElement row : rows) {
            try {
                if (row.isDisplayed() && !row.getText().trim().isEmpty()
                        && !row.getText().toLowerCase().contains("no record")) {
                    return row;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next row
            }
        }
        return null;
    }

    private void navigateToPatientAccessFromMenu(WebDriverWait wait) {
        waitForBlockingOverlaysToClose();
        By[] patientJourneyMenus = {
                By.xpath("//span[contains(@class,'layout-menuitem-text')"
                        + " and contains(normalize-space(.),'Patient Journey')]/ancestor::a[1]"),
                By.xpath("//span[contains(@class,'layout-menuitem-text')"
                        + " and normalize-space()='Patient Journey Management']/ancestor::a[1]")
        };
        for (By menuBy : patientJourneyMenus) {
            for (WebElement menu : driver.findElements(menuBy)) {
                try {
                    if (menu.isDisplayed()) {
                        clickElement(menu);
                        sleepQuietMs(400);
                        break;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        By[] patientAccessLinks = {
                By.xpath("//span[contains(@class,'layout-menuitem-text')"
                        + " and normalize-space()='Patient Access']/ancestor::a[1]"),
                By.xpath("//a[.//span[normalize-space()='Patient Access']]"),
                By.xpath("//nav//a[span[text()='Patient Access']]"),
                By.xpath("//h4[text()='Patient Access']/ancestor::a[1]")
        };
        for (By linkBy : patientAccessLinks) {
            for (WebElement link : driver.findElements(linkBy)) {
                try {
                    if (link.isDisplayed() && link.isEnabled()) {
                        clickElement(link);
                        wait.until(ExpectedConditions.visibilityOfElementLocated(billManagementTableBody));
                        System.out.println("Navigated to Patient Access from menu.");
                        return;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        loginPage.clickPatientAccess();
        wait.until(ExpectedConditions.visibilityOfElementLocated(billManagementTableBody));
    }

    private void scrollToPaymentSection() {
        for (By locator : new By[] {
                By.id("paymentForm:categorizedBillsTbl"),
                By.id("paymentForm:addPaymentBtn"),
                addPaymentButton
        }) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    if (el.isDisplayed()) {
                        scrollToElement(el);
                        return;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
    }

    public boolean isBillingClaimLoadedWithServicesAndBills() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        scrollToPaymentSection();
        try {
            wait.until(d -> isEncounterVisible() && hasActivityServices() && getCategorizedBillsRowCount() >= 1);
            System.out.println("Billing claim loaded — services and " + getCategorizedBillsRowCount()
                    + " categorized bill(s) visible.");
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Billing claim load check failed — encounter=" + isEncounterVisible()
                    + ", services=" + hasActivityServices()
                    + ", bills=" + getCategorizedBillsRowCount());
            return false;
        }
    }

    public void clearCategorizedBillSelection() {
        WebElement selectAll = waitForCategorizedBillsHeaderCheckbox();
        if (isCheckboxSelected(selectAll)) {
            clickCheckbox(selectAll);
            sleepQuietMs(400);
        }
        deselectAllVisibleBillRows();
        System.out.println("Cleared categorized bill selection.");
    }

    public void selectCategorizedBillRow(int rowIndex) {
        clearCategorizedBillSelection();
        clickCategorizedBillRowCheckbox(rowIndex);
        System.out.println("Selected categorized bill row " + rowIndex + ".");
    }

    public void selectCategorizedBillRows(int firstRowIndex, int secondRowIndex) {
        clearCategorizedBillSelection();
        int rowCount = getCategorizedBillsRowCount();
        clickCategorizedBillRowCheckbox(firstRowIndex);
        if (rowCount > 1) {
            int second = secondRowIndex < rowCount ? secondRowIndex : 1;
            clickCategorizedBillRowCheckbox(second);
        }
        System.out.println("Selected multiple categorized bills (rows available: " + rowCount + ").");
    }

    public int getExpectedMultiSelectCount() {
        int rows = getCategorizedBillsRowCount();
        int selected = getSelectedCategorizedBillsCount();
        return rows >= 2 ? Math.max(2, selected) : Math.max(1, selected);
    }

    public void selectAllCategorizedBills() {
        WebElement selectAll = waitForCategorizedBillsHeaderCheckbox();
        if (!isCheckboxSelected(selectAll)) {
            clickCheckbox(selectAll);
        }
        System.out.println("Selected all categorized bills.");
    }

    public int getSelectedCategorizedBillsCount() {
        int selected = 0;
        for (WebElement row : getVisibleCategorizedBillRows()) {
            try {
                if (isRowCheckboxSelected(row)) {
                    selected++;
                }
            } catch (StaleElementReferenceException ignored) {
                // re-count on next poll
            }
        }
        if (selected == 0) {
            WebElement selectAll = findElementQuietly(selectAllCategorizedBillsCheckbox);
            if (selectAll != null && isCheckboxSelected(selectAll)) {
                return getCategorizedBillsRowCount();
            }
        }
        return selected;
    }

    public void clickAddPayment() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addPaymentButton));
        clickElement(btn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTypeDropdown));
        System.out.println("Clicked Add Payment.");
    }

    public void collectPaymentUsingType(String paymentType) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(paymentTypeDropdown));
        selectPaymentType(paymentType, wait);
        enterDefaultPaymentAmount(wait);
        WebElement save = wait.until(ExpectedConditions.elementToBeClickable(paymentSaveButton));
        clickElement(save);
        waitForBlockingOverlaysToClose();
        System.out.println("Collected payment using type: " + paymentType);
    }

    public void collectPaymentForSelectedBills(String paymentType) {
        if (getSelectedCategorizedBillsCount() < 1) {
            selectAllCategorizedBills();
        }
        clickAddPayment();
        collectPaymentUsingType(paymentType);
    }

    public int getCategorizedBillsRowCount() {
        return getVisibleCategorizedBillRows().size();
    }

    private void generateBillsIfRequired(WebDriverWait wait) {
        List<WebElement> generateButtons = driver.findElements(By.id("AccumedHaadActivityListForm:generateBillBtn"));
        if (generateButtons.isEmpty()) {
            return;
        }
        try {
            WebElement generateBtn = generateButtons.get(0);
            if (generateBtn.isDisplayed() && generateBtn.isEnabled()) {
                clickElement(generateBtn);
                loginPage.clickAlertDialogOkButton();
                waitForBlockingOverlaysToClose();
                System.out.println("Generated bill(s) for claim.");
            }
        } catch (Exception e) {
            System.out.println("Generate Bill skipped: " + e.getMessage());
        }
    }

    private void waitForClaimToLoad(WebDriverWait wait) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(encounterLegend),
                ExpectedConditions.presenceOfElementLocated(activityListBody)));
        waitForBlockingOverlaysToClose();
    }

    private void waitForCategorizedBillsTable(WebDriverWait wait) {
        try {
            wait.until(d -> getCategorizedBillsRowCount() >= 1);
        } catch (org.openqa.selenium.TimeoutException e) {
            generateBillsIfRequired(wait);
            wait.until(d -> getCategorizedBillsRowCount() >= 1);
        }
    }

    private boolean isEncounterVisible() {
        try {
            return driver.findElement(encounterLegend).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasActivityServices() {
        for (WebElement row : driver.findElements(By.xpath(
                "//tbody[contains(@id,'AccumedHaadActivityListForm:datalist_data')]/tr"))) {
            try {
                if (row.isDisplayed() && !row.getText().trim().isEmpty()
                        && !row.getText().toLowerCase().contains("no records")) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next row
            }
        }
        return false;
    }

    private List<WebElement> getVisibleCategorizedBillRows() {
        return driver.findElements(By.xpath(
                "//tbody[@id='paymentForm:categorizedBillsTbl_data']"
                        + "/tr[contains(@class,'ui-datatable-selectable')]"))
                .stream()
                .filter(row -> {
                    try {
                        return row.isDisplayed() && !row.getText().trim().isEmpty();
                    } catch (StaleElementReferenceException e) {
                        return false;
                    }
                })
                .toList();
    }

    private void clickCategorizedBillRowCheckbox(int rowIndex) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By rowCheckbox = By.xpath("//tbody[@id='paymentForm:categorizedBillsTbl_data']"
                + "/tr[@data-ri='" + rowIndex + "']//div[contains(@class,'ui-chkbox-box')]");
        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(rowCheckbox));
        clickElement(box);
        sleepQuietMs(300);
    }

    private void deselectAllVisibleBillRows() {
        for (WebElement row : getVisibleCategorizedBillRows()) {
            try {
                if (isRowCheckboxSelected(row)) {
                    WebElement box = row.findElement(By.xpath(".//div[contains(@class,'ui-chkbox-box')]"));
                    clickElement(box);
                }
            } catch (Exception ignored) {
                // continue
            }
        }
    }

    private WebElement waitForCategorizedBillsHeaderCheckbox() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        return wait.until(ExpectedConditions.elementToBeClickable(selectAllCategorizedBillsCheckbox));
    }

    private boolean isRowCheckboxSelected(WebElement row) {
        try {
            WebElement input = row.findElement(By.xpath(".//input[@type='checkbox']"));
            return input.isSelected();
        } catch (Exception e) {
            return row.getAttribute("aria-selected") != null
                    && "true".equalsIgnoreCase(row.getAttribute("aria-selected"));
        }
    }

    private boolean isCheckboxSelected(WebElement checkboxContainer) {
        try {
            WebElement input = checkboxContainer.findElement(By.xpath(".//input[@type='checkbox']"));
            if (input.isSelected()) {
                return true;
            }
        } catch (Exception ignored) {
            // fall through
        }
        String clazz = checkboxContainer.getAttribute("class");
        return clazz != null && clazz.contains("ui-state-active");
    }

    private void clickCheckbox(WebElement checkboxContainer) {
        try {
            WebElement box = checkboxContainer.findElement(By.xpath(".//div[contains(@class,'ui-chkbox-box')]"));
            clickElement(box);
        } catch (Exception e) {
            clickElement(checkboxContainer);
        }
    }

    private void selectPaymentType(String paymentType, WebDriverWait wait) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(paymentTypeDropdown));
        clickElement(dropdown);
        String literal = xpathLiteral(paymentType.trim());
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//div[contains(@id,'paymentForm:payment:0:sfsfsdf_panel')]"
                        + "//li[contains(@data-label," + literal + ")"
                        + " or normalize-space()=" + literal + "]"
                        + " | //li[normalize-space()=" + literal + "]")));
        clickElement(option);
        sleepQuietMs(500);
    }

    private void enterDefaultPaymentAmount(WebDriverWait wait) {
        try {
            WebElement amount = wait.until(ExpectedConditions.presenceOfElementLocated(paymentAmountField));
            if (amount.isDisplayed() && amount.isEnabled()) {
                String existing = amount.getAttribute("value");
                if (existing == null || existing.isBlank() || "0".equals(existing.trim())) {
                    amount.clear();
                    amount.sendKeys("10000");
                }
            }
        } catch (Exception e) {
            loginPage.enterPaymentAmount10000();
        }
    }

    private WebElement findElementQuietly(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return elements.isEmpty() ? null : elements.get(0);
    }

    private void clickElement(WebElement element) {
        scrollToElement(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    private void waitForBlockingOverlaysToClose() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(d -> driver.findElements(By.cssSelector(
                    "#statusDialog_modal, .ui-widget-overlay.ui-dialog-mask, .ui-blockui"))
                    .stream()
                    .noneMatch(el -> {
                        try {
                            return el.isDisplayed();
                        } catch (StaleElementReferenceException e) {
                            return false;
                        }
                    }));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // continue
        }
        sleepQuietMs(400);
    }

    private static void sleepQuietMs(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }
}
