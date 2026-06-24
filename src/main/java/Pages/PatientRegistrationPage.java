package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Patient Registration / Visitor Identification — UI actions only (UC_01).
 */
public class PatientRegistrationPage {

    private final WebDriver driver;
    private String lastRegisteredPassport;
    private String initialRegisteredPassport;
    private String lastRegisteredNationalId;
    private String lastFirstName;
    private String lastLastName;

    private static final String FORM = "AccumedPatientCreateForm";
    private static final By mrnField = By.id(FORM + ":mrn");
    private static final By firstNameField = By.id(FORM + ":patientName");
    private static final By lastNameField = By.id(FORM + ":patientSurname");
    private static final By dobField = By.id(FORM + ":dateOfBirth2_input");
    private static final By passportField = By.id(FORM + ":crPassportId");
    private static final String EDIT_FORM = "AccumedPatientEditForm";
    private static final By editPatientButton = By.id("InvoiceForm:editPatientButton");
    private static final By editPatientDialog = By.id("AccumedPatientEditDlg");
    private static final By editPassportField = By.id(EDIT_FORM + ":crPassportId");
    private static final By editPatientSaveButton = By.id(EDIT_FORM + ":j_idt3685");
    private static final By createNationalIdField = By.id(FORM + ":emiratesId2");
    private static final By editNationalIdField = By.id(EDIT_FORM + ":emiratesId2");
    private static final By editNationalIdExpiryField = By.id(EDIT_FORM + ":NationalIdExpiryDateID_input");
    private static final By editNoQidCheckbox = By.id(EDIT_FORM + ":NoQIDCheck_input");
    private static final By editNoQidCheckboxBox = By.xpath(
            "//div[@id='" + EDIT_FORM + ":NoQIDCheck']//div[contains(@class,'ui-chkbox-box')]");
    private static final By noQidCheckbox = By.id(FORM + ":NoQIDCheck_input");
    private static final By noQidCheckboxBox = By.xpath(
            "//div[@id='" + FORM + ":NoQIDCheck']//div[contains(@class,'ui-chkbox-box')]");
    private static final String NO_NATIONAL_ID_WIDGET = FORM + ":noNationalID";
    private static final String VISITOR_SUB_CATEGORY_WIDGET = FORM + ":visitorSubCategory";
    private static final String VISA_TYPE_WIDGET = FORM + ":visaType";
    private static final By encounterSection = By.xpath("//legend[normalize-space()='Encounter']");
    private static final By growlError = By.xpath("//span[contains(@class,'ui-growl-title')]");
    private static final By healthInsuranceFieldset = By.id("AccumedPatientCreateForm:INSID");
    private static final By insuranceCardsTableBody = By.id("AccumedPatientCreateForm:datalist_data");
    private static final String INSURANCE_CARD_ROW_XPATH =
            "//tbody[@id='AccumedPatientCreateForm:datalist_data']/tr[contains(@class,'ui-datatable-selectable')]";

    public PatientRegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getLastRegisteredPassport() {
        return lastRegisteredPassport;
    }

    public String getInitialRegisteredPassport() {
        return initialRegisteredPassport;
    }

    public String getLastRegisteredNationalId() {
        return lastRegisteredNationalId;
    }

