package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import java.time.Duration;
import java.util.Random;
import org.openqa.selenium.Keys;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.ElementNotInteractableException;


public class LoginPage {
    WebDriver driver;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    private String randomFirstName;
    private String randomLastName;
    private static String lastRegisteredMemberId;
    private static String lastRegisteredMrn;

    // Locators
    /** Login screen ready: username field (works when heading text/markup differs per tenant). */
    private By loginHeader = By.id("username");
    private By usernameField = By.xpath("//input[@id='username']");
    private By passwordField = By.xpath("//input[@id='password']");
    private By signInButton = By.xpath("//input[@value='Sign In']");
    private By patientAccess = By.xpath("//h4[text()='Patient Access']");
    private By patientAccessScreen = By.xpath("//nav//a[span[text()='Patient Access']]");
    private By newVisitButton = By.xpath("//span[text()='New Visit']");
    private By patientAndEncounterScreen = By.xpath("//a[contains(text(),'Patient and Encounter')]");
    private By addPatientButton = By.xpath("//button[@id='InvoiceForm:j_idt2268']");
    private By createPatientScreen = By.xpath("//span[text()='Create Patient']");
//    private By mrnField = By.xpath("//input[@name='AccumedPatientCreateForm:mrn']");
//    private By communityMrnField = By.xpath("//input[@id='AccumedPatientCreateForm:communityMrn']");
//    private By nationalIdField = By.xpath("//input[@id='AccumedPatientCreateForm:emiratesId1']");
//    private By firstNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientName']");
//    private By lastNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientSurname']");
    private By mrnField = By.xpath("//input[@name='AccumedPatientCreateForm:mrn']");
    private By communityMrnField = By.xpath("//input[@id='AccumedPatientCreateForm:communityMrn']");
    private By nationalIdField = By.id("AccumedPatientCreateForm:emiratesId2");
    private By firstNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientName']");
    private By lastNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientSurname']");

    private By dobField = By.xpath("//input[@id='AccumedPatientCreateForm:dateOfBirth2_input']");
    private By qidExpiryDateInput = By.xpath(
            "//input[@title='QID Expiry Date']"
                    + " | //input[@id='AccumedPatientCreateForm:qidExpiryDate_input']"
                    + " | //input[contains(@id,'AccumedPatientCreateForm') and contains(@id,'xpir') and contains(@class,'hasDatepicker')]");
    private By datepickerPanel = By.id("ui-datepicker-div");
    private By nationalityLabel = By.xpath("//label[@id='AccumedPatientCreateForm:nationality_label']");
    /** PrimeFaces selectOneMenu trigger (more reliable than label for opening the overlay). */
    private By nationalitySelectTrigger = By.xpath(
            "//div[@id='AccumedPatientCreateForm:nationality']//div[contains(@class,'ui-selectonemenu-trigger')]");
    /** Overlay that lists nationality options (id usually contains "nationality"). */
    private By nationalityDropdownPanel = By.xpath(
            "//div[contains(@id,'nationality')][contains(@class,'ui-selectonemenu-panel')]");
    /** Exact panel id for Accumed nationality selectOneMenu (avoids matching hidden panels). */
    private By nationalityDropdownPanelById = By.cssSelector("[id='AccumedPatientCreateForm:nationality_panel']");
    private By addInsuranceCardButton = By.xpath("//button[@id='AccumedPatientCreateForm:createButtonInsurance']");
    /** Add Insurance modal: Receiver dropdown (label id from DevTools). */
    private By addInsuranceModalReceiverLabel = By.id("AccumedPatientInsuranceCreateForm:insuranceLisence_label");
    /**
     * Add Insurance — Member ID textbox (distinct from Policy Number; DevTools typically
     * {@code input#AccumedPatientInsuranceCreateForm:patientInsuranceId}).
     */
    private static final String ADD_INSURANCE_MEMBER_ID_INPUT_ID = "AccumedPatientInsuranceCreateForm:patientInsuranceId";
    /**
     * Add Insurance — required Policy Number textbox (DevTools: {@code input#AccumedPatientInsuranceCreateForm:policyNumber},
     * {@code title="Policy Number"}). Not {@code patientInsuranceId} / Member ID.
     */
    private static final String ADD_INSURANCE_POLICY_NUMBER_INPUT_ID = "AccumedPatientInsuranceCreateForm:policyNumber";
    private By startDateField = By.xpath("//input[@id='AccumedPatientInsuranceCreateForm:startDate_input']");
    private By endDateField = By.xpath("//input[@id='AccumedPatientInsuranceCreateForm:endDate_input']");
    private By verifyDropdown = By.xpath("//div[@id='AccumedPatientInsuranceCreateForm:isver']");
    private By verifyTrueOption = By.xpath("//li[@data-label='True']");
    private By saveInsuranceCardButton = By.xpath("//button[@id='AccumedPatientInsuranceCreateForm:save']");

    private static final String RECEIVER_WIDGET = "AccumedPatientInsuranceCreateForm:insuranceLisence";
    /** Payer selectOneMenu client id (update if DevTools shows a different {@code id}). */
    private static final String PAYER_WIDGET = "AccumedPatientInsuranceCreateForm:payer";
    /** Priority selectOneMenu client id (update if DevTools shows a different {@code id}). */
    private static final String PRIORITY_WIDGET = "AccumedPatientInsuranceCreateForm:priority";
    private static final String NETWORK_WIDGET = "AccumedPatientInsuranceCreateForm:networkId";
    /** PrimeFaces selectOneMenu client id for Plan (panel/options use planId_panel, planId_items). */
    private static final String PLAN_WIDGET = "AccumedPatientInsuranceCreateForm:planId";
    /**
     * Add Insurance modal: Network / Plan dropdown openers ({@code *_label}). Use {@link By#id} so both
     * {@code <label>} and PrimeFaces {@code <span class="ui-selectonemenu-label">} resolve.
     */
    private By addInsuranceNetworkDropdownLabel = By.id("AccumedPatientInsuranceCreateForm:networkId_label");
    private By addInsurancePlanDropdownLabel = By.id("AccumedPatientInsuranceCreateForm:planId_label");
    private By insertPatientButton = By.xpath("//button[@id='AccumedPatientCreateForm:insertNewPatient']");
    private By encounterSection = By.xpath("//legend[normalize-space()='Encounter']");
    // Encounter mandatory fields only.
    private By encounterStartDateInput = By.id("InvoiceForm:startDate_input");
    private By encounterLocationInput = By.id("InvoiceForm:location_input");
    private By encounterDepartmentInput = By.id("InvoiceForm:dept_input");
    private By encounterAttendingClinicianInput = By.id("InvoiceForm:clinician_input");
    /** PrimeFaces selectOneMenu client ids (panel/options: {@code medicalServiceID_panel}, {@code eDTriageCode_panel}). */
    private static final String ENCOUNTER_MEDICAL_SERVICE_WIDGET = "InvoiceForm:medicalServiceID";
    private static final String ENCOUNTER_ED_TRIAGE_WIDGET = "InvoiceForm:eDTriageCode";
    /** ED Disposition Code selectOneMenu ({@code InvoiceForm:eDDisposition_Code}). */
    private static final String ENCOUNTER_ED_DISPOSITION_WIDGET = "InvoiceForm:eDDisposition_Code";
    /** Encounter Class / Status ({@code encounterForm:icd_input}, {@code encounterForm:encounterStatusValue_input}). */
    private static final String ENCOUNTER_CLASS_WIDGET = "encounterForm:icd";
    private static final String ENCOUNTER_STATUS_WIDGET = "encounterForm:encounterStatusValue";
    private static final String ENCOUNTER_CLASS_SELECT_ID = "encounterForm:icd_input";
    private static final String ENCOUNTER_STATUS_SELECT_ID = "encounterForm:encounterStatusValue_input";
    private By locationInputField = By.xpath("//input[@id='InvoiceForm:location_input']");
    private By locationDropdownOption = By.xpath("//td[text()='Family Medicine Clinic']");
    private By departmentInputField = By.xpath("//input[@id='InvoiceForm:dept_input']");
    private By departmentDropdownOption = By.xpath("//td[text()='Family Medicine']");
    private By attendingClinicianInputField = By.xpath("//input[@id='InvoiceForm:clinician_input']");
    private By attendingClinicianDropdownOption = By.xpath("//td[text()='DNNG9MB7EKsLFxUuzyxWaS']");
    private By orderingClinicianInputField = By.xpath("//input[@id='InvoiceForm:orderingclinician_input']");
    private By orderingClinicianDropdownOption = By.xpath("(//td[text()='DNNG9MB7EKsLFxUuzyxWaS'])[2]");
    private By startTypeDropdown = By.xpath("//div[@id='InvoiceForm:startType']//div[contains(@class, 'ui-selectonemenu-trigger')]");
    private By startTypeElectiveOption = By.xpath("//li[text()='Elective']");
    private By endTypeDropdown = By.xpath("//div[@id='InvoiceForm:endType']//div[contains(@class, 'ui-selectonemenu-trigger')]");
    private By endTypeDischargedOption = By.xpath("//li[text()='Discharged with approval']");
    private By visitTypeDropdown = By.xpath("//div[@id='InvoiceForm:visit_type']//div[contains(@class, 'ui-selectonemenu-trigger')]");
    private By visitTypeNewOption = By.xpath("//li[text()='New']");
    private By endDateInput = By.xpath("//input[@id='InvoiceForm:endDate_input']");
    private By diagnosisAndInterventionsLink = By.xpath("//a[text()='Diagnosis and Interventions']");
    private By addBulkDiagnosisButton = By.xpath("//button[@id='AccumedHaadDiagnosisListForm:j_idt2243']");
    private By activityTypeDropdown = By.xpath("//div[@id='AccumedHaadActivityListForm:activityType']//div[contains(@class, 'ui-selectonemenu-trigger')]");
    private By cptOption = By.xpath("//li[text()='CPT']");
    private By codeField = By.xpath("//input[@id='AccumedHaadActivityListForm:activityCode_input']");
    private static final String ACTIVITY_TYPE_WIDGET = "AccumedHaadActivityListForm:activityType";
    private static final String ACTIVITY_TYPE_SELECT_ID = "AccumedHaadActivityListForm:activityType_input";
    private static final String ACTIVITY_ORDER_STATUS_EDIT_WIDGET =
            "AccumedHaadActivityListForm:activityOrderStatusEditMode";
    private By activityCodeInput = By.id("AccumedHaadActivityListForm:activityCode_input");
    private By activityStartDateInput = By.id("AccumedHaadActivityListForm:startDate_input");
    private By generateBillButton = By.id("AccumedHaadActivityListForm:generateBillBtn");
    private By alertDialogOkButton = By.xpath(
            "//form[contains(@id,'alerForm')]//button[.//span[normalize-space()='OK']]"
                    + " | //button[contains(@id,'alerForm')][.//span[normalize-space()='OK']]");
    private By selectAllCategorizedBillsCheckbox = By.id("paymentForm:categorizedBillsTbl_head_checkbox");
    private By addPaymentButton = By.id("paymentForm:addPaymentBtn");
    private By paymentSaveButton = By.xpath(
            "//button[contains(@onclick,'addPaymentCompleteBtn')][.//span[normalize-space()='Save']]");
    private By billManagementMenuLink = By.xpath(
            "//span[contains(@class,'layout-menuitem-text') and normalize-space()='Bill Management']/ancestor::a[1]");
    private By code70030Option = By.xpath("//td[text()='70030']");
    private By addServiceButton = By.id("AccumedHaadActivityListForm:addServiceBTN");
    private By updateVisitButton = By.id("actionsFormInvoice:j_idt2194");   
    private By markAsReadyToBillButton = By.xpath("//button[span[text()='Mark As Ready To Bill']]");
    private By okButton = By.xpath("//span[text()='OK']");
    private By billingMenuLink = By.xpath(
            "//span[contains(@class,'layout-menuitem-text') and normalize-space()='Billing']/ancestor::a[1]");
    private By billingLink = By.xpath("//a[span[text()='Billing']]");
    private By opReceiptsLink = By.xpath("//a[span[text()='OP Receipts']]");
    private By paymentTypeDropdown = By.xpath("//div[@id='paymentForm:payment:0:sfsfsdf']");
    private By paymentAmountField = By.xpath("//input[@id='paymentForm:payment:0:ForeigncurrencyAmount_input']");
    private By paymentAmountField_cheque = By.xpath("//input[@id='paymentForm:payment:0:Amount_input']");
    private By generateReceiptButton = By.xpath("//button[span[text()='Generate Receipt & Invoice']]");
    private By receiptSuccessMessage = By.xpath("//span[text()='Receipt generated Successfully']");




    // Methods
    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    /** Fixed delay for manual pacing or AJAX settle (use sparingly). */
    public void waitForSeconds(int seconds) {
        if (seconds > 0) {
            sleepQuietMs(seconds * 1000);
        }
    }

    // Method to check if login header is displayed
    public boolean isLoginHeaderDisplayed() {
        return driver.findElement(loginHeader).isDisplayed();
    }

    // Getter method for login header locator
    public By getLoginHeaderLocator() {
        return loginHeader;
    }

    // Method to click Sign In button
    public void clickSignIn() {
        driver.findElement(signInButton).click();
    }

    public void clickPatientAccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        By[] candidates = {
                patientAccessScreen,
                patientAccess,
                By.xpath("//a[span[text()='Patient Access']]")
        };
        for (By by : candidates) {
            List<WebElement> links = driver.findElements(by);
            for (WebElement link : links) {
                try {
                    if (link.isDisplayed() && link.isEnabled()) {
                        scrollToElement(link);
                        try {
                            link.click();
                        } catch (ElementClickInterceptedException e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                        }
                        wait.until(ExpectedConditions.or(
                                ExpectedConditions.visibilityOfElementLocated(newVisitButton),
                                ExpectedConditions.visibilityOfElementLocated(patientAccess)));
                        return;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next candidate
                }
            }
        }
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(patientAccessScreen));
        scrollToElement(link);
        link.click();
    }

    // Method to verify Patient Access screen
    public boolean isPatientAccessScreenDisplayed() {
        return driver.findElement(patientAccessScreen).isDisplayed();
    }

    // Method to click on New Visit button
    public void clickNewVisitButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(newVisitButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(patientAndEncounterScreen));
    }

    // Method to verify "Patient and Encounter" screen is displayed
    public boolean isPatientAndEncounterScreenDisplayed() {
        return driver.findElement(patientAndEncounterScreen).isDisplayed();
    }

    public By getPatientAndEncounterScreenLocator() {
        return By.xpath("//a[contains(text(),'Patient and Encounter')]");
    }

    // Method to click on Add Patient button
    public void clickAddPatientButton() {
        driver.findElement(addPatientButton).click();
    }

    // Method to get the locator for WebDriverWait
    public By getCreatePatientScreenLocator() {
        return createPatientScreen;
    }

    // Method to check if "Create Patient" screen is displayed
    public boolean isCreatePatientScreenDisplayed() {
        return driver.findElement(createPatientScreen).isDisplayed();
    }

    // **Wait for an element to be visible and interactable**
    private WebElement waitForElementToBeVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForElementToBeClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // **Scroll into view before interaction**
    private void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }


    public String generateRandomString(String prefix) {
        return prefix + new Random().nextInt(100000);  // Appends a random number to make it unique
    }

    public String generateRandomID() {
        return String.valueOf(1000000000 + new Random().nextInt(900000000)); // Generates a 10-digit number
    }
    public String generateRandomNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // digits from 0 to 9
        }
        return sb.toString();
    }


    public void fillPatientFormWithRandomData() {
        String randomMRN = generateRandomString("TestMRN");
        String randomCommunityMRN = generateRandomString("AutoTest");
        String randomNationalID = generateRandomNumericString(15);
        String hardcodedDOB = "12/03/2015";
        randomFirstName = "Test" + UUID.randomUUID().toString().substring(0, 5);
        randomLastName = "Auto" + UUID.randomUUID().toString().substring(0, 5);

        // Logging to check random values
        System.out.println("Generated First Name: " + randomFirstName);
        System.out.println("Generated Last Name: " + randomLastName);

        randomFirstName = randomFirstName.toUpperCase();
        randomLastName = randomLastName.toUpperCase();

        driver.findElement(mrnField).sendKeys(randomMRN);
        driver.findElement(nationalIdField).sendKeys(randomNationalID);
        driver.findElement(firstNameField).sendKeys(randomFirstName);
        driver.findElement(lastNameField).sendKeys(randomLastName);
        driver.findElement(dobField).sendKeys(hardcodedDOB);
        typeQidExpiryDateDirect(21, 4, 2026);

        System.out.println("Filled Patient Form with Random Data:");
        System.out.println("MRN: " + randomMRN);
        System.out.println("Community MRN: " + randomCommunityMRN);
        System.out.println("National ID: " + randomNationalID);
        System.out.println("DOB: " + hardcodedDOB);  // Always the same
        System.out.println("QID Expiry Date: 21/04/2026 (calendar)");
        System.out.println("First Name: " + randomFirstName);
        System.out.println("Last Name: " + randomLastName);

        this.randomFirstName = randomFirstName;
        this.randomLastName = randomLastName;
        lastRegisteredMrn = randomMRN;
    }

    /**
     * Opens the PrimeFaces/jQuery datepicker for QID Expiry Date and picks the given day/month/year.
     */
    private void selectQidExpiryDateFromCalendar(int day, int month1Based, int year) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(qidExpiryDateInput));
        scrollToElement(input);
        WebElement trigger = null;
        try {
            trigger = input.findElement(By.xpath("./following-sibling::button"));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            try {
                trigger = input.findElement(By.xpath("./../button[contains(@class,'ui-datepicker-trigger')]"));
            } catch (org.openqa.selenium.NoSuchElementException ignored) {
                // no separate trigger
            }
        }
        if (trigger != null) {
            trigger.click();
        } else {
            input.click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(datepickerPanel));
        WebElement panel = driver.findElement(datepickerPanel);
        try {
            WebElement monthSelect = panel.findElement(By.cssSelector("select.ui-datepicker-month"));
            new Select(monthSelect).selectByIndex(month1Based - 1);
            WebElement yearSelect = panel.findElement(By.cssSelector("select.ui-datepicker-year"));
            new Select(yearSelect).selectByValue(Integer.toString(year));
        } catch (org.openqa.selenium.NoSuchElementException e) {
            typeQidExpiryDateDirect(day, month1Based, year);
            return;
        }
        String dayStr = Integer.toString(day);
        By dayCell = By.xpath(
                "//div[@id='ui-datepicker-div']//td[not(contains(@class,'ui-datepicker-other-month'))"
                        + " and not(contains(@class,'ui-datepicker-unselectable'))]//a[normalize-space()='" + dayStr + "']");
        try {
            WebElement dayLink = wait.until(ExpectedConditions.elementToBeClickable(dayCell));
            try {
                dayLink.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dayLink);
            }
            wait.until(ExpectedConditions.invisibilityOfElementLocated(datepickerPanel));
        } catch (org.openqa.selenium.TimeoutException e) {
            typeQidExpiryDateDirect(day, month1Based, year);
        }
    }

    private void typeQidExpiryDateDirect(int day, int month1Based, int year) {
        setQidExpiryDate(String.format("%02d/%02d/%04d", day, month1Based, year));
    }

    /** Fills National ID (QID) and expiry after No QID checkbox is unchecked. */
    public void fillPatientQid(String qidNumber, String expiryDateDdMmYyyy) {
        waitForNationalIdFieldReady();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement nationalIdInput = wait.until(ExpectedConditions.elementToBeClickable(nationalIdField));
        scrollToElement(nationalIdInput);
        nationalIdInput.click();
        nationalIdInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        nationalIdInput.sendKeys(Keys.DELETE);
        nationalIdInput.sendKeys(qidNumber);
        nationalIdInput.sendKeys(Keys.TAB);
        sleepQuietMs(600);
        setQidExpiryDate(expiryDateDdMmYyyy);
        System.out.println("Filled National ID (emiratesId2): " + qidNumber + ", expiry: " + expiryDateDdMmYyyy);
    }

    /** Waits until No QID is off and {@code emiratesId2} (National ID) is enabled. */
    public void waitForNationalIdFieldReady() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> {
            WebElement noQid = d.findElement(By.id("AccumedPatientCreateForm:NoQIDCheck_input"));
            if (noQid.isSelected()) {
                return false;
            }
            WebElement nationalId = d.findElement(nationalIdField);
            if (!nationalId.isDisplayed() || !nationalId.isEnabled()) {
                return false;
            }
            String cssClass = nationalId.getAttribute("class");
            return cssClass == null || !cssClass.contains("ui-state-disabled");
        });
    }

    public void setQidExpiryDate(String expiryDateDdMmYyyy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement input = wait.until(d -> findVisibleQidExpiryInput(d));
        scrollToElement(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(expiryDateDdMmYyyy);
        input.sendKeys(Keys.TAB);
    }

    private WebElement findVisibleQidExpiryInput(WebDriver driver) {
        for (WebElement candidate : driver.findElements(qidExpiryDateInput)) {
            try {
                if (candidate.isDisplayed() && candidate.isEnabled()) {
                    String cssClass = candidate.getAttribute("class");
                    if (cssClass == null || !cssClass.contains("ui-state-disabled")) {
                        return candidate;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // try next candidate
            }
        }
        return null;
    }

    public String getRandomFirstName() {
        return randomFirstName;
    }

    public String getRandomLastName() {
        return randomLastName;
    }

    public String getLastRegisteredMrn() {
        return lastRegisteredMrn;
    }

    public void rememberRegisteredMrn(String mrn) {
        if (mrn != null && !mrn.isBlank()) {
            lastRegisteredMrn = mrn.trim();
        }
    }


    public void selectNationalityAmerican() {
        selectSelectOneMenuOptionWithRetry("AccumedPatientCreateForm:nationality", "AMERICAN");
    }

    /** Opens nationality selectOneMenu and picks an option matching {@code searchText} (label or filter). */
    public void selectNationality(String searchText) {
        selectSelectOneMenuOptionWithRetry("AccumedPatientCreateForm:nationality", searchText.trim());
    }

    /** Create Patient form — selectOneMenu by label text or numeric list index. */
    public void selectCreatePatientDropdown(String widgetBaseId, String optionMatch) {
        if (optionMatch == null || optionMatch.isBlank()) {
            return;
        }
        if (optionMatch.matches("\\d+")) {
            selectSelectOneMenuByIndexWithRetry(widgetBaseId, Integer.parseInt(optionMatch));
        } else {
            selectSelectOneMenuOptionWithRetry(widgetBaseId, optionMatch);
        }
    }

    /**
     * Single-pass select (no retry loop): waits once for the selectOneMenu to be enabled,
     * opens the panel once, then clicks the option whose label/text contains {@code keyword}.
     */
    public void selectCreatePatientDropdownContainingOnce(String widgetBaseId, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return;
        }
        String needle = keyword.trim().toLowerCase();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(d -> {
            List<WebElement> widgets = d.findElements(By.id(widgetBaseId));
            if (widgets.isEmpty()) {
                return false;
            }
            WebElement widget = widgets.get(0);
            String cls = widget.getAttribute("class");
            return widget.isDisplayed() && (cls == null || !cls.contains("ui-state-disabled"));
        });
        openSelectOneMenuPanel(widgetBaseId, wait);
        String panelDomId = widgetBaseId + "_panel";
        wait.until(d -> selectOneMenuPanelHasVisibleItems(panelDomId));
        WebElement panel = findDisplayedPanelById(panelDomId);
        for (WebElement li : findSelectOneMenuListItems(panel)) {
            if (!li.isDisplayed()) {
                continue;
            }
            String dataLabel = li.getAttribute("data-label");
            String text = li.getText();
            String combined = ((dataLabel != null ? dataLabel : "") + " " + (text != null ? text : ""))
                    .toLowerCase();
            if (combined.contains(needle)) {
                clickListOption(li);
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "No option containing '" + keyword + "' in " + widgetBaseId);
    }

    /** Select first dropdown row whose label/text contains {@code keyword} (case-insensitive). */
    public void selectCreatePatientDropdownContaining(String widgetBaseId, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return;
        }
        org.openqa.selenium.TimeoutException lastTimeout = null;
        String needle = keyword.trim().toLowerCase();
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                openSelectOneMenuPanel(widgetBaseId, wait);
                String panelDomId = widgetBaseId + "_panel";
                wait.until(d -> selectOneMenuPanelHasVisibleItems(panelDomId));
                WebElement panel = findDisplayedPanelById(panelDomId);
                for (WebElement li : findSelectOneMenuListItems(panel)) {
                    if (!li.isDisplayed()) {
                        continue;
                    }
                    String dataLabel = li.getAttribute("data-label");
                    String text = li.getText();
                    String combined = ((dataLabel != null ? dataLabel : "") + " " + (text != null ? text : ""))
                            .toLowerCase();
                    if (combined.contains(needle)) {
                        clickListOption(li);
                        return;
                    }
                }
                throw new org.openqa.selenium.NoSuchElementException(
                        "No option containing '" + keyword + "' in " + widgetBaseId);
            } catch (org.openqa.selenium.TimeoutException | StaleElementReferenceException e) {
                lastTimeout = e instanceof org.openqa.selenium.TimeoutException
                        ? (org.openqa.selenium.TimeoutException) e : lastTimeout;
                dismissVisibleAutocompletePanelsIfPresent();
                sleepQuietMs(600);
            }
        }
        if (lastTimeout != null) {
            throw lastTimeout;
        }
    }

    private void selectSelectOneMenuByIndexWithRetry(String widgetBaseId, int index) {
        org.openqa.selenium.TimeoutException lastTimeout = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                openSelectOneMenuPanel(widgetBaseId, wait);
                String panelDomId = widgetBaseId + "_panel";
                wait.until(d -> selectOneMenuPanelHasVisibleItems(panelDomId));
                WebElement panel = findDisplayedPanelById(panelDomId);
                List<WebElement> items = findSelectOneMenuListItems(panel);
                if (index >= items.size()) {
                    throw new org.openqa.selenium.NoSuchElementException(
                            "Index " + index + " out of range for " + widgetBaseId + " (" + items.size() + " items)");
                }
                clickListOption(items.get(index));
                return;
            } catch (org.openqa.selenium.TimeoutException | StaleElementReferenceException e) {
                lastTimeout = e instanceof org.openqa.selenium.TimeoutException
                        ? (org.openqa.selenium.TimeoutException) e : lastTimeout;
                dismissVisibleAutocompletePanelsIfPresent();
                sleepQuietMs(600);
            }
        }
        if (lastTimeout != null) {
            throw lastTimeout;
        }
    }

    private WebElement waitForVisibleNationalityPanel(WebDriverWait wait) {
        try {
            return wait.until(driver -> {
                List<WebElement> panels = driver.findElements(nationalityDropdownPanelById);
                for (WebElement p : panels) {
                    if (p.isDisplayed()) {
                        return p;
                    }
                }
                List<WebElement> fuzzy = driver.findElements(nationalityDropdownPanel);
                for (WebElement p : fuzzy) {
                    if (p.isDisplayed()) {
                        return p;
                    }
                }
                return null;
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(nationalityDropdownPanel));
        }
    }

    private void tryApplyNationalityFilter(WebElement panel, String filterText) {
        try {
            WebElement filter = panel.findElement(By.cssSelector("input.ui-selectonemenu-filter"));
            if (filter.isDisplayed()) {
                filter.clear();
                filter.sendKeys(filterText.length() >= 3 ? filterText.substring(0, 3) : filterText);
            }
        } catch (org.openqa.selenium.NoSuchElementException ignored) {
            // no filter on this selectOneMenu
        }
    }

    private void tryApplyNationalityFilter(WebElement panel) {
        tryApplyNationalityFilter(panel, "AMERIC");
    }

    private WebElement findNationalityOptionInsidePanel(WebElement panel, WebDriverWait wait, String searchText) {
        String needle = searchText.trim();
        String[] relativeXpaths = {
                ".//li[@data-label=" + xpathLiteral(needle) + "]",
                ".//li[contains(@data-label," + xpathLiteral(needle) + ")]",
                ".//li[contains(normalize-space(.)," + xpathLiteral(needle) + ")]",
        };
        for (String rel : relativeXpaths) {
            try {
                for (WebElement li : panel.findElements(By.xpath(rel))) {
                    if (li.isDisplayed()) {
                        return li;
                    }
                }
            } catch (Exception ignored) {
                // try next pattern
            }
        }
        return wait.until(drv -> {
            for (WebElement li : panel.findElements(By.xpath(".//li[contains(@class,'ui-selectonemenu-item')]"))) {
                if (!li.isDisplayed()) {
                    continue;
                }
                String dl = li.getAttribute("data-label");
                if (dl != null && dl.toLowerCase().contains(needle.toLowerCase())) {
                    return li;
                }
                if (li.getText().trim().toLowerCase().contains(needle.toLowerCase())) {
                    return li;
                }
            }
            return null;
        });
    }

    private WebElement findNationalityDropdownOpener(WebDriverWait wait) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(nationalitySelectTrigger));
        } catch (org.openqa.selenium.TimeoutException e) {
            return wait.until(ExpectedConditions.elementToBeClickable(nationalityLabel));
        }
    }

    public void selectGender() {
        selectGender("Male");
    }

    /** Create Patient form — gender selectOneMenu (Male/Female). */
    public void selectGender(String gender) {
        if (gender == null || gender.isBlank()) {
            gender = "Male";
        }
        selectCreatePatientDropdown("AccumedPatientCreateForm:genderId", gender.trim());
    }

    // **Method to Click "Add Insurance Card"**
    public void clickAddInsuranceCard() {
        By[] buttons = {
                By.id("AccumedPatientCreateForm:createButtonInsurance"),
                By.id("AccumedPatientEditForm:createButtonInsurance")
        };
        WebElement button = null;
        for (By by : buttons) {
            for (WebElement candidate : driver.findElements(by)) {
                try {
                    if (candidate.isDisplayed() && candidate.isEnabled()) {
                        button = candidate;
                        break;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
            if (button != null) {
                break;
            }
        }
        if (button == null) {
            button = waitForElementToBeClickable(addInsuranceCardButton);
        }
        scrollToElement(button);
        button.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(addInsuranceModalReceiverLabel),
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(@id,'AccumedPatientInsuranceCreateForm')]"))));
    }

    /**
     * Fills mandatory fields on the Add Insurance modal (Insurance Details tab).
     * Keys (case-insensitive): Member ID, Receiver, Payer, Network Name, Plan, Policy Number,
     * Policy Start Date, Policy End Date, Priority.
     * <p><b>Member ID</b> and <b>Policy Number</b> are always filled with two different random 4-digit
     * strings ({@code 0000}–{@code 9999}) each run; table values for those keys are ignored.
     */
    public void fillAddInsuranceModalMandatoryFields(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(addInsuranceModalReceiverLabel),
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[contains(@id,'AccumedPatientInsuranceCreateForm')]"))));
        if (driver.findElements(addInsuranceModalReceiverLabel).isEmpty()
                || !driver.findElement(addInsuranceModalReceiverLabel).isDisplayed()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(addInsuranceModalReceiverLabel));
        }

        String randomMemberId = randomFourDigits();
        String randomPolicyNumber = randomFourDigitsDifferentFrom(randomMemberId);
        System.out.println(
                "Add Insurance modal: Member ID (random)=" + randomMemberId + ", Policy Number (random)="
                        + randomPolicyNumber);
        WebElement memberField = waitForVisibleAddInsuranceMemberIdInput(wait);
        scrollToElement(memberField);
        memberField.clear();
        memberField.sendKeys(randomMemberId);
        lastRegisteredMemberId = randomMemberId;
        System.out.println("Add Insurance modal: Member ID filled.");
        String receiver = mapGet(fields, "Receiver");
        if (receiver != null && !receiver.isEmpty()) {
            System.out.println("Add Insurance modal: selecting Receiver...");
            selectSelectOneMenuOptionWithRetry(RECEIVER_WIDGET, receiver);
            System.out.println("Add Insurance modal: Receiver selected.");
            waitForInsuranceDropdownEnabled(wait, NETWORK_WIDGET);
        }
        String payer = mapGet(fields, "Payer");
        if (payer != null && !payer.isEmpty()) {
            // In this tenant payer is often auto-populated/disabled right after receiver.
            if (isSelectOneMenuAlreadySet(PAYER_WIDGET, payer)) {
                System.out.println("Add Insurance modal: Payer already set, skipping explicit selection.");
            } else {
                try {
                    System.out.println("Add Insurance modal: selecting Payer...");
                    selectSelectOneMenuOption(PAYER_WIDGET, payer);
                    System.out.println("Add Insurance modal: Payer selected.");
                } catch (Exception e) {
                    System.out.println("Add Insurance modal: Payer selection skipped (" + e.getClass().getSimpleName() + ").");
                }
            }
        }
        String network = mapGet(fields, "Network Name");
        if (network != null && !network.isEmpty()) {
            System.out.println("Add Insurance modal: selecting Network...");
            selectSelectOneMenuOptionWithRetry(NETWORK_WIDGET, network.trim());
            // Network selection AJAX-refreshes Plan options.
            waitForInsuranceDropdownEnabled(wait, PLAN_WIDGET);
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.elementToBeClickable(addInsurancePlanDropdownLabel));
            System.out.println("Add Insurance modal: Network selected.");
        }
        String plan = mapGet(fields, "Plan");
        if (plan != null && !plan.isEmpty()) {
            System.out.println("Add Insurance modal: selecting Plan...");
            selectSelectOneMenuOptionWithRetry(PLAN_WIDGET, plan.trim());
            System.out.println("Add Insurance modal: Plan selected.");
        }
        WebElement policyField = waitForVisibleAddInsurancePolicyNumberInput(wait);
        scrollToElement(policyField);
        policyField.clear();
        policyField.sendKeys(randomPolicyNumber);
        System.out.println("Add Insurance modal: Policy Number filled.");
        String startDate = mapGet(fields, "Policy Start Date");
        String endDate = mapGet(fields, "Policy End Date");
        if (startDate != null && !startDate.isEmpty()) {
            WebElement startDateInput = waitForElementToBeVisible(startDateField);
            scrollToElement(startDateInput);
            startDateInput.clear();
            startDateInput.sendKeys(startDate);
            System.out.println("Add Insurance modal: Policy Start Date filled.");
        }
        if (endDate != null && !endDate.isEmpty()) {
            WebElement endDateInput = waitForElementToBeVisible(endDateField);
            scrollToElement(endDateInput);
            endDateInput.clear();
            endDateInput.sendKeys(endDate);
            System.out.println("Add Insurance modal: Policy End Date filled.");
        }
        String priority = mapGet(fields, "Priority");
        if (priority != null && !priority.isEmpty()) {
            try {
                System.out.println("Add Insurance modal: selecting Priority...");
                selectSelectOneMenuOption(PRIORITY_WIDGET, priority.trim());
                System.out.println("Add Insurance modal: Priority selected.");
            } catch (Exception e) {
                System.out.println("Add Insurance modal: Priority selection skipped (" + e.getClass().getSimpleName() + ").");
            }
        }
    }

    /** Legacy helper: policy number + policy dates only (same form ids). */
    public void fillInsuranceCardDetails(String cardNumber, String startDate, String endDate) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement policyField = waitForVisibleAddInsurancePolicyNumberInput(wait);
        scrollToElement(policyField);
        policyField.clear();
        policyField.sendKeys(cardNumber);

        WebElement startDateInput = waitForElementToBeVisible(startDateField);
        scrollToElement(startDateInput);
        startDateInput.clear();
        startDateInput.sendKeys(startDate);

        WebElement endDateInput = waitForElementToBeVisible(endDateField);
        scrollToElement(endDateInput);
        endDateInput.clear();
        endDateInput.sendKeys(endDate);
    }

    public void selectReceiver(String optionMatchSubstring) {
        selectSelectOneMenuOption(RECEIVER_WIDGET, optionMatchSubstring);
    }

    /** Kept for older scenarios; new modal flow uses fillAddInsuranceModalMandatoryFields. */
    public void selectVerifyTrue() {
        WebElement dropdown = waitForElementToBeClickable(verifyDropdown);
        scrollToElement(dropdown);
        dropdown.click();

        WebElement option = waitForElementToBeClickable(verifyTrueOption);
        option.click();
    }

    public void selectNetworkName(String optionMatchSubstring) {
        if (optionMatchSubstring == null || optionMatchSubstring.isBlank()) {
            return;
        }
        selectInsuranceModalDropdownByDataLabel(
                addInsuranceNetworkDropdownLabel, NETWORK_WIDGET, optionMatchSubstring.trim());
    }

    public void clickSaveInsuranceCard() {
        WebElement save = waitForElementToBeClickable(saveInsuranceCardButton);
        scrollToElement(save);
        save.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(addInsuranceModalReceiverLabel));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // modal may close without receiver label visibility change
        }
        sleepQuietMs(800);
    }

    public boolean isAddInsuranceModalDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(addInsuranceModalReceiverLabel));
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return driver.findElements(By.xpath("//*[contains(@id,'AccumedPatientInsuranceCreateForm')]"))
                    .stream().anyMatch(el -> {
                        try {
                            return el.isDisplayed();
                        } catch (Exception ex) {
                            return false;
                        }
                    });
        }
    }

    /**
     * Add Insurance modal — link baby to existing insured mother via Relationship = Child.
     */
    public void fillChildInsuranceLinkedToExistingPatient(String relationship, String existingPatientName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(@id,'AccumedPatientInsuranceCreateForm')]")));
        selectInsuranceRelationship(wait, relationship);
        linkExistingPatientInInsuranceModal(wait, existingPatientName);
        System.out.println("Child insurance linked to patient: " + existingPatientName);
    }

    private void selectInsuranceRelationship(WebDriverWait wait, String relationship) {
        String[] widgetIds = {
                "AccumedPatientInsuranceCreateForm:relationshipId",
                "AccumedPatientInsuranceCreateForm:relationship",
                "AccumedPatientInsuranceCreateForm:patientRelationship",
                "AccumedPatientInsuranceCreateForm:insuranceRelationship"
        };
        for (String widgetId : widgetIds) {
            if (!driver.findElements(By.id(widgetId)).isEmpty()
                    || !driver.findElements(By.id(widgetId + "_label")).isEmpty()) {
                selectSelectOneMenuOptionWithRetry(widgetId, relationship);
                return;
            }
        }
        By labelNearRelationship = By.xpath(
                "//*[contains(@id,'AccumedPatientInsuranceCreateForm')]"
                        + "//label[contains(translate(normalize-space(.),'RELATIONSHIP','relationship'),'relationship')]"
                        + "/following::*[contains(@class,'ui-selectonemenu')][1]//span[contains(@class,'ui-selectonemenu-label')]");
        if (!driver.findElements(labelNearRelationship).isEmpty()) {
            WebElement opener = wait.until(ExpectedConditions.elementToBeClickable(labelNearRelationship));
            opener.click();
            clickSelectOneMenuOptionByText(relationship, wait);
        }
    }

    private void linkExistingPatientInInsuranceModal(WebDriverWait wait, String patientName) {
        By[] candidateInputs = {
                By.xpath("//input[contains(@id,'AccumedPatientInsuranceCreateForm') and contains(@class,'ui-autocomplete-input')]"),
                By.xpath("//input[contains(@id,'AccumedPatientInsuranceCreateForm') and (contains(@id,'link')"
                        + " or contains(@id,'guarantor') or contains(@id,'existing') or contains(@id,'Patient'))"
                        + " and not(@type='hidden')]"),
                By.xpath("//input[contains(@id,'AccumedPatientInsuranceCreateForm') and @type='text']")
        };
        WebElement input = null;
        for (By by : candidateInputs) {
            List<WebElement> fields = driver.findElements(by);
            for (WebElement field : fields) {
                try {
                    if (field.isDisplayed() && field.isEnabled()) {
                        String id = field.getAttribute("id");
                        if (id != null && (id.contains("patientInsuranceId") || id.contains("policyNumber")
                                || id.contains("insuranceLisence") || id.contains("startDate")
                                || id.contains("endDate"))) {
                            continue;
                        }
                        input = field;
                        break;
                    }
                } catch (Exception ignored) {
                    // try next
                }
            }
            if (input != null) {
                break;
            }
        }
        if (input == null) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Existing patient link field not found in Add Insurance modal.");
        }
        scrollToElement(input);
        input.click();
        input.clear();
        String search = patientName.trim();
        input.sendKeys(search);
        sleepQuietMs(800);
        By suggestion = By.xpath(
                "//ul[contains(@class,'ui-autocomplete-items')]//li[contains(normalize-space(.), "
                        + xpathLiteral(search) + ")]"
                        + " | //div[contains(@class,'ui-autocomplete-panel')]//li[contains(normalize-space(.), "
                        + xpathLiteral(search) + ")]");
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(suggestion));
        scrollToElement(option);
        try {
            option.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }
        sleepQuietMs(500);
    }

    private void clickSelectOneMenuOptionByText(String optionText, WebDriverWait wait) {
        By option = By.xpath(
                "//div[contains(@class,'ui-selectonemenu-panel') and contains(@style,'display: block')]"
                        + "//li[contains(@data-label," + xpathLiteral(optionText) + ")"
                        + " or contains(normalize-space(.)," + xpathLiteral(optionText) + ")]");
        WebElement li = wait.until(ExpectedConditions.elementToBeClickable(option));
        clickListOption(li);
    }

    private static String randomFourDigits() {
        return String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    private static String randomFourDigitsDifferentFrom(String avoid) {
        for (int i = 0; i < 100; i++) {
            String s = randomFourDigits();
            if (!s.equals(avoid)) {
                return s;
            }
        }
        return String.format("%04d", (Integer.parseInt(avoid, 10) + 1) % 10000);
    }

    public String getLastRegisteredMemberId() {
        return lastRegisteredMemberId;
    }

    /** Generates a new 4-digit Member ID different from the last registered value. */
    public String generateAndRememberDifferentMemberId() {
        String current = lastRegisteredMemberId != null ? lastRegisteredMemberId : "";
        String candidate = randomFourDigitsDifferentFrom(current);
        lastRegisteredMemberId = candidate;
        System.out.println("Generated updated Member ID: " + candidate
                + (current.isEmpty() ? "" : " (was: " + current + ")"));
        return candidate;
    }

    public void rememberRegisteredMemberId(String memberId) {
        lastRegisteredMemberId = memberId != null ? memberId.trim() : "";
    }

    private static String mapGet(Map<String, String> fields, String wantedKey) {
        for (Map.Entry<String, String> e : fields.entrySet()) {
            if (e.getKey() != null && e.getKey().trim().equalsIgnoreCase(wantedKey)) {
                String v = e.getValue();
                return v == null ? null : v.trim();
            }
        }
        return null;
    }

    /** Resolves the visible Member ID input (may share duplicate-id pattern with other hidden copies). */
    private WebElement waitForVisibleAddInsuranceMemberIdInput(WebDriverWait wait) {
        return waitForVisibleInputById(wait, ADD_INSURANCE_MEMBER_ID_INPUT_ID);
    }

    /** Resolves the visible Policy Number input (DOM can contain duplicate ids; DevTools shows 1 of 2). */
    private WebElement waitForVisibleAddInsurancePolicyNumberInput(WebDriverWait wait) {
        return waitForVisibleInputById(wait, ADD_INSURANCE_POLICY_NUMBER_INPUT_ID);
    }

    private WebElement waitForVisibleInputById(WebDriverWait wait, String elementId) {
        return wait.until(d -> {
            List<WebElement> candidates = d.findElements(By.id(elementId));
            for (WebElement el : candidates) {
                try {
                    if (el.isDisplayed() && el.isEnabled()) {
                        return el;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // retry on next poll
                }
            }
            return null;
        });
    }

    private void clickSelectOneMenuTriggerWithRetry(By trigger) {
        for (int attempt = 0; attempt < 5; attempt++) {
            try {
                WebElement el = driver.findElement(trigger);
                scrollToElement(el);
                el.click();
                return;
            } catch (StaleElementReferenceException e) {
                if (attempt == 4) {
                    throw e;
                }
            }
        }
    }

    private static void sleepQuietMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean isSelectOneMenuPanelVisible(String panelDomId) {
        for (WebElement p : driver.findElements(By.id(panelDomId))) {
            try {
                if (p.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        return false;
    }

    /**
     * Opens PrimeFaces p:selectOneMenu: prefer label ({@code id}_label) like in DevTools, then wait for
     * {@code id}_panel; fall back to trigger arrow if the panel does not appear.
     */
    /** After Receiver (or Network) AJAX, dependent insurance dropdowns must be enabled before interaction. */
    private void waitForInsuranceDropdownEnabled(WebDriverWait wait, String widgetBaseId) {
        By labelBy = By.id(widgetBaseId + "_label");
        wait.until(ExpectedConditions.elementToBeClickable(labelBy));
        wait.until(d -> {
            try {
                WebElement widget = d.findElement(By.id(widgetBaseId));
                if (!widget.isDisplayed()) {
                    return false;
                }
                String cls = widget.getAttribute("class");
                return cls == null || !cls.contains("ui-state-disabled");
            } catch (org.openqa.selenium.NoSuchElementException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    private boolean selectOneMenuPanelHasVisibleItems(String panelDomId) {
        for (WebElement panel : driver.findElements(By.id(panelDomId))) {
            try {
                if (!panel.isDisplayed()) {
                    continue;
                }
                for (WebElement li : findSelectOneMenuListItems(panel)) {
                    if (li.isDisplayed()) {
                        return true;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // retry on next poll
            }
        }
        return false;
    }

    private void openSelectOneMenuPanel(String widgetBaseId, WebDriverWait wait) {
        String panelDomId = widgetBaseId + "_panel";
        if (isSelectOneMenuPanelVisible(panelDomId) && selectOneMenuPanelHasVisibleItems(panelDomId)) {
            return;
        }
        By labelBy = By.id(widgetBaseId + "_label");
        List<WebElement> labels = driver.findElements(labelBy);
        if (!labels.isEmpty()) {
            try {
                WebElement label = labels.get(0);
                if (label.isDisplayed()) {
                    scrollToElement(label);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", label);
                }
            } catch (StaleElementReferenceException ignored) {
                // fall through to trigger
            }
        }
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(d -> isSelectOneMenuPanelVisible(panelDomId));
            return;
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // open via trigger arrow
        }
        By trigger = By.xpath("//*[@id='" + widgetBaseId + "']//div[contains(@class,'ui-selectonemenu-trigger')]");
        wait.until(ExpectedConditions.elementToBeClickable(trigger));
        clickSelectOneMenuTriggerWithRetry(trigger);
        wait.until(d -> isSelectOneMenuPanelVisible(panelDomId));
    }

    private WebElement findVisibleSelectOneMenuPanel(String panelDomId, WebDriverWait wait) {
        return wait.until(d -> {
            List<WebElement> list = d.findElements(By.id(panelDomId));
            for (int i = list.size() - 1; i >= 0; i--) {
                WebElement p = list.get(i);
                try {
                    if (p.isDisplayed()) {
                        return p;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // continue
                }
            }
            return null;
        });
    }

    private static By liByDataLabelInPanel(String dataLabel) {
        String v = dataLabel.trim().replace("\\", "\\\\").replace("\"", "\\\"");
        return By.cssSelector("li[data-label=\"" + v + "\"]");
    }

    /** True when widget label already has a real selected value matching expected text. */
    private boolean isSelectOneMenuAlreadySet(String widgetBaseId, String expectedValue) {
        try {
            WebElement label = driver.findElement(By.id(widgetBaseId + "_label"));
            if (!label.isDisplayed()) {
                return false;
            }
            String text = label.getText();
            if (text == null) {
                return false;
            }
            String t = text.replace('\u00a0', ' ').trim();
            if (t.isEmpty() || t.equals("...") || t.equalsIgnoreCase("Select")) {
                return false;
            }
            return optionMatchesDropdownValue(t, expectedValue);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Opens the Add Insurance Network or Plan {@code p:selectOneMenu} using the label control, then selects the row
     * whose {@code data-label} exactly matches the scenario value (e.g. {@code AlKoot Network}, {@code Alkoot Plan}).
     */
    private void selectInsuranceModalDropdownByDataLabel(By dropdownLabelBy, String widgetBaseId, String dataLabel) {
        if (dataLabel == null || dataLabel.isBlank()) {
            return;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement label = wait.until(ExpectedConditions.elementToBeClickable(dropdownLabelBy));
        scrollToElement(label);
        selectSelectOneMenuOption(widgetBaseId, dataLabel);
    }

    private void selectSelectOneMenuOption(String widgetBaseId, String optionMatch) {
        if (optionMatch == null || optionMatch.isBlank()) {
            return;
        }
        if (isSelectOneMenuAlreadySet(widgetBaseId, optionMatch)) {
            return;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        openSelectOneMenuPanel(widgetBaseId, wait);
        String panelDomId = widgetBaseId + "_panel";
        // Panel can open before PrimeFaces finishes loading list items (e.g. Network after Receiver).
        wait.until(d -> selectOneMenuPanelHasVisibleItems(panelDomId));
        clickMatchingSelectOneMenuItem(panelDomId, optionMatch.trim(), wait);
    }

    private void selectSelectOneMenuOptionWithRetry(String widgetBaseId, String optionMatch) {
        if (optionMatch == null || optionMatch.isBlank()) {
            return;
        }
        if (isSelectOneMenuAlreadySet(widgetBaseId, optionMatch)) {
            return;
        }
        org.openqa.selenium.TimeoutException lastTimeout = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                selectSelectOneMenuOption(widgetBaseId, optionMatch);
                if (isSelectOneMenuAlreadySet(widgetBaseId, optionMatch)) {
                    return;
                }
            } catch (org.openqa.selenium.TimeoutException | StaleElementReferenceException e) {
                lastTimeout = e instanceof org.openqa.selenium.TimeoutException
                        ? (org.openqa.selenium.TimeoutException) e : lastTimeout;
                System.out.println("Retry selectOneMenu " + widgetBaseId + " (attempt " + (attempt + 1) + ")...");
                dismissVisibleAutocompletePanelsIfPresent();
                sleepQuietMs(600);
            }
        }
        if (lastTimeout != null) {
            throw lastTimeout;
        }
    }

    private WebElement findDisplayedPanelById(String panelDomId) {
        List<WebElement> panels = driver.findElements(By.id(panelDomId));
        for (int i = panels.size() - 1; i >= 0; i--) {
            try {
                if (panels.get(i).isDisplayed()) {
                    return panels.get(i);
                }
            } catch (StaleElementReferenceException ignored) {
                // try next panel instance
            }
        }
        return null;
    }

    private void applySelectOneMenuFilterIfPresent(String panelDomId, String optionMatch) {
        WebElement panel = findDisplayedPanelById(panelDomId);
        if (panel == null) {
            return;
        }
        try {
            List<WebElement> filters = panel.findElements(By.cssSelector("input.ui-selectonemenu-filter"));
            if (filters.isEmpty()) {
                return;
            }
            WebElement f = filters.get(0);
            if (!f.isDisplayed()) {
                return;
            }
            String q = optionMatch.length() > 16 ? optionMatch.substring(0, 16) : optionMatch;
            f.clear();
            f.sendKeys(q);
            String match = optionMatch;
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> {
                WebElement activePanel = findDisplayedPanelById(panelDomId);
                if (activePanel == null) {
                    return false;
                }
                try {
                    for (WebElement li : findSelectOneMenuListItems(activePanel)) {
                        if (li.isDisplayed() && rowMatchesSelectOption(li, match)) {
                            return true;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    return false;
                }
                return false;
            });
        } catch (Exception ignored) {
            // no filter or not usable
        }
    }

    private List<WebElement> findSelectOneMenuListItems(WebElement panel) {
        List<WebElement> items = panel.findElements(By.xpath(".//div[contains(@class,'ui-selectonemenu-items')]//li"));
        if (!items.isEmpty()) {
            return items;
        }
        items = panel.findElements(By.xpath(".//ul[contains(@class,'ui-selectonemenu-items')]//li"));
        if (!items.isEmpty()) {
            return items;
        }
        items = panel.findElements(By.xpath(
                ".//li[contains(@class,'ui-selectonemenu-item') or contains(@class,'ui-selectonemenu-list-item')]"));
        if (!items.isEmpty()) {
            return items;
        }
        return panel.findElements(By.xpath(".//li[@role='option']"));
    }

    private boolean rowMatchesSelectOption(WebElement li, String optionMatch) {
        try {
            String dataLabel = li.getAttribute("data-label");
            if (dataLabel != null) {
                String dl = dataLabel.trim();
                String want = optionMatch.trim();
                if (dl.equalsIgnoreCase(want) || optionMatchesDropdownValue(dataLabel, optionMatch)) {
                    return true;
                }
            }
            String text = li.getText();
            if (text != null && !text.isBlank() && optionMatchesDropdownValue(text, optionMatch)) {
                return true;
            }
            String textContent = li.getAttribute("textContent");
            if (textContent != null && optionMatchesDropdownValue(textContent, optionMatch)) {
                return true;
            }
        } catch (StaleElementReferenceException ignored) {
            return false;
        }
        return false;
    }

    private void clickMatchingSelectOneMenuItem(String panelDomId, String optionMatch, WebDriverWait wait) {
        wait.until(d -> findDisplayedPanelById(panelDomId) != null);
        applySelectOneMenuFilterIfPresent(panelDomId, optionMatch);
        String match = optionMatch.trim();
        WebElement option = wait.until(d -> {
            WebElement activePanel = findDisplayedPanelById(panelDomId);
            if (activePanel == null) {
                return null;
            }
            for (WebElement li : findSelectOneMenuListItems(activePanel)) {
                try {
                    if (li.isDisplayed() && rowMatchesSelectOption(li, match)) {
                        return li;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // list rerendered
                }
            }
            return null;
        });
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                clickListOption(option);
                return;
            } catch (StaleElementReferenceException e) {
                option = wait.until(d -> {
                    WebElement activePanel = findDisplayedPanelById(panelDomId);
                    if (activePanel == null) {
                        return null;
                    }
                    for (WebElement li : findSelectOneMenuListItems(activePanel)) {
                        try {
                            if (li.isDisplayed() && rowMatchesSelectOption(li, match)) {
                                return li;
                            }
                        } catch (StaleElementReferenceException ignored) {
                            // retry
                        }
                    }
                    return null;
                });
            }
        }
    }

    /** Full substring match, or when value uses " | ", all segments must appear (spacing-insensitive). */
    private boolean optionMatchesDropdownValue(String haystack, String optionMatch) {
        if (haystack == null || optionMatch == null) {
            return false;
        }
        String h = haystack.toLowerCase().replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
        String n = optionMatch.toLowerCase().replace('\u00a0', ' ').replaceAll("\\s+", " ").trim();
        if (h.contains(n)) {
            return true;
        }
        if (n.contains("|")) {
            boolean allFound = true;
            for (String part : n.split("\\|")) {
                String t = part.trim();
                if (!t.isEmpty() && !h.contains(t)) {
                    allFound = false;
                    break;
                }
            }
            return allFound;
        }
        return false;
    }

    private void clickListOption(WebElement li) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", li);
        try {
            new Actions(driver).moveToElement(li).click().perform();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", li);
        }
    }

    /** After Save on insurance card, Insert Patient should become actionable without a fixed delay. */
    public void waitForInsertPatientButtonReady() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(insertPatientButton));
    }

    public boolean isCreatePatientInsertButtonVisible() {
        try {
            WebElement btn = driver.findElement(insertPatientButton);
            return btn.isDisplayed() && btn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickInsertPatientButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(insertPatientButton));
                scrollToElement(btn);
                btn.click();
                System.out.println("Clicked on Insert Patient button.");
                wait.until(ExpectedConditions.visibilityOfElementLocated(encounterSection));
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("Insert Patient button stale, re-finding...");
            } catch (ElementClickInterceptedException e) {
                WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(insertPatientButton));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                System.out.println("Clicked on Insert Patient button (JS).");
                wait.until(ExpectedConditions.visibilityOfElementLocated(encounterSection));
                return;
            }
        }
    }

    public boolean isEncounterSectionVisible() {
        return driver.findElement(encounterSection).isDisplayed();
    }

    public By getEncounterSectionLocator() {
        return encounterSection;
    }

    /**
     * Root container for the visit "Encounter" block (fieldset or panel wrapping the legend).
     * All relative xpaths below are scoped under this node.
     */
    private static final String ENCOUNTER_CONTAINER =
            "//*[self::fieldset or self::div][.//legend[contains(normalize-space(.),'Encounter')]][1]";

    /**
     * Fills mandatory encounter fields only, using the same flow as manual entry: text / autocomplete /
     * {@code p:selectOneMenu} near each label inside the Encounter section.
     * <p>Supported keys (case-insensitive): Encounter Class, Encounter Status, Start Date, Location, Department,
     * Attending Clinician, Medical Service, ED Triage Code (or ED Triage), ED Disposition Code
     * (or Disposition Code / ED Disposition).
     */
    public void fillMandatoryEncounterFields(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement encounter = wait.until(ExpectedConditions.visibilityOfElementLocated(encounterSection));
        scrollToElement(encounter);

        String startDate = mapGet(fields, "Start Date");
        if (startDate != null && !startDate.isBlank()) {
            WebElement input = findEncounterVisibleInputWithFallback(wait, encounterStartDateInput, "Start Date");
            setEncounterTextInputWithRetry(input, startDate.trim());
            System.out.println("Encounter: Start Date filled.");
        }
        String location = mapGet(fields, "Location");
        if (location != null && !location.isBlank()) {
            encounterAutocompleteWithRetry(wait, "Location", location.trim());
        }
        String department = mapGet(fields, "Department");
        if (department != null && !department.isBlank()) {
            wait.until(d -> {
                try {
                    WebElement dept = d.findElement(encounterDepartmentInput);
                    return dept.isDisplayed() && dept.isEnabled();
                } catch (Exception e) {
                    return false;
                }
            });
            encounterAutocompleteWithRetry(wait, "Department", department.trim());
        }
        String attending = mapGet(fields, "Attending Clinician");
        if (attending != null && !attending.isBlank()) {
            encounterAutocompleteWithRetry(wait, "Attending Clinician", attending.trim());
        }
        String medService = mapGet(fields, "Medical Service");
        if (medService != null && !medService.isBlank()) {
            dismissVisibleAutocompletePanels();
            selectSelectOneMenuOptionWithRetry(ENCOUNTER_MEDICAL_SERVICE_WIDGET, medService.trim());
            System.out.println("Encounter: Medical Service selected.");
        }
        String edTriage = mapGet(fields, "ED Triage Code");
        if (edTriage == null || edTriage.isBlank()) {
            edTriage = mapGet(fields, "ED Triage");
        }
        if (edTriage != null && !edTriage.isBlank()) {
            dismissVisibleAutocompletePanels();
            selectSelectOneMenuOptionWithRetry(ENCOUNTER_ED_TRIAGE_WIDGET, edTriage.trim());
            System.out.println("Encounter: ED Triage selected.");
            sleepQuietMs(400);
        }
        String edDisposition = mapGet(fields, "ED Disposition Code");
        if (edDisposition == null || edDisposition.isBlank()) {
            edDisposition = mapGet(fields, "Disposition Code");
        }
        if (edDisposition == null || edDisposition.isBlank()) {
            edDisposition = mapGet(fields, "ED Disposition");
        }
        if (edDisposition != null && !edDisposition.isBlank()) {
            dismissVisibleAutocompletePanels();
            waitForInsuranceDropdownEnabled(wait, ENCOUNTER_ED_DISPOSITION_WIDGET);
            selectSelectOneMenuOptionWithRetry(ENCOUNTER_ED_DISPOSITION_WIDGET, edDisposition.trim());
            System.out.println("Encounter: ED Disposition Code selected.");
        }

        dismissVisibleAutocompletePanels();
        String encounterClass = mapGet(fields, "Encounter Class");
        if (encounterClass != null && !encounterClass.isBlank()) {
            String classValue = encounterClass.trim();
            selectEncounterFormDropdown(wait, ENCOUNTER_CLASS_WIDGET, ENCOUNTER_CLASS_SELECT_ID, classValue);
            System.out.println("Encounter: Encounter Class selected.");
            String encounterStatus = mapGet(fields, "Encounter Status");
            if (encounterStatus != null && !encounterStatus.isBlank()) {
                wait.until(d -> isEncounterFormDropdownReady(ENCOUNTER_STATUS_WIDGET)
                        && encounterFormDropdownShowsValue(
                                ENCOUNTER_CLASS_WIDGET, ENCOUNTER_CLASS_SELECT_ID, classValue));
                selectEncounterFormDropdown(
                        wait, ENCOUNTER_STATUS_WIDGET, ENCOUNTER_STATUS_SELECT_ID, encounterStatus.trim());
                System.out.println("Encounter: Encounter Status selected.");
            }
        } else {
            String encounterStatus = mapGet(fields, "Encounter Status");
            if (encounterStatus != null && !encounterStatus.isBlank()) {
                selectEncounterFormDropdown(
                        wait, ENCOUNTER_STATUS_WIDGET, ENCOUNTER_STATUS_SELECT_ID, encounterStatus.trim());
                System.out.println("Encounter: Encounter Status selected.");
            }
        }
    }

    private void setEncounterTextInputWithRetry(WebElement input, String value) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                scrollToElement(input);
                input.click();
                try {
                    input.clear();
                    input.sendKeys(value);
                } catch (org.openqa.selenium.InvalidElementStateException e) {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));"
                                    + "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
                            input, value);
                }
                try {
                    input.sendKeys(Keys.TAB);
                } catch (Exception ignored) {}
                ((JavascriptExecutor) driver).executeScript("document.body.click();");
                return;
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element on text input, re-finding...");
            }
        }
    }

    private void encounterAutocompleteWithRetry(WebDriverWait wait, String labelContains, String typeText) {
        org.openqa.selenium.TimeoutException lastTimeout = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                doEncounterAutocomplete(wait, labelContains, typeText);
                return;
            } catch (StaleElementReferenceException
                    | org.openqa.selenium.TimeoutException
                    | InvalidElementStateException e) {
                lastTimeout = e instanceof org.openqa.selenium.TimeoutException
                        ? (org.openqa.selenium.TimeoutException) e : lastTimeout;
                System.out.println("Retry autocomplete for " + labelContains + " (attempt " + (attempt + 1) + ")...");
                dismissVisibleAutocompletePanelsIfPresent();
            }
        }
        if (lastTimeout != null) {
            throw lastTimeout;
        }
    }

    private void clearEncounterAutocompleteInput(WebElement input) {
        try {
            input.clear();
        } catch (InvalidElementStateException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", input);
        }
    }

    private void doEncounterAutocomplete(WebDriverWait wait, String labelContains, String typeText) {
        By preferredBy;
        if ("Location".equalsIgnoreCase(labelContains)) {
            preferredBy = encounterLocationInput;
        } else if ("Department".equalsIgnoreCase(labelContains)) {
            preferredBy = encounterDepartmentInput;
        } else {
            preferredBy = encounterAttendingClinicianInput;
        }
        WebElement input = findEncounterVisibleInputWithFallback(wait, preferredBy, labelContains);
        scrollToElement(input);
        try {
            input.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            input = findEncounterVisibleInputWithFallback(wait, preferredBy, labelContains);
            scrollToElement(input);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        }
        dismissVisibleAutocompletePanels();
        clearEncounterAutocompleteInput(input);
        try {
            input.sendKeys(typeText);
        } catch (ElementNotInteractableException e) {
            input = findEncounterVisibleInputWithFallback(wait, preferredBy, labelContains);
            scrollToElement(input);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
            clearEncounterAutocompleteInput(input);
            input.sendKeys(typeText);
        }
        String trimmed = typeText.trim();
        By panelBy = By.xpath("//*[contains(@class,'ui-autocomplete-panel')]");
        WebDriverWait rowWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement row = rowWait.until(d -> {
            List<WebElement> panels = d.findElements(panelBy);
            for (int i = panels.size() - 1; i >= 0; i--) {
                WebElement panel = panels.get(i);
                try {
                    if (!panel.isDisplayed()) {
                        continue;
                    }
                    WebElement match = findMatchingAutocompleteRowInPanel(panel, trimmed);
                    if (match != null) {
                        return match;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // panel rerendered; try next
                }
            }
            return null;
        });
        clickListOption(row);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(panelBy));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // overlay may stay in DOM hidden
        }
        dismissVisibleAutocompletePanels();
        System.out.println("Encounter: " + labelContains + " selected.");
    }

    /**
     * Encounter Class / Status use {@code encounterForm:*} PrimeFaces selectOneMenu (hidden {@code select} + visible label).
     * Waits until the label or hidden select reflects the choice and the panel closes before returning.
     */
    private void selectEncounterFormDropdown(
            WebDriverWait wait, String widgetBaseId, String hiddenSelectId, String visibleText) {
        String expected = visibleText.trim();
        dismissVisibleAutocompletePanels();
        wait.until(d -> isEncounterFormDropdownReady(widgetBaseId));
        if (encounterFormDropdownShowsValue(widgetBaseId, hiddenSelectId, expected)) {
            return;
        }
        WebDriverWait confirmWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String panelDomId = widgetBaseId + "_panel";
        for (int attempt = 0; attempt < 3; attempt++) {
            if (encounterFormDropdownShowsValue(widgetBaseId, hiddenSelectId, expected)) {
                return;
            }
            try {
                selectSelectOneMenuOption(widgetBaseId, expected);
            } catch (Exception e) {
                System.out.println("Encounter dropdown " + widgetBaseId + ": selectOneMenu click failed (attempt "
                        + (attempt + 1) + ").");
            }
            try {
                waitForSelectOneMenuPanelClosed(panelDomId);
                waitForEncounterFormDropdownValue(confirmWait, widgetBaseId, hiddenSelectId, expected);
                dismissVisibleAutocompletePanels();
                return;
            } catch (org.openqa.selenium.TimeoutException e) {
                System.out.println("Encounter dropdown " + widgetBaseId + " not confirmed, retry...");
                dismissVisibleAutocompletePanelsIfPresent();
            }
        }
        if (!encounterFormDropdownShowsValue(widgetBaseId, hiddenSelectId, expected)) {
            System.out.println("Encounter dropdown " + widgetBaseId + ": using JS on hidden select.");
            selectEncounterFormDropdownViaJsNativeSelect(hiddenSelectId, expected);
        }
        waitForSelectOneMenuPanelClosed(panelDomId);
        waitForEncounterFormDropdownValue(confirmWait, widgetBaseId, hiddenSelectId, expected);
        dismissVisibleAutocompletePanels();
    }

    private boolean isEncounterFormDropdownReady(String widgetBaseId) {
        try {
            WebElement widget = driver.findElement(By.id(widgetBaseId));
            if (!widget.isDisplayed()) {
                return false;
            }
            String cls = widget.getAttribute("class");
            if (cls != null && cls.contains("ui-state-disabled")) {
                return false;
            }
            WebElement label = driver.findElement(By.id(widgetBaseId + "_label"));
            return label.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean encounterFormDropdownShowsValue(String widgetBaseId, String hiddenSelectId, String expected) {
        if (isSelectOneMenuAlreadySet(widgetBaseId, expected)) {
            return true;
        }
        try {
            WebElement select = driver.findElement(By.id(hiddenSelectId));
            String text = new Select(select).getFirstSelectedOption().getText();
            if (text == null) {
                return false;
            }
            String t = text.replace('\u00a0', ' ').trim();
            return optionMatchesDropdownValue(t, expected) || t.equalsIgnoreCase(expected.trim());
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForEncounterFormDropdownValue(
            WebDriverWait wait, String widgetBaseId, String hiddenSelectId, String expected) {
        wait.until(d -> encounterFormDropdownShowsValue(widgetBaseId, hiddenSelectId, expected));
    }

    private void waitForSelectOneMenuPanelClosed(String panelDomId) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(d -> !isSelectOneMenuPanelVisible(panelDomId));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // panel may already be gone from DOM
        }
    }

    /** PrimeFaces hides the native {@code select}; set value via JS and fire change. */
    private void selectEncounterFormDropdownViaJsNativeSelect(String hiddenSelectId, String visibleText) {
        WebElement select = driver.findElement(By.id(hiddenSelectId));
        scrollToElement(select);
        String wanted = visibleText.trim();
        Boolean matched = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var sel = arguments[0];"
                        + "var want = arguments[1].toLowerCase().replace(/\\s+/g, ' ').trim();"
                        + "for (var i = 0; i < sel.options.length; i++) {"
                        + "  var opt = sel.options[i];"
                        + "  var text = (opt.text || '').replace(/\\s+/g, ' ').trim().toLowerCase();"
                        + "  if (text === want || text.indexOf(want) >= 0 || want.indexOf(text) >= 0) {"
                        + "    sel.value = opt.value;"
                        + "    sel.selectedIndex = i;"
                        + "    sel.dispatchEvent(new Event('change', {bubbles: true}));"
                        + "    return true;"
                        + "  }"
                        + "}"
                        + "return false;",
                select,
                wanted);
        if (matched == null || !matched) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "No option matching '" + visibleText + "' in select " + hiddenSelectId);
        }
    }

    private void dismissVisibleAutocompletePanelsIfPresent() {
        By panelBy = By.xpath("//*[contains(@class,'ui-autocomplete-panel')]");
        boolean dismissed = false;
        for (WebElement panel : driver.findElements(panelBy)) {
            try {
                if (panel.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("document.body.click();");
                    dismissed = true;
                    break;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next panel
            }
        }
        if (dismissed) {
            sleepQuietMs(150);
        }
    }

    private void dismissVisibleAutocompletePanels() {
        By panelBy = By.xpath("//*[contains(@class,'ui-autocomplete-panel')]");
        for (WebElement panel : driver.findElements(panelBy)) {
            try {
                if (panel.isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("document.body.click();");
                    break;
                }
            } catch (StaleElementReferenceException ignored) {
                // retry
            }
        }
    }

    private void selectEncounterSelectOneWithRetry(WebDriverWait wait, String widgetId, String labelContains, String optionMatch) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                doSelectEncounterSelectOne(wait, widgetId, labelContains, optionMatch);
                return;
            } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException
                    | org.openqa.selenium.TimeoutException e) {
                System.out.println("Retry selectOne for " + labelContains + " (attempt " + (attempt + 1) + ")...");
                dismissVisibleAutocompletePanels();
            }
        }
    }

    private void doSelectEncounterSelectOne(WebDriverWait wait, String widgetId, String labelContains, String optionMatch) {
        dismissVisibleAutocompletePanels();
        if (widgetId != null && !widgetId.isBlank()) {
            List<WebElement> widgets = driver.findElements(By.id(widgetId));
            for (WebElement widget : widgets) {
                try {
                    if (widget.isDisplayed()) {
                        scrollToElement(widget);
                        selectSelectOneMenuOption(widgetId, optionMatch);
                        return;
                    }
                } catch (Exception ignored) {
                    // try label fallback
                }
            }
            if (!widgets.isEmpty()) {
                try {
                    WebElement widget = new WebDriverWait(driver, Duration.ofSeconds(3))
                            .until(ExpectedConditions.visibilityOfElementLocated(By.id(widgetId)));
                    scrollToElement(widget);
                    selectSelectOneMenuOption(widgetId, optionMatch);
                    return;
                } catch (org.openqa.selenium.TimeoutException ignored) {
                    // resolve widget near label text inside Encounter block
                }
            }
        }
        selectEncounterSelectOneNearLabel(wait, labelContains, optionMatch);
    }

    private WebElement findEncounterVisibleInputWithFallback(WebDriverWait wait, By preferredBy, String labelContains) {
        List<WebElement> preferred = driver.findElements(preferredBy);
        for (WebElement el : preferred) {
            try {
                if (el.isDisplayed()) {
                    return el;
                }
            } catch (StaleElementReferenceException ignored) {
                // fallback below
            }
        }
        String safe = labelContains.replace("\"", "");
        return wait.until(d -> {
            List<By> candidates = List.of(
                    By.xpath(ENCOUNTER_CONTAINER + "//tr[.//*[contains(normalize-space(.), \"" + safe
                            + "\")]]//input[not(@type='hidden') and (@type='text' or @type='search' or not(@type))][1]"),
                    By.xpath(ENCOUNTER_CONTAINER + "//*[contains(normalize-space(.), \"" + safe
                            + "\")]/following::input[not(@type='hidden')][1]"));
            for (By by : candidates) {
                List<WebElement> found = d.findElements(by);
                for (WebElement el : found) {
                    try {
                        if (el.isDisplayed()) {
                            return el;
                        }
                    } catch (StaleElementReferenceException ignored) {
                        // next
                    }
                }
            }
            return null;
        });
    }

    private void encounterAutocompleteNearLabel(WebDriverWait wait, String labelContains, String typeText) {
        By preferredBy;
        if ("Location".equalsIgnoreCase(labelContains)) {
            preferredBy = encounterLocationInput;
        } else if ("Department".equalsIgnoreCase(labelContains)) {
            preferredBy = encounterDepartmentInput;
        } else {
            preferredBy = encounterAttendingClinicianInput;
        }
        WebElement input = findEncounterVisibleInputWithFallback(wait, preferredBy, labelContains);
        scrollToElement(input);
        // Re-find if stale (page DOM updates after autocomplete selections)
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                input.click();
                break;
            } catch (StaleElementReferenceException e) {
                input = findEncounterVisibleInputWithFallback(wait, preferredBy, labelContains);
                scrollToElement(input);
            }
        }
        input.clear();
        input.sendKeys(typeText);
        // Wait up to 15s for autocomplete panel to appear
        // Use * to match both <div> and <span> (PrimeFaces can render as either)
        By panelBy = By.xpath("//*[contains(@class,'ui-autocomplete-panel')]");
        WebElement panel = wait.withTimeout(Duration.ofSeconds(15))
                .until(d -> {
                    List<WebElement> panels = d.findElements(panelBy);
                    for (WebElement p : panels) {
                        try {
                            if (p.isDisplayed() && p.isEnabled()) return p;
                        } catch (StaleElementReferenceException ignored) {}
                    }
                    return null;
                });
        // Find the <tr> row directly by data-item-label attribute
        String trimmed = typeText.trim();
        By rowBy = By.xpath(
                ".//tr[@data-item-label='" + trimmed + "' and @role='option']");
        WebElement row = wait.withTimeout(Duration.ofSeconds(5))
                .until(d -> {
                    List<WebElement> rows = panel.findElements(rowBy);
                    for (WebElement r : rows) {
                        try {
                            if (r.isDisplayed()) return r;
                        } catch (StaleElementReferenceException ignored) {}
                    }
                    return null;
                });
        row.click();
        sleepQuietMs(200);
    }

    private void selectEncounterSelectOneByKnownWidgetOrLabel(
            WebDriverWait wait, String widgetId, String labelContains, String optionMatch) {
        List<WebElement> widgets = driver.findElements(By.id(widgetId));
        for (WebElement widget : widgets) {
            try {
                if (widget.isDisplayed()) {
                    selectSelectOneMenuOption(widgetId, optionMatch);
                    return;
                }
            } catch (Exception ignored) {
                // fallback below
            }
        }
        selectEncounterSelectOneNearLabel(wait, labelContains, optionMatch);
    }

    private void selectEncounterSelectOneNearLabel(WebDriverWait wait, String labelContains, String optionMatch) {
        String widgetId = findEncounterSelectOneWidgetIdNearLabel(wait, labelContains);
        if (widgetId == null || widgetId.isBlank()) {
            throw new org.openqa.selenium.NoSuchElementException(
                    "Encounter: no p:selectOneMenu with @id found near label containing: " + labelContains);
        }
        selectSelectOneMenuOption(widgetId, optionMatch);
    }

    private String findEncounterSelectOneWidgetIdNearLabel(WebDriverWait wait, String labelContains) {
        return wait.until(d -> {
            List<WebElement> menus = d.findElements(By.xpath(
                    ENCOUNTER_CONTAINER + "//div[contains(@class,'ui-selectonemenu') and @id]"));
            for (WebElement menu : menus) {
                try {
                    if (!menu.isDisplayed()) {
                        continue;
                    }
                    String rowText;
                    try {
                        rowText = menu.findElement(By.xpath("./ancestor::tr[1]")).getText();
                    } catch (org.openqa.selenium.NoSuchElementException e1) {
                        try {
                            rowText = menu.findElement(By.xpath("./ancestor::div[contains(@class,'ui-g')][1]"))
                                    .getText();
                        } catch (org.openqa.selenium.NoSuchElementException e2) {
                            rowText = menu.findElement(By.xpath("./ancestor::div[position()<=5]")).getText();
                        }
                    }
                    if (rowText != null && rowText.contains(labelContains)) {
                        return menu.getAttribute("id");
                    }
                } catch (StaleElementReferenceException | org.openqa.selenium.NoSuchElementException ignored) {
                    // continue
                }
            }
            return null;
        });
    }

    public void enterLocation() {
        WebElement locationField = driver.findElement(locationInputField);
        locationField.click(); // optional, to ensure it's active
        locationField.sendKeys("Family Medicine Clinic");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locationDropdownOption));
        driver.findElement(locationDropdownOption).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterAndSelectDepartment() {
        WebElement departmentField = driver.findElement(departmentInputField);
        departmentField.click();
        departmentField.sendKeys("Family Medicine");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(departmentDropdownOption));
        driver.findElement(departmentDropdownOption).click();
    }
    public void enterAndSelectAttendingClinician() {
        WebElement clinicianField = driver.findElement(attendingClinicianInputField);
        clinicianField.click();
        clinicianField.sendKeys("DHA-P-27337208");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(attendingClinicianDropdownOption));
        driver.findElement(attendingClinicianDropdownOption).click();
    }

    public void enterAndSelectOrderingClinician() {
        WebElement orderingField = driver.findElement(orderingClinicianInputField);
        orderingField.click();
        orderingField.sendKeys("DHA-P-27337208");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderingClinicianDropdownOption));
        driver.findElement(orderingClinicianDropdownOption).click();

    }

    public void selectStartTypeElective() {
        WebElement startTypeField = driver.findElement(startTypeDropdown);
        startTypeField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(startTypeElectiveOption));
        driver.findElement(startTypeElectiveOption).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectEndTypeDischarged() {
        WebElement endTypeField = driver.findElement(endTypeDropdown);
        endTypeField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(endTypeDischargedOption));
        driver.findElement(endTypeDischargedOption).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectVisitTypeNew() {
        WebElement visitTypeField = driver.findElement(visitTypeDropdown);
        visitTypeField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(visitTypeNewOption));
        driver.findElement(visitTypeNewOption).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterEndDate(String endDate) {
        WebElement endDateField = driver.findElement(endDateInput);
        endDateField.sendKeys(endDate);
    }

    public void clickDiagnosisAndInterventions() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(diagnosisAndInterventionsLink));
        scrollToElement(link);
        link.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(activityCodeInput));
    }

    private static By activityListDataBody() {
        return By.id("AccumedHaadActivityListForm:datalist_data");
    }

    private static By activityRowByIndex(int rowIndex) {
        return By.xpath("//tbody[@id='AccumedHaadActivityListForm:datalist_data']"
                + "//tr[@data-ri='" + rowIndex + "']");
    }

    private static By activityRowActionsMenuButton(int rowIndex) {
        return By.xpath("//tbody[@id='AccumedHaadActivityListForm:datalist_data']"
                + "//tr[@data-ri='" + rowIndex + "']//span[contains(@class,'ui-menubutton')]//button");
    }

    private static String activityMenuActionIdSuffix(String menuItemText) {
        return switch (menuItemText.trim().toLowerCase()) {
            case "edit" -> "editButton";
            case "exclude" -> "excludeButton";
            case "delete" -> "deleteButton";
            default -> menuItemText.trim();
        };
    }

    private static By activityRowMenuActionLink(int rowIndex, String menuItemText) {
        String suffix = activityMenuActionIdSuffix(menuItemText);
        return By.id("AccumedHaadActivityListForm:datalist:" + rowIndex + ":" + suffix);
    }

    private static By activityMenuItemLink(String menuItemText) {
        String label = menuItemText.trim().replace("\\", "\\\\").replace("'", "\\'");
        return By.xpath("//a[contains(@class,'ui-menuitem-link')]"
                + "[.//span[contains(@class,'ui-menuitem-text') and normalize-space()=\"" + label + "\"]]");
    }

    /**
     * Opens row actions (⋮) then selects a menu item (Edit, Exclude, Delete, …).
     */
    public void editActivityInDiagnosisTab(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        clickDiagnosisAndInterventions();
        int rowIndex = 0;
        String rowStr = mapGet(fields, "Row Index");
        if (rowStr != null && !rowStr.isBlank()) {
            rowIndex = Integer.parseInt(rowStr.trim());
        }
        String menuItem = mapGet(fields, "Menu Item");
        if (menuItem == null || menuItem.isBlank()) {
            menuItem = "Edit";
        }
        clickActivityRowActionsMenu(wait, rowIndex);
        clickActivityMenuItem(wait, rowIndex, menuItem.trim());
        fillEditActivityDetails(wait, fields);
    }

    public void clickActivityRowActionsMenu(WebDriverWait wait, int rowIndex) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(activityListDataBody()));
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(activityRowByIndex(rowIndex)));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'end'});", row);
        sleepQuietMs(300);
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(activityRowActionsMenuButton(rowIndex)));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.until(d -> isActivityRowMenuVisible(rowIndex));
        sleepQuietMs(300);
        System.out.println("Activity: opened actions menu for row " + rowIndex + ".");
    }

    private boolean isActivityRowMenuVisible(int rowIndex) {
        By editInRow = activityRowMenuActionLink(rowIndex, "Edit");
        for (WebElement link : driver.findElements(editInRow)) {
            try {
                if (link.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        for (WebElement menu : driver.findElements(By.xpath(
                "//div[contains(@class,'ui-menu') and contains(@class,'ui-menubutton')]"
                        + "[not(contains(@style,'display: none'))]"))) {
            try {
                if (menu.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        return false;
    }

    public void clickActivityMenuItem(WebDriverWait wait, int rowIndex, String menuItemText) {
        By idBy = activityRowMenuActionLink(rowIndex, menuItemText);
        WebElement item = wait.until(d -> {
            for (WebElement link : d.findElements(idBy)) {
                try {
                    if (link.isDisplayed()) {
                        return link;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // next
                }
            }
            By itemBy = activityMenuItemLink(menuItemText);
            for (WebElement link : d.findElements(itemBy)) {
                try {
                    if (link.isDisplayed()) {
                        return link;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // next
                }
            }
            return null;
        });
        scrollToElement(item);
        try {
            item.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", item);
        }
        System.out.println("Activity: clicked menu item '" + menuItemText + "'.");
    }

    /** Edit-mode fields: Activity Type, Code, Date, Order Status (same widgets as add-service row). */
    public void fillEditActivityDetails(WebDriverWait wait, Map<String, String> fields) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id(ACTIVITY_ORDER_STATUS_EDIT_WIDGET)),
                ExpectedConditions.visibilityOfElementLocated(activityCodeInput)));
        fillActivityServiceFields(wait, fields);
        String orderStatus = mapGet(fields, "Order Status");
        if (orderStatus != null && !orderStatus.isBlank()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ACTIVITY_ORDER_STATUS_EDIT_WIDGET)));
            selectSelectOneMenuOption(ACTIVITY_ORDER_STATUS_EDIT_WIDGET, orderStatus.trim());
            System.out.println("Activity edit: Order Status selected -> " + orderStatus.trim());
        }
    }

    /**
     * Opens row actions (⋮) → Edit on the current Diagnosis tab (no tab navigation).
     */
    public void editActivityInCurrentDiagnosisTab(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activityListDataBody()));
        int rowIndex = 0;
        String rowStr = mapGet(fields, "Row Index");
        if (rowStr != null && !rowStr.isBlank()) {
            rowIndex = Integer.parseInt(rowStr.trim());
        }
        String menuItem = mapGet(fields, "Menu Item");
        if (menuItem == null || menuItem.isBlank()) {
            menuItem = "Edit";
        }
        clickActivityRowActionsMenu(wait, rowIndex);
        clickActivityMenuItem(wait, rowIndex, menuItem.trim());
        fillEditActivityDetails(wait, fields);
    }

    /**
     * Medical Services row on Diagnosis and Interventions — keys: Activity Type, Code, Date (case-insensitive).
     * Order Status is filled in edit mode via {@link #fillEditActivityDetails}.
     */
    public void fillMandatoryAddServiceDetails(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(activityCodeInput));
        scrollToElement(driver.findElement(activityCodeInput));
        fillActivityServiceFields(wait, fields);
    }

    private void fillActivityServiceFields(WebDriverWait wait, Map<String, String> fields) {
        String activityType = mapGet(fields, "Activity Type");
        if (activityType != null && !activityType.isBlank()) {
            selectEncounterFormDropdown(wait, ACTIVITY_TYPE_WIDGET, ACTIVITY_TYPE_SELECT_ID, activityType.trim());
            wait.until(d -> {
                try {
                    WebElement codeInput = d.findElement(activityCodeInput);
                    return codeInput.isDisplayed() && codeInput.isEnabled();
                } catch (Exception e) {
                    return false;
                }
            });
            System.out.println("Activity: Activity Type selected -> " + activityType.trim());
        }
        String code = mapGet(fields, "Code");
        if (code != null && !code.isBlank()) {
            activityCodeAutocompleteWithRetry(wait, code.trim());
        }
        String date = mapGet(fields, "Date");
        if (date != null && !date.isBlank()) {
            WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(activityStartDateInput));
            setEncounterTextInputWithRetry(dateInput, date.trim());
            System.out.println("Activity: Date filled -> " + date.trim());
        }
    }

    private void activityCodeAutocompleteWithRetry(WebDriverWait wait, String typeText) {
        org.openqa.selenium.TimeoutException lastTimeout = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                doActivityCodeAutocomplete(wait, typeText);
                return;
            } catch (StaleElementReferenceException
                    | org.openqa.selenium.TimeoutException
                    | ElementNotInteractableException e) {
                lastTimeout = e instanceof org.openqa.selenium.TimeoutException
                        ? (org.openqa.selenium.TimeoutException) e : lastTimeout;
                System.out.println("Retry Activity Code autocomplete (attempt " + (attempt + 1) + ")...");
                dismissVisibleAutocompletePanelsIfPresent();
            }
        }
        if (lastTimeout != null) {
            throw lastTimeout;
        }
    }

    private void doActivityCodeAutocomplete(WebDriverWait wait, String typeText) {
        dismissVisibleAutocompletePanels();
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(activityCodeInput));
        scrollToElement(input);
        try {
            input.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            input = wait.until(ExpectedConditions.elementToBeClickable(activityCodeInput));
            scrollToElement(input);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
        }
        try {
            input.clear();
        } catch (InvalidElementStateException | StaleElementReferenceException e) {
            input = wait.until(ExpectedConditions.elementToBeClickable(activityCodeInput));
            scrollToElement(input);
        }
        String searchText = typeText;
        if (typeText.contains("|")) {
            searchText = typeText.split("\\|", 2)[0].trim();
        }
        try {
            input.sendKeys(searchText);
        } catch (ElementNotInteractableException e) {
            input = wait.until(ExpectedConditions.presenceOfElementLocated(activityCodeInput));
            scrollToElement(input);
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].removeAttribute('readonly'); arguments[0].focus();", input);
            input.sendKeys(searchText);
        }
        By panelBy = By.xpath("//*[contains(@class,'ui-autocomplete-panel')]");
        WebDriverWait rowWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement row = rowWait.until(d -> {
            List<WebElement> panels = d.findElements(panelBy);
            for (int i = panels.size() - 1; i >= 0; i--) {
                WebElement panel = panels.get(i);
                try {
                    if (!panel.isDisplayed()) {
                        continue;
                    }
                    WebElement match = findMatchingAutocompleteRowInPanel(panel, typeText);
                    if (match != null) {
                        return match;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // panel rerendered
                }
            }
            return null;
        });
        clickListOption(row);
        dismissVisibleAutocompletePanels();
        System.out.println("Add Service: Code selected.");
    }

    /** Shared autocomplete matcher for encounter clinicians and activity code rows. */
    private WebElement findMatchingAutocompleteRowInPanel(WebElement panel, String typeText) {
        List<WebElement> rows = panel.findElements(By.xpath(".//tr[@role='option']"));
        if (rows.isEmpty()) {
            rows = panel.findElements(By.xpath(".//tr[contains(@class,'ui-autocomplete-row')]"));
        }
        if (rows.isEmpty()) {
            rows = panel.findElements(By.xpath(".//tbody/tr"));
        }
        if (rows.isEmpty()) {
            rows = panel.findElements(By.xpath(".//li[@role='option']"));
        }
        for (WebElement row : rows) {
            try {
                if (!row.isDisplayed()) {
                    continue;
                }
                String dataLabel = row.getAttribute("data-item-label");
                if (dataLabel != null && optionMatchesDropdownValue(dataLabel, typeText)) {
                    return row;
                }
                String rowText = row.getText();
                if (rowText != null && optionMatchesDropdownValue(rowText, typeText)) {
                    return row;
                }
                for (WebElement cell : row.findElements(By.xpath(".//td"))) {
                    try {
                        if (!cell.isDisplayed()) {
                            continue;
                        }
                        String cellText = cell.getText();
                        if (cellText != null && optionMatchesDropdownValue(cellText, typeText)) {
                            return row;
                        }
                    } catch (StaleElementReferenceException ignored) {
                        // next cell
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // next row
            }
        }
        return null;
    }

    public boolean isAddBulkDiagnosisButtonDisplayed() {
        return driver.findElement(addBulkDiagnosisButton).isDisplayed();
    }

    public void selectActivityTypeCPT() {
        WebElement activityTypeField = driver.findElement(activityTypeDropdown);
        activityTypeField.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cptOption));
        driver.findElement(cptOption).click();
    }

    public void enterCode70030() {
        WebElement codeFieldElement = driver.findElement(codeField);
        codeFieldElement.click();
        codeFieldElement.sendKeys("70030");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(code70030Option));
        driver.findElement(code70030Option).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickAddServiceButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addServiceButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    private static String normalizeServiceCodeKey(String serviceCode) {
        String code = serviceCode.trim();
        int pipe = code.indexOf('|');
        if (pipe >= 0) {
            code = code.substring(0, pipe).trim();
        }
        return code;
    }

    private static By serviceCodeInActivityDataListLabel(String serviceCode) {
        String code = normalizeServiceCodeKey(serviceCode);
        return By.xpath("//label[contains(@id,'AccumedHaadActivityListForm:datalist')]"
                + "[contains(normalize-space(.), " + xpathLiteral(code) + ")]");
    }

    private static By growlTitleLocator(String message) {
        return By.xpath("//span[contains(@class,'ui-growl-title')][normalize-space()=" + xpathLiteral(message.trim()) + "]");
    }

    /** Safe XPath string literal for arbitrary message/code text. */
    private static String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        StringBuilder parts = new StringBuilder("concat(");
        String remaining = value;
        boolean first = true;
        while (!remaining.isEmpty()) {
            int quote = remaining.indexOf('\'');
            String segment = quote >= 0 ? remaining.substring(0, quote) : remaining;
            if (!segment.isEmpty()) {
                if (!first) {
                    parts.append(", ");
                }
                parts.append("'").append(segment).append("'");
                first = false;
            }
            if (quote >= 0) {
                if (!first) {
                    parts.append(", ");
                }
                parts.append("\"'\"");
                first = false;
                remaining = remaining.substring(quote + 1);
            } else {
                remaining = "";
            }
        }
        parts.append(")");
        return parts.toString();
    }

    public void waitForServiceCodeInActivityList(String serviceCode) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(serviceCodeInActivityDataListLabel(serviceCode)));
    }

    public boolean isServiceCodeDisplayedInActivityList(String serviceCode) {
        return driver.findElement(serviceCodeInActivityDataListLabel(serviceCode)).isDisplayed();
    }

    public void clickUpdateVisitButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(updateVisitButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    private static By buttonContainingText(String buttonText) {
        return By.xpath("//button[contains(normalize-space(.), " + xpathLiteral(buttonText.trim()) + ")]");
    }

    /** Clicks any button whose visible text contains the given label (e.g. {@code Insert Visit}). */
    public void clickButtonContainingText(String buttonText) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(buttonContainingText(buttonText)));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void clickMarkAsReadyToBillButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(markAsReadyToBillButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(60)));
        System.out.println("Clicked Mark As Ready To Bill.");
    }

    /** Waits for AJAX loader / status dialog overlay to disappear after Mark As Ready To Bill. */
    private void waitForBlockingOverlaysToClose(WebDriverWait wait) {
        try {
            wait.until(d -> !isBlockingOverlayVisible());
        } catch (org.openqa.selenium.TimeoutException e) {
            dismissBlockingOverlays();
        }
        sleepQuietMs(500);
    }

    private boolean isBlockingOverlayVisible() {
        By overlayBy = By.cssSelector(
                "#statusDialog_modal, .ui-widget-overlay.ui-dialog-mask, .ui-blockui, .ui-blockui-document");
        for (WebElement overlay : driver.findElements(overlayBy)) {
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

    private void dismissBlockingOverlays() {
        for (WebElement ok : driver.findElements(By.xpath(
                "//div[contains(@id,'statusDialog')]//button[.//span[normalize-space()='OK']]"
                        + " | //button[contains(@onclick,'statusDialog')][.//span[normalize-space()='OK']]"))) {
            try {
                if (ok.isDisplayed()) {
                    ok.click();
                    sleepQuietMs(500);
                    return;
                }
            } catch (Exception ignored) {
                // try next
            }
        }
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(By.id("statusDialog_modal")));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('#statusDialog_modal,.ui-widget-overlay.ui-dialog-mask')"
                            + ".forEach(function(el){el.style.display='none';});");
        }
    }

    private void clickElementWithJsFallback(WebElement element) {
        scrollToElement(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void navigateToBillManagementPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        closeVisitDialogIfOpen();
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
        WebElement billing = wait.until(ExpectedConditions.presenceOfElementLocated(billingMenuLink));
        clickElementWithJsFallback(billing);
        System.out.println("Clicked Billing menu.");
        WebElement billMgmt = wait.until(ExpectedConditions.presenceOfElementLocated(billManagementMenuLink));
        clickElementWithJsFallback(billMgmt);
        System.out.println("Clicked Bill Management.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
    }

    public void clickCreatedPatientInBillManagementList() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));
        String fullName = (randomFirstName + " " + randomLastName).trim();
        String mrn = lastRegisteredMrn;
        if (mrn != null && !mrn.isBlank()) {
            searchVisitInPatientAccess(mrn);
        }
        WebElement link = wait.until(d -> findCreatedPatientLinkInWorklist(d, mrn, fullName));
        scrollToElement(link);
        try {
            link.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(60)));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(encounterSection),
                ExpectedConditions.visibilityOfElementLocated(patientAndEncounterScreen),
                ExpectedConditions.presenceOfElementLocated(generateBillButton)));
        System.out.println("Clicked patient in Bill Management: " + fullName
                + (mrn != null && !mrn.isBlank() ? " (MRN: " + mrn + ")" : ""));
    }

    private WebElement findCreatedPatientLinkInWorklist(WebDriver driver, String mrn, String fullName) {
        String upperName = fullName.toUpperCase();
        if (mrn != null && !mrn.isBlank()) {
            for (WebElement link : driver.findElements(By.xpath(
                    "//tbody[@id='phWLForm:phWLTbl_data']//tr[.//span[contains(@id,'mrnCol')]"
                            + "[normalize-space()=" + xpathLiteral(mrn.trim()) + "]]//a"))) {
                if (isDisplayedAndEnabled(link)) {
                    return link;
                }
            }
        }
        By[] namePatterns = {
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patientNameLink')]"
                        + "[contains(translate(normalize-space(.),"
                        + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),"
                        + xpathLiteral(upperName) + ")]"),
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//a[contains(@id,'patient_FullNameCol')]"
                        + "[contains(translate(normalize-space(.),"
                        + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),"
                        + xpathLiteral(upperName) + ")]"),
                By.xpath("//a[contains(@id,'patientNameLink') or contains(@id,'patient_FullNameCol')]"
                        + "[contains(translate(normalize-space(.),"
                        + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),"
                        + xpathLiteral(upperName) + ")]")
        };
        for (By pattern : namePatterns) {
            for (WebElement link : driver.findElements(pattern)) {
                if (isDisplayedAndEnabled(link)) {
                    return link;
                }
            }
        }
        return null;
    }

    private boolean isDisplayedAndEnabled(WebElement element) {
        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public void clickGenerateBillButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitForBlockingOverlaysToClose(wait);
        WebElement btn = wait.until(d -> {
            for (WebElement candidate : d.findElements(generateBillButton)) {
                if (isDisplayedAndEnabled(candidate)) {
                    return candidate;
                }
            }
            return null;
        });
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        System.out.println("Clicked Generate Bill.");
    }

    public void clickAlertDialogOkButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement ok = wait.until(ExpectedConditions.elementToBeClickable(alertDialogOkButton));
        scrollToElement(ok);
        try {
            ok.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ok);
        }
        System.out.println("Clicked alert OK.");
    }

    public void selectAllCategorizedBills() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(selectAllCategorizedBillsCheckbox));
        scrollToElement(box);
        try {
            box.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", box);
        }
        System.out.println("Selected all categorized bills.");
    }

    public void clickAddPaymentButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(addPaymentButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        System.out.println("Clicked Add Payment.");
    }

    public void clickPaymentSaveButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(paymentSaveButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        System.out.println("Clicked payment Save.");
    }

    public void verifyGrowlSuccessMessage(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(growlTitleLocator(expectedMessage)));
    }

    /** Waits until a visible growl success message contains {@code partialMessage}. */
    public void verifyGrowlSuccessMessageContaining(String partialMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String expected = normalizeMessageText(partialMessage).toLowerCase();
        wait.until(d -> {
            for (WebElement element : d.findElements(By.cssSelector("span.ui-growl-title"))) {
                try {
                    if (element.isDisplayed()) {
                        String text = normalizeMessageText(element.getText()).toLowerCase();
                        if (text.contains(expected)) {
                            System.out.println("Verified growl success message: " + element.getText().trim());
                            return true;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
            return false;
        });
    }

    /** Waits until a visible inline or growl error contains {@code expectedMessage}. */
    public void verifyErrorMessageDisplayed(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        String expected = normalizeMessageText(expectedMessage);
        wait.until(d -> {
            for (String actual : collectVisibleErrorMessages()) {
                if (normalizeMessageText(actual).contains(expected)) {
                    System.out.println("Verified error message: " + actual);
                    return true;
                }
            }
            return false;
        });
    }

    private List<String> collectVisibleErrorMessages() {
        List<String> messages = new ArrayList<>();
        By[] locators = {
                By.cssSelector("div.ui-message-error-detail"),
                By.cssSelector("div.ui-message-error p"),
                By.cssSelector("span.ui-growl-title"),
                By.cssSelector("div.ui-messages-error span"),
                By.cssSelector("span.ui-message-error-detail")
        };
        for (By locator : locators) {
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (element.isDisplayed()) {
                        String text = element.getText().trim();
                        if (!text.isEmpty()) {
                            messages.add(text);
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next element
                }
            }
        }
        return messages;
    }

    private static String normalizeMessageText(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\u2018', '\'')
                .replace('\u2019', '\'')
                .replaceAll("\\s+", " ")
                .trim();
    }

    public void clickOkButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(okButton));
        driver.findElement(okButton).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void navigateToBillingOpReceiptsPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(billingLink));
        driver.findElement(billingLink).click();
        System.out.println("Clicked on 'Billing' link.");

        wait.until(ExpectedConditions.elementToBeClickable(opReceiptsLink));
        driver.findElement(opReceiptsLink).click();
        System.out.println("Clicked on 'OP Receipts' link.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clickCreatedVisit() {
        String createdVisitXpath = "//a[text()='" + randomFirstName + " " + randomLastName + "']";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(createdVisitXpath)));
        driver.findElement(By.xpath(createdVisitXpath)).click();
        System.out.println("Clicked on created visit: " + randomFirstName + " " + randomLastName);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** Closes the open visit invoice dialog when present (returns to Checked-in Visits list). */
    public void closeVisitDialogIfOpen() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> dialogs = driver.findElements(By.id("InvoiceDlg"));
        if (dialogs.isEmpty() || !dialogs.get(0).isDisplayed()) {
            return;
        }
        try {
            WebElement close = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#InvoiceDlg .ui-dialog-titlebar-close")));
            scrollToElement(close);
            close.click();
        } catch (org.openqa.selenium.TimeoutException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "if (typeof PF !== 'undefined' && PF('NewInvoiceWid')) { PF('NewInvoiceWid').hide(); }");
        }
        confirmUnsavedVisitChangesDialogIfPresent();
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
        System.out.println("Closed visit dialog.");
    }

    /** Closes the encounter/visit dialog and discards unsaved visit changes (no visit persisted). */
    public void closeEncounterTabWithoutSavingVisit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> dialogs = driver.findElements(By.id("InvoiceDlg"));
        if (dialogs.isEmpty() || !dialogs.get(0).isDisplayed()) {
            returnToPatientAccessVisitList();
            System.out.println("Encounter tab already closed — returned to visit list.");
            return;
        }
        try {
            WebElement close = wait.until(ExpectedConditions.elementToBeClickable(
                    By.cssSelector("#InvoiceDlg .ui-dialog-titlebar-close")));
            scrollToElement(close);
            close.click();
        } catch (org.openqa.selenium.TimeoutException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "if (typeof PF !== 'undefined' && PF('NewInvoiceWid')) { PF('NewInvoiceWid').hide(); }");
        }
        dismissUnsavedVisitChangesDialogIfPresent();
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
        } catch (org.openqa.selenium.TimeoutException e) {
            clickPatientAccess();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
        }
        System.out.println("Closed encounter tab without saving the visit.");
    }

    private void dismissUnsavedVisitChangesDialogIfPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            WebElement noBtn = wait.until(d -> findVisiblePrimeFacesConfirmDialogButton(d, "No"));
            scrollToElement(noBtn);
            try {
                noBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noBtn);
            }
            waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
            System.out.println("Discarded unsaved visit changes.");
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // no confirmation shown
        }
    }

    public boolean isVisitAbsentFromPatientAccessList(String mrn) {
        if (mrn == null || mrn.isBlank()) {
            System.out.println("Visit absence check failed — MRN is blank.");
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            boolean absent = Boolean.TRUE.equals(wait.until(d -> {
                for (WebElement row : d.findElements(patientAccessRowByMrn(mrn))) {
                    try {
                        if (row.isDisplayed()) {
                            return false;
                        }
                    } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                        // re-check on next poll
                    }
                }
                return true;
            }));
            System.out.println("Visit for MRN " + mrn + " absent from Patient Access list: " + absent);
            return absent;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Visit for MRN " + mrn + " still present in Patient Access list.");
            return false;
        }
    }

    private void confirmUnsavedVisitChangesDialogIfPresent() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            WebElement yesBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//form[contains(@id,'mainForm1')]//button[.//span[normalize-space()='Yes']]"
                            + " | //div[contains(@id,'confirmDialog')]//button[.//span[normalize-space()='Yes']]")));
            scrollToElement(yesBtn);
            try {
                yesBtn.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yesBtn);
            }
            waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
            System.out.println("Saved visit changes before returning to list.");
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // no confirmation shown
        }
    }

    /** Ensures Patient Access Checked-in Visits table is visible. */
    public void returnToPatientAccessVisitList() {
        closeVisitDialogIfOpen();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
        } catch (org.openqa.selenium.TimeoutException e) {
            clickPatientAccess();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
        }
        System.out.println("Returned to Patient Access visit list.");
    }

    public void searchVisitInPatientAccess(String searchTerm) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("phWLForm:search")));
        scrollToElement(searchInput);
        searchInput.clear();
        searchInput.sendKeys(searchTerm);
        WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("phWLForm:search-BTN")));
        scrollToElement(searchBtn);
        try {
            searchBtn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchBtn);
        }
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
        System.out.println("Searched Patient Access visits for: " + searchTerm);
    }

    private static By patientAccessRowByMrn(String mrn) {
        return By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//tr[.//span[contains(@id,'mrnCol')"
                + " and normalize-space()=" + xpathLiteral(mrn.trim()) + "]]");
    }

    public boolean isVisitReadyToBillInPatientAccessList(String mrn) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            return wait.until(d -> {
                try {
                    WebElement row = d.findElement(patientAccessRowByMrn(mrn));
                    WebElement billStatus = row.findElement(By.xpath(".//span[contains(@id,'billing_Status')]"));
                    String status = billStatus.getText().trim();
                    System.out.println("Bill status for MRN " + mrn + ": " + status);
                    return status.equalsIgnoreCase("Ready to Bill") || status.equalsIgnoreCase("Ready To Bill");
                } catch (Exception e) {
                    return false;
                }
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Visit for MRN " + mrn + " did not show Ready to Bill in Patient Access list.");
            return false;
        }
    }

    /** Opens a visit from Checked-in Visits by MRN (clicks patient name link). */
    public void openVisitFromPatientAccessListByMrn(String mrn) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        By rowBy = patientAccessRowByMrn(mrn);
        wait.until(ExpectedConditions.presenceOfElementLocated(rowBy));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//tbody[@id='phWLForm:phWLTbl_data']//tr[.//span[contains(@id,'mrnCol')"
                        + " and normalize-space()=" + xpathLiteral(mrn.trim()) + "]]"
                        + "//a[contains(@id,'patientNameLink')]")));
        scrollToElement(link);
        try {
            link.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(60)));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(encounterSection),
                ExpectedConditions.visibilityOfElementLocated(patientAndEncounterScreen)));
        System.out.println("Opened visit from Patient Access list for MRN: " + mrn);
    }

    private static final By firstReadyToBillVisitLink = By.xpath(
            "//tbody[@id='phWLForm:phWLTbl_data']//tr[.//span[contains(@id,'billing_Status')]"
                    + "[contains(translate(normalize-space(.),"
                    + "'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'READY TO BILL')]]"
                    + "//a[contains(@id,'patientNameLink')]");

    /** Opens the first visit with Bill Status Ready to Bill from Checked-in Visits. */
    public void openFirstReadyToBillVisitFromPatientAccess() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("phWLForm:phWLTbl_data")));
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(firstReadyToBillVisitLink));
        scrollToElement(link);
        try {
            link.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        }
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(60)));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(encounterSection),
                ExpectedConditions.visibilityOfElementLocated(patientAndEncounterScreen)));
        System.out.println("Opened first Ready to Bill visit from Patient Access list.");
    }

    private static final By excludeClaimToolbarButton = By.xpath(
            "//form[contains(@id,'actionsFormInvoice')]//button[.//span[normalize-space()='Exclude Claim']]"
                    + " | //button[contains(@onclick,'ExcludeClaimWid')][.//span[normalize-space()='Exclude Claim']]"
                    + " | //button[contains(@data-pfconfirmcommand,'ExcludeClaimForm')]"
                    + "[.//span[normalize-space()='Exclude Claim']]");

    public void clickExcludeClaimToolbarButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(excludeClaimToolbarButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        try {
            wait.until(this::findVisibleConfirmDialogYesButton);
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // confirm dialog may appear on the next step
        }
        System.out.println("Clicked Exclude Claim toolbar button.");
    }

    public void clickPrimeFacesConfirmDialogButton(String buttonLabel) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String label = buttonLabel.trim();
        WebElement btn = wait.until(d -> findVisiblePrimeFacesConfirmDialogButton(d, label));
        clickElementWithJsFallback(btn);
        System.out.println("Clicked PrimeFaces confirm dialog button: " + label);
    }

    public void waitForExcludeClaimForm() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("ExcludeClaimForm")),
                ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//div[contains(@id,'ExcludeClaim') and contains(@class,'ui-dialog')]"
                                + "[contains(@style,'display: block') or @aria-hidden='false']"))));
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(30)));
        System.out.println("Exclude claim form is open.");
    }

    private WebElement findVisibleConfirmDialogYesButton(WebDriver webDriver) {
        By[] locators = {
                By.id("phWLForm:confirmI"),
                By.cssSelector("button.ui-confirmdialog-yes"),
                By.xpath("//div[contains(@class,'ui-dialog-footer')]//button[.//span[normalize-space()='Yes']]")
        };
        for (By locator : locators) {
            for (WebElement candidate : webDriver.findElements(locator)) {
                try {
                    if (candidate.isDisplayed() && candidate.isEnabled()) {
                        return candidate;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // re-find on next poll
                }
            }
        }
        return null;
    }

    private WebElement findVisiblePrimeFacesConfirmDialogButton(WebDriver webDriver, String buttonLabel) {
        if ("Yes".equalsIgnoreCase(buttonLabel)) {
            return findVisibleConfirmDialogYesButton(webDriver);
        }
        if ("No".equalsIgnoreCase(buttonLabel)) {
            By[] noLocators = {
                    By.cssSelector("button.ui-confirmdialog-no"),
                    By.xpath("//div[contains(@class,'ui-dialog-footer')]//button[.//span[normalize-space()='No']]")
            };
            for (By locator : noLocators) {
                for (WebElement candidate : webDriver.findElements(locator)) {
                    try {
                        if (candidate.isDisplayed() && candidate.isEnabled()) {
                            return candidate;
                        }
                    } catch (StaleElementReferenceException ignored) {
                        // re-find on next poll
                    }
                }
            }
            return null;
        }
        String literal = xpathLiteral(buttonLabel);
        for (WebElement candidate : webDriver.findElements(By.xpath(
                "//div[contains(@class,'ui-dialog-footer')]//button[.//span[normalize-space()="
                        + literal + "]]"))) {
            try {
                if (candidate.isDisplayed() && candidate.isEnabled()) {
                    return candidate;
                }
            } catch (StaleElementReferenceException ignored) {
                // re-find on next poll
            }
        }
        return null;
    }

    public void confirmExcludeClaimDialogYes() {
        clickPrimeFacesConfirmDialogButton("Yes");
        waitForExcludeClaimForm();
        System.out.println("Confirmed exclude claim dialog with Yes.");
    }

    private String excludeClaimFormWidgetId(String fieldToken) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        String xpath = "NoteDescription".equals(fieldToken)
                ? "//form[@id='ExcludeClaimForm']//div[contains(@id,':NoteDescription')"
                        + " and not(contains(@id,'TextArea')) and contains(@class,'ui-selectonemenu')]"
                : "//form[@id='ExcludeClaimForm']//div[contains(@id,'" + fieldToken + "')"
                        + " and contains(@class,'ui-selectonemenu') and not(contains(@class,'ui-selectonemenu-panel'))]";
        WebElement widget = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return widget.getAttribute("id");
    }

    public void fillExcludeClaimForm(Map<String, String> fields) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ExcludeClaimForm")));
        String category = mapGet(fields, "Category");
        if (category != null && !category.isBlank()) {
            selectSelectOneMenuOptionWithRetry(excludeClaimFormWidgetId("ActionCategories"), category.trim());
            sleepQuietMs(800);
        }
        String preTextNote = mapGet(fields, "PreText Note");
        if (preTextNote != null && !preTextNote.isBlank()) {
            selectSelectOneMenuOptionWithRetry(excludeClaimFormWidgetId("NoteDescription"), preTextNote.trim());
        }
        String noteDescription = mapGet(fields, "Note Description");
        if (noteDescription != null && !noteDescription.isBlank()) {
            WebElement textarea = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//form[@id='ExcludeClaimForm']//textarea[contains(@id,'NoteDescriptionTextArea')]")));
            scrollToElement(textarea);
            textarea.clear();
            textarea.sendKeys(noteDescription.trim());
        }
        System.out.println("Filled exclude claim form.");
    }

    public void submitExcludeClaimForm() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//form[@id='ExcludeClaimForm']//button[.//span[normalize-space()='Exclude Claim']]")));
        clickElementWithJsFallback(btn);
        waitForBlockingOverlaysToClose(new WebDriverWait(driver, Duration.ofSeconds(60)));
        System.out.println("Submitted exclude claim form.");
    }

    /** Clicks Exclude Claim, confirms Yes, fills the form, and submits. */
    public void excludeClaimOnOpenVisit(Map<String, String> fields) {
        clickExcludeClaimToolbarButton();
        clickPrimeFacesConfirmDialogButton("Yes");
        waitForExcludeClaimForm();
        fillExcludeClaimForm(fields);
        submitExcludeClaimForm();
    }

    public boolean isActivityOrderStatusDisplayedInList(int rowIndex, String expectedStatus) {
        int row = rowIndex + 1;
        String status = expectedStatus.trim();
        By rowLocator = By.xpath("//tbody[contains(@id,'AccumedHaadActivityListForm:datalist')]"
                + "//tr[" + row + "]");
        try {
            WebElement rowEl = driver.findElement(rowLocator);
            return rowEl.getText().contains(status);
        } catch (Exception e) {
            return false;
        }
    }

    public void selectPaymentTypeCash() {
        driver.findElement(paymentTypeDropdown).click();
        String hardcodedType = "Cash";
        By option = By.xpath("//li[text()='" + hardcodedType + "']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(option));
        driver.findElement(option).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterPaymentAmount10000() {
        String hardcodedAmount = "10000";
        WebElement amountInput = driver.findElement(paymentAmountField);
        amountInput.clear();
        amountInput.sendKeys(hardcodedAmount);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void selectPaymentTypeCheque() {
        driver.findElement(paymentTypeDropdown).click();
        String hardcodedType = "Cheque";
        By option = By.xpath("//li[text()='" + hardcodedType + "']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(option));
        driver.findElement(option).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enterPaymentAmount0() {
        String hardcodedAmount = "0";
        WebElement amountInput = driver.findElement(paymentAmountField_cheque);

        // Click the input field to focus
        amountInput.click();

        // Select all the content (this simulates Ctrl+A)
        amountInput.sendKeys(Keys.CONTROL + "a");

        // Clear the field (this simulates pressing the BACKSPACE key)
        amountInput.sendKeys(Keys.SPACE);

        // Enter the new hardcoded value
        amountInput.sendKeys(hardcodedAmount);

        try {
            Thread.sleep(2000); // Optional delay (try to avoid this in real tests, use WebDriverWait instead)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void clickGenerateReceiptButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(generateReceiptButton));
        driver.findElement(generateReceiptButton).click();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void verifyReceiptSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(receiptSuccessMessage));
    }

}