    /** Opens Edit Patient modal from the encounter screen ({@code InvoiceForm:editPatientButton}). */
    public void clickEditPatientButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(editPatientButton));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPassportField));
        waitForAjaxSettle(wait);
        System.out.println("Edit Patient modal opened.");
    }

    /**
     * Updates passport in the Edit Patient modal ({@code AccumedPatientEditForm:crPassportId}).
     * When {@code newPassportValue} is {@code auto}, generates a number different from the current one.
     */
    public void updatePassportInEditPatientModal(String newPassportValue) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPassportField));
        String passport = "auto".equalsIgnoreCase(newPassportValue != null ? newPassportValue.trim() : "")
                ? generateDifferentPassport()
                : newPassportValue.trim();
        setTextInput(editPassportField, passport);
        lastRegisteredPassport = readPassportValueFromField(editPassportField, passport);
        waitForAjaxSettle(wait);
        System.out.println("Passport updated in Edit Patient modal to: " + lastRegisteredPassport
                + (initialRegisteredPassport != null ? " (was: " + initialRegisteredPassport + ")" : ""));
    }

    /** Saves Edit Patient modal and waits for it to close and encounter screen to refresh. */
    public void saveEditPatientModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(editPatientSaveButton));
        scrollToElement(saveBtn);
        try {
            saveBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(editPatientDialog));
        waitForAjaxSettle(wait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(encounterSection));
        if (lastRegisteredPassport != null && !lastRegisteredPassport.isBlank()) {
            final String expectedPassport = lastRegisteredPassport;
            try {
                wait.until(d -> {
                    String actual = getPassportFromForm();
                    return actual != null && actual.contains(expectedPassport);
                });
            } catch (org.openqa.selenium.TimeoutException ignored) {
                System.out.println("Passport field not yet refreshed on patient form after save.");
            }
        }
        if (lastRegisteredNationalId != null && !lastRegisteredNationalId.isBlank()) {
            final String expectedNationalId = lastRegisteredNationalId;
            try {
                wait.until(d -> {
                    String actual = getNationalIdFromForm();
                    return actual != null && actual.contains(expectedNationalId);
                });
            } catch (org.openqa.selenium.TimeoutException ignored) {
                System.out.println("National ID field not yet refreshed on patient form after save.");
            }
        }
        System.out.println("Edit Patient modal saved — encounter screen refreshed.");
    }

    /**
     * TC07 — unchecks No QID in Edit Patient modal and fills National ID + expiry
     * ({@code AccumedPatientEditForm:emiratesId2}).
     */
    public void uncheckNoQidAndFillNationalIdInEditPatientModal(Map<String, String> data) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        disableEditNoQidCheckbox(wait);
        waitForEditNationalIdFieldReady(wait);
        String nationalId = resolveNationalId(data.get("National ID"));
        if (nationalId == null || nationalId.isBlank()) {
            nationalId = resolveNationalId(data.get("QID"));
        }
        String expiry = data.getOrDefault("QID Expiry Date", "21/04/2027");
        fillEditPatientNationalId(nationalId, expiry);
        lastRegisteredNationalId = readNationalIdValueFromField(editNationalIdField, nationalId);
        waitForAjaxSettle(wait);
        System.out.println("TC07 — National ID entered in Edit Patient modal: " + lastRegisteredNationalId
                + ", expiry: " + expiry);
    }

    public boolean isNationalIdEmptyOnForm() {
        String actual = getNationalIdFromForm();
        System.out.println("National ID on form (expect empty): '" + actual + "'");
        return actual == null || actual.isBlank();
    }

    public boolean isNationalIdOnForm(String expectedNationalId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(d -> {
                String actual = getNationalIdFromForm();
                return actual != null && actual.contains(expectedNationalId);
            });
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // fall through — log actual value below
        }
        String actual = getNationalIdFromForm();
        System.out.println("National ID — expected: " + expectedNationalId + ", actual: " + actual);
        return actual != null && actual.contains(expectedNationalId);
    }

    public String getNationalIdFromForm() {
        By[] candidates = {
                createNationalIdField,
                editNationalIdField,
                By.xpath("//input[contains(@id,'emiratesId2')]")
        };
        String hiddenValue = "";
        for (By by : candidates) {
            for (WebElement input : driver.findElements(by)) {
                try {
                    String value = input.getAttribute("value");
                    if (value == null || value.isBlank()) {
                        continue;
                    }
                    if (input.isDisplayed()) {
                        return value.trim();
                    }
                    if (hiddenValue.isBlank()) {
                        hiddenValue = value.trim();
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next candidate
                }
            }
        }
        return hiddenValue;
    }

    public String getLastPatientFullName() {
        if (lastFirstName == null || lastLastName == null) {
            return "";
        }
        return lastFirstName.trim() + " " + lastLastName.trim();
    }

    public void fillVisitorPatientForm(Map<String, String> data, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(mrnField));

        String mrn = resolveAuto(data.get("MRN"), "VIS");
        String firstName = resolveAuto(data.get("First Name"), "Test");
        String lastName = resolveAuto(data.get("Last Name"), "Visitor");
        String dob = data.getOrDefault("DOB", "11/04/1966");
        String passport = resolvePassport(data.get("Passport"));
        String nationality = data.getOrDefault("Nationality", "ALB");
        String missingReason = data.get("Reason for Absence of QID");
        String visitorSubCategory = data.getOrDefault("Visitor Sub-Category", "2");
        String visaType = data.getOrDefault("Visa Type", "2");
        boolean checkNoQid = !"false".equalsIgnoreCase(data.getOrDefault("No QID Check", "true"));

        setTextInput(mrnField, mrn);
        setTextInput(firstNameField, firstName);
        setTextInput(lastNameField, lastName);
        setTextInput(dobField, dob);
        loginPage.selectNationality("ALB".equalsIgnoreCase(nationality) ? "ALBAN" : nationality);
        loginPage.selectGender();

        if (checkNoQid) {
            enableNoQidCheckbox(wait);
            waitForAjaxSettle(wait);
        }
        if (missingReason != null && !missingReason.isBlank()) {
            selectMissingQidReason(loginPage, missingReason);
        }

        setTextInput(passportField, passport);
        if (hasValue(data, "Visitor Sub-Category")) {
            loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, visitorSubCategory);
        }
        if (hasValue(data, "Visa Type")) {
            loginPage.selectCreatePatientDropdown(VISA_TYPE_WIDGET, visaType);
        }

        lastFirstName = firstName;
        lastLastName = lastName;
        lastRegisteredPassport = readPassportValue(passport);
        if (initialRegisteredPassport == null) {
            initialRegisteredPassport = lastRegisteredPassport;
        }
        System.out.println("Filled visitor patient form — MRN: " + mrn + ", Passport: " + lastRegisteredPassport
                + ", Name: " + getLastPatientFullName());
    }

    public void fillBabyVisitorPatientForm(Map<String, String> data, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(mrnField));

        String mrn = resolveAuto(data.get("MRN"), "BABY");
        String firstName = resolveAuto(data.get("First Name"), "Baby");
        String lastName = hasValue(data, "Last Name")
                ? resolveAuto(data.get("Last Name"), "Baby")
                : (lastLastName != null && !lastLastName.isBlank() ? lastLastName : resolveAuto(null, "Baby"));
        String dob = data.getOrDefault("DOB", "01/06/2026");
        String passport = resolvePassport(data.get("Passport"));
        String nationality = data.getOrDefault("Nationality", "ALB");
        String missingReason = data.getOrDefault("Reason for Absence of QID", "New Born");
        String visitorSubCategory = data.getOrDefault("Visitor Sub-Category", "2");
        String visaType = data.getOrDefault("Visa Type", "2");
        boolean checkNoQid = !"false".equalsIgnoreCase(data.getOrDefault("No QID Check", "true"));
        boolean usePassport = hasValue(data, "Passport");

        setTextInput(mrnField, mrn);
        setTextInput(firstNameField, firstName);
        setTextInput(lastNameField, lastName);
        setTextInput(dobField, dob);
        loginPage.selectNationality("ALB".equalsIgnoreCase(nationality) ? "ALBAN" : nationality);
        loginPage.selectGender();

        if (checkNoQid) {
            enableNoQidCheckbox(wait);
            waitForAjaxSettle(wait);
        }
        if (missingReason != null && !missingReason.isBlank()) {
            selectMissingQidReason(loginPage, missingReason);
        }
        if (usePassport) {
            setTextInput(passportField, passport);
            lastRegisteredPassport = readPassportValue(passport);
        }

        if (hasValue(data, "Visitor Sub-Category")) {
            loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, visitorSubCategory);
        }
        if (hasValue(data, "Visa Type")) {
            loginPage.selectCreatePatientDropdown(VISA_TYPE_WIDGET, visaType);
        }

        System.out.println("Filled baby visitor patient form — MRN: " + mrn + ", Name: " + firstName + " " + lastName);
    }

    public boolean isVisitLabelVisitorInsuredOrSelfPay() {
        String label = getVisitLabelText();
        System.out.println("Visit label text: " + label);
        if (label == null || label.isBlank()) {
            return false;
        }
        String normalized = label.toLowerCase();
        boolean hasVisitor = normalized.contains("visitor");
        boolean hasInsured = normalized.contains("insured");
        boolean hasSelfPay = normalized.contains("self pay") || normalized.contains("self-pay");
        return hasVisitor && (hasInsured || hasSelfPay);
    }

    public String getVisitLabelText() {
        By[] locators = {
                By.xpath("//*[contains(@id,'visitLabel') or contains(@id,'visitType') or contains(@id,'billingGroup')"
                        + " or contains(@id,'BillingGroup')]"),
                By.xpath("//label[contains(normalize-space(.),'Visit Label')]/following::*[1]"),
                By.xpath("//*[contains(@id,'AccumedPatientCreateForm') and contains(@id,'visit')]")
        };
        for (By by : locators) {
            for (WebElement el : driver.findElements(by)) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText().trim();
                        if (!text.isEmpty()) {
                            return text;
                        }
                        String value = el.getAttribute("value");
                        if (value != null && !value.isBlank()) {
                            return value.trim();
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next element
                }
            }
        }
        List<WebElement> visitorHits = driver.findElements(By.xpath(
                "//*[contains(normalize-space(.),'Visitor') and (contains(.,'Insured') or contains(.,'Self Pay'))]"));
        for (WebElement hit : visitorHits) {
            try {
                if (hit.isDisplayed()) {
                    String text = hit.getText().trim();
                    if (!text.isEmpty()) {
                        return text;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        return "";
    }

    public void expandHealthInsuranceSectionIfNeeded() {
        try {
            WebElement collapsed = driver.findElement(By.id("AccumedPatientCreateForm:INSID_collapsed"));
            if ("true".equalsIgnoreCase(collapsed.getAttribute("value"))) {
                WebElement legend = driver.findElement(
                        By.xpath("//fieldset[@id='AccumedPatientCreateForm:INSID']//legend"));
                scrollToElement(legend);
                legend.click();
                sleepQuietMs(500);
            }
        } catch (Exception ignored) {
            // fieldset may already be expanded
        }
    }

    public int getPatientInsuranceCardCount() {
        expandHealthInsuranceSectionIfNeeded();
        List<WebElement> rows = driver.findElements(By.xpath(INSURANCE_CARD_ROW_XPATH));
        int count = 0;
        for (WebElement row : rows) {
            try {
                if (row.isDisplayed() && !row.getText().trim().isEmpty()) {
                    count++;
                }
            } catch (StaleElementReferenceException ignored) {
                // recount on next poll
            }
        }
        return count;
    }

    public void waitForInsuranceCardCount(int expectedCount) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(d -> getPatientInsuranceCardCount() >= expectedCount);
        System.out.println("Health Insurance Info list shows " + getPatientInsuranceCardCount() + " card(s).");
    }

    public boolean isInsuranceCardListedContaining(String expectedText) {
        if (expectedText == null || expectedText.isBlank()) {
            return false;
        }
        expandHealthInsuranceSectionIfNeeded();
        String needle = expectedText.trim().toLowerCase();
        for (WebElement row : driver.findElements(By.xpath(INSURANCE_CARD_ROW_XPATH))) {
            try {
                if (!row.isDisplayed()) {
                    continue;
                }
                String rowText = row.getText();
                if (rowText != null && rowText.toLowerCase().contains(needle)) {
                    System.out.println("Found insurance card row containing: " + expectedText);
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next row
            }
        }
        return false;
    }

    public boolean isHealthInsuranceSectionDisplayed() {
        try {
            expandHealthInsuranceSectionIfNeeded();
            WebElement fieldset = driver.findElement(healthInsuranceFieldset);
            return fieldset.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void fillVisitorPatientFormWithoutQidAndReason(Map<String, String> data, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(mrnField));

        String mrn = resolveAuto(data.get("MRN"), "VIS");
        String firstName = resolveAuto(data.get("First Name"), "Test");
        String lastName = resolveAuto(data.get("Last Name"), "NoReason");
        String dob = data.getOrDefault("DOB", "11/04/1966");
        String passport = resolvePassport(data.get("Passport"));
        String nationality = data.getOrDefault("Nationality", "ALB");
        String visitorSubCategory = data.getOrDefault("Visitor Sub-Category", "2");
        String visaType = data.getOrDefault("Visa Type", "2");

        setTextInput(mrnField, mrn);
        setTextInput(firstNameField, firstName);
        setTextInput(lastNameField, lastName);
        setTextInput(dobField, dob);
        loginPage.selectNationality("ALB".equalsIgnoreCase(nationality) ? "ALBAN" : nationality);
        loginPage.selectGender();
        enableNoQidCheckbox(wait);
        waitForAjaxSettle(wait);
        setTextInput(passportField, passport);
        loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, visitorSubCategory);
        loginPage.selectCreatePatientDropdown(VISA_TYPE_WIDGET, visaType);

        lastFirstName = firstName;
        lastLastName = lastName;
        lastRegisteredPassport = readPassportValue(passport);
        System.out.println("TC02 form — No QID checked, passport filled, missing reason skipped. MRN: " + mrn);
    }

    /**
     * Visitor with No QID checked, reason for absence filled, passport intentionally left empty (Cerner ADT path).
     */
    public void fillVisitorPatientFormWithNoQidAndNoPassport(Map<String, String> data, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(mrnField));

        String mrn = resolveAuto(data.get("MRN"), "VIS");
        String firstName = resolveAuto(data.get("First Name"), "QidFix");
        String lastName = resolveAuto(data.get("Last Name"), "Visitor");
        String dob = data.getOrDefault("DOB", "11/04/1966");
        String nationality = data.getOrDefault("Nationality", "ALB");
        String missingReason = data.getOrDefault("Reason for Absence of QID", "1");
        String visitorSubCategory = data.getOrDefault("Visitor Sub-Category", "2");
        String visaType = data.getOrDefault("Visa Type", "2");

        setTextInput(mrnField, mrn);
        setTextInput(firstNameField, firstName);
        setTextInput(lastNameField, lastName);
        setTextInput(dobField, dob);
        loginPage.selectNationality("ALB".equalsIgnoreCase(nationality) ? "ALBAN" : nationality);
        loginPage.selectGender();
        enableNoQidCheckbox(wait);
        waitForAjaxSettle(wait);
        selectMissingQidReason(loginPage, missingReason);

        if (hasValue(data, "Visitor Sub-Category")) {
            loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, visitorSubCategory);
        }
        if (hasValue(data, "Visa Type")) {
            loginPage.selectCreatePatientDropdown(VISA_TYPE_WIDGET, visaType);
        }

        lastFirstName = firstName;
        lastLastName = lastName;
        System.out.println("TC05 initial form — No QID + reason, passport empty. MRN: " + mrn);
    }

    public void uncheckNoQidAndFillQidDetails(Map<String, String> data, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        disableNoQidCheckbox(wait);
        loginPage.waitForNationalIdFieldReady();
        String nationalId = resolveNationalId(data.get("National ID"));
        if (nationalId == null || nationalId.isBlank()) {
            nationalId = resolveNationalId(data.get("QID"));
        }
        String expiry = data.getOrDefault("QID Expiry Date", "21/04/2027");
        loginPage.fillPatientQid(nationalId, expiry);
        waitForAjaxSettle(wait);
        System.out.println("TC05 correction — No QID unchecked, National ID entered.");
    }

    public boolean isPatientInsertBlocked() {
        if (isValidationErrorDisplayed() || isInlineValidationDisplayed()) {
            return true;
        }
        return isCreatePatientScreenStillVisible() && !isEncounterSectionActive();
    }

    public boolean isPassportNumber(String expectedPassport) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(d -> {
                String actual = getPassportFromForm();
                return actual != null && actual.contains(expectedPassport);
            });
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // fall through — log actual value below
        }
        String actual = getPassportFromForm();
        System.out.println("Passport — expected: " + expectedPassport + ", actual: " + actual);
        return actual != null && actual.contains(expectedPassport);
    }

    public void clickInsertPatientOnly() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id(FORM + ":insertNewPatient")));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        waitForAjaxSettle(wait);
        System.out.println("Clicked Insert Patient (no encounter wait).");
    }

    public boolean isInsertPatientButtonStillAvailable() {
        try {
            WebElement insertBtn = driver.findElement(By.id(FORM + ":insertNewPatient"));
            return insertBtn.isDisplayed() && insertBtn.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPassportFromForm() {
        By[] candidates = {
                passportField,
                editPassportField,
                By.xpath("//input[contains(@id,'crPassportId')]")
        };
        String hiddenValue = "";
        for (By by : candidates) {
            for (WebElement input : driver.findElements(by)) {
                try {
                    String value = input.getAttribute("value");
                    if (value == null || value.isBlank()) {
                        continue;
                    }
                    if (input.isDisplayed()) {
                        return value.trim();
                    }
                    if (hiddenValue.isBlank()) {
                        hiddenValue = value.trim();
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next candidate
                }
            }
        }
        return hiddenValue;
    }

    private String readPassportValue(String fallback) {
        return readPassportValueFromField(passportField, fallback);
    }

    private String readPassportValueFromField(By field, String fallback) {
        for (WebElement input : driver.findElements(field)) {
            try {
                if (input.isDisplayed()) {
                    String actual = input.getAttribute("value");
                    if (actual != null && !actual.isBlank()) {
                        return actual.trim();
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // retry on next element
            }
        }
        return fallback;
    }

    private void enableNoQidCheckbox(WebDriverWait wait) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(noQidCheckbox));
        if (!input.isSelected()) {
            try {
                WebElement box = driver.findElement(noQidCheckboxBox);
                scrollToElement(box);
                box.click();
            } catch (Exception e) {
                scrollToElement(input);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
            }
        }
        waitForAjaxSettle(wait);
    }

    private void disableEditNoQidCheckbox(WebDriverWait wait) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(editNoQidCheckbox));
        if (input.isSelected()) {
            try {
                WebElement box = driver.findElement(editNoQidCheckboxBox);
                scrollToElement(box);
                box.click();
            } catch (Exception e) {
                scrollToElement(input);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
            }
        }
        wait.until(d -> !d.findElement(editNoQidCheckbox).isSelected());
        waitForAjaxSettle(wait);
        sleepQuietMs(800);
    }

    private void waitForEditNationalIdFieldReady(WebDriverWait wait) {
        wait.until(d -> {
            WebElement noQid = d.findElement(editNoQidCheckbox);
            if (noQid.isSelected()) {
                return false;
            }
            WebElement nationalId = d.findElement(editNationalIdField);
            if (!nationalId.isDisplayed() || !nationalId.isEnabled()) {
                return false;
            }
            String cssClass = nationalId.getAttribute("class");
            return cssClass == null || !cssClass.contains("ui-state-disabled");
        });
    }

    private void fillEditPatientNationalId(String nationalId, String expiryDateDdMmYyyy) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement nationalIdInput = wait.until(ExpectedConditions.elementToBeClickable(editNationalIdField));
        scrollToElement(nationalIdInput);
        nationalIdInput.click();
        nationalIdInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        nationalIdInput.sendKeys(Keys.DELETE);
        nationalIdInput.sendKeys(nationalId);
        nationalIdInput.sendKeys(Keys.TAB);
        sleepQuietMs(600);
        setTextInput(editNationalIdExpiryField, expiryDateDdMmYyyy);
    }

    private String readNationalIdValueFromField(By field, String fallback) {
        for (WebElement input : driver.findElements(field)) {
            try {
                String actual = input.getAttribute("value");
                if (actual != null && !actual.isBlank()) {
                    return actual.trim();
                }
            } catch (StaleElementReferenceException ignored) {
                // retry on next element
            }
        }
        return fallback;
    }

    private void disableNoQidCheckbox(WebDriverWait wait) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(noQidCheckbox));
        if (input.isSelected()) {
            try {
                WebElement box = driver.findElement(noQidCheckboxBox);
                scrollToElement(box);
                box.click();
            } catch (Exception e) {
                scrollToElement(input);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
            }
        }
        wait.until(d -> !d.findElement(noQidCheckbox).isSelected());
        waitForAjaxSettle(wait);
        sleepQuietMs(800);
    }

    private boolean isValidationErrorDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(growlError));
            String message = driver.findElement(growlError).getText().trim();
            System.out.println("Validation message: " + message);
            return !message.isEmpty();
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }

    private boolean isInlineValidationDisplayed() {
        List<WebElement> errors = driver.findElements(By.xpath(
                "//div[contains(@class,'ui-message-error')]"
                        + " | //span[contains(@class,'ui-message-error-detail')]"
                        + " | //*[contains(@class,'ui-state-error') and contains(@id,'noNationalID')]"));
        for (WebElement err : errors) {
            try {
                if (err.isDisplayed() && !err.getText().trim().isEmpty()) {
                    System.out.println("Inline validation: " + err.getText().trim());
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // continue
            }
        }
        return false;
    }

    private boolean isCreatePatientScreenStillVisible() {
        try {
            return driver.findElement(By.xpath("//span[text()='Create Patient']")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isEncounterSectionActive() {
        try {
            WebElement legend = driver.findElement(encounterSection);
            if (!legend.isDisplayed()) {
                return false;
            }
            WebElement insertBtn = driver.findElement(By.id(FORM + ":insertNewPatient"));
            return !insertBtn.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void selectMissingQidReason(LoginPage loginPage, String reason) {
        if (reason.matches("\\d+")) {
            loginPage.selectCreatePatientDropdown(NO_NATIONAL_ID_WIDGET, reason);
            return;
        }
        try {
            loginPage.selectCreatePatientDropdown(NO_NATIONAL_ID_WIDGET, reason);
        } catch (RuntimeException exactMatchFailed) {
            if (reason.toLowerCase().contains("born")) {
                loginPage.selectCreatePatientDropdownContaining(NO_NATIONAL_ID_WIDGET, "born");
                return;
            }
            throw exactMatchFailed;
        }
    }

    private static boolean hasValue(Map<String, String> data, String key) {
        return data.containsKey(key) && data.get(key) != null && !data.get(key).isBlank();
    }

    private void setTextInput(By locator, String value) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollToElement(input);
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(value);
    }

    private void selectSelectOneMenuByLabelOrIndex(String widgetBaseId, String labelOrIndex, WebDriverWait wait) {
        if (labelOrIndex == null || labelOrIndex.isBlank()) {
            return;
        }
        openSelectOneMenuPanel(widgetBaseId, wait);
        WebElement panel = findVisiblePanel(widgetBaseId + "_panel", wait);
        WebElement option = findOption(panel, labelOrIndex, wait);
        scrollToElement(option);
        try {
            new Actions(driver).moveToElement(option).click().perform();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
        }
        waitForAjaxSettle(wait);
    }

    private void openSelectOneMenuPanel(String widgetBaseId, WebDriverWait wait) {
        By labelBy = By.id(widgetBaseId + "_label");
        if (!driver.findElements(labelBy).isEmpty()) {
            WebElement label = wait.until(ExpectedConditions.elementToBeClickable(labelBy));
            scrollToElement(label);
            label.click();
        } else {
            By trigger = By.xpath("//*[@id='" + widgetBaseId + "']//div[contains(@class,'ui-selectonemenu-trigger')]");
            wait.until(ExpectedConditions.elementToBeClickable(trigger)).click();
        }
        wait.until(d -> isPanelVisible(widgetBaseId + "_panel"));
    }

    private WebElement findVisiblePanel(String panelId, WebDriverWait wait) {
        return wait.until(d -> {
            for (WebElement p : d.findElements(By.id(panelId))) {
                if (p.isDisplayed()) {
                    return p;
                }
            }
            return null;
        });
    }

    private WebElement findOption(WebElement panel, String labelOrIndex, WebDriverWait wait) {
        if (labelOrIndex.matches("\\d+")) {
            int index = Integer.parseInt(labelOrIndex);
            List<WebElement> items = panel.findElements(By.cssSelector("li.ui-selectonemenu-item"));
            if (index < items.size()) {
                return items.get(index);
            }
        }
        String[] patterns = {
                ".//li[@data-label=" + xpathLiteral(labelOrIndex) + "]",
                ".//li[contains(normalize-space(.), " + xpathLiteral(labelOrIndex) + ")]"
        };
        for (String pattern : patterns) {
            for (WebElement li : panel.findElements(By.xpath(pattern))) {
                if (li.isDisplayed()) {
                    return li;
                }
            }
        }
        return wait.until(d -> {
            for (WebElement li : panel.findElements(By.cssSelector("li.ui-selectonemenu-item"))) {
                if (!li.isDisplayed()) {
                    continue;
                }
                String dl = li.getAttribute("data-label");
                if (dl != null && dl.toLowerCase().contains(labelOrIndex.toLowerCase())) {
                    return li;
                }
                if (li.getText().toLowerCase().contains(labelOrIndex.toLowerCase())) {
                    return li;
                }
            }
            return null;
        });
    }

    private boolean isPanelVisible(String panelId) {
        for (WebElement p : driver.findElements(By.id(panelId))) {
            if (p.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    private void waitForAjaxSettle(WebDriverWait wait) {
        try {
            wait.until(d -> !isBlockingOverlayVisible());
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // continue
        }
        sleepQuietMs(500);
    }

    private boolean isBlockingOverlayVisible() {
        for (WebElement overlay : driver.findElements(By.cssSelector(
                "#statusDialog_modal, .ui-widget-overlay.ui-dialog-mask, .ui-blockui"))) {
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

    private static String resolveAuto(String value, String prefix) {
        if (value == null || value.isBlank() || "auto".equalsIgnoreCase(value.trim())) {
            return prefix + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        }
        return value.trim();
    }

    private static String resolvePassport(String value) {
        if (value == null || value.isBlank() || "auto".equalsIgnoreCase(value.trim())) {
            return String.valueOf(100000000 + new java.util.Random().nextInt(900000000));
        }
        return value.trim();
    }

    private String generateDifferentPassport() {
        String current = lastRegisteredPassport != null ? lastRegisteredPassport : "";
        for (int i = 0; i < 25; i++) {
            String candidate = resolvePassport("auto");
            if (!candidate.equals(current)) {
                return candidate;
            }
        }
        return String.valueOf(Long.parseLong(current.isEmpty() ? "100000001" : current) + 1);
    }

    private static String resolveNationalId(String value) {
        if (value == null || value.isBlank() || "auto".equalsIgnoreCase(value.trim())) {
            return String.format("%011d", 10000000000L + new java.util.Random().nextInt(900000000));
        }
        return value.trim();
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
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
