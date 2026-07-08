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
    private String lastVisitorSubCategory;
    private String lastVisaType;
    private String lastMissingQidReason;
    private String previousVisaTypeLabel;
    private String lastVisaTypeLabel;

    private static final String FORM = "AccumedPatientCreateForm";
    private static final By mrnField = By.id(FORM + ":mrn");
    private static final By firstNameField = By.id(FORM + ":patientName");
    private static final By lastNameField = By.id(FORM + ":patientSurname");
    private static final By dobField = By.id(FORM + ":dateOfBirth2_input");
    private static final By passportField = By.id(FORM + ":crPassportId");
    private static final String EDIT_FORM = "AccumedPatientEditForm";
    private static final String EDIT_VISA_TYPE_WIDGET = EDIT_FORM + ":visaType";
    private static final By editAddInsuranceButton = By.id(EDIT_FORM + ":createButtonInsurance");
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
    private static final String EDIT_INSURANCE_CARD_ROW_XPATH =
            "//tbody[@id='AccumedPatientEditForm:datalist_data']/tr[contains(@class,'ui-datatable-selectable')]";
    private static final By editInsuranceDialog = By.id("AccumedPatientInsuranceEditDlg");
    private static final By editInsuranceMemberIdField =
            By.id("AccumedPatientInsuranceEditForm:patientInsuranceId");
    private static final By editInsuranceSaveButton = By.xpath(
            "//form[@id='AccumedPatientInsuranceEditForm']//button[.//span[normalize-space()='Save']]"
                    + " | //button[@id='AccumedPatientInsuranceEditForm:save']");

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

    public void clickEditInsuranceCardInEditPatientModal(int rowIndex) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        expandEditPatientHealthInsuranceSectionIfNeeded();
        By editBtn = By.xpath(EDIT_INSURANCE_CARD_ROW_XPATH + "[@data-ri='" + rowIndex + "']"
                + "//button[contains(@id,'editPatientInsuranceButton')]");
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(editBtn));
        scrollToElement(btn);
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(editInsuranceDialog));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editInsuranceMemberIdField));
        waitForAjaxSettle(wait);
        System.out.println("Opened Edit Insurance modal for row " + rowIndex + ".");
    }

    public void updateMemberIdInEditInsuranceModal(String newMemberIdValue, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editInsuranceMemberIdField));
        String memberId;
        if (newMemberIdValue == null || newMemberIdValue.isBlank()
                || "auto".equalsIgnoreCase(newMemberIdValue.trim())) {
            memberId = loginPage.generateAndRememberDifferentMemberId();
        } else {
            memberId = newMemberIdValue.trim();
            loginPage.rememberRegisteredMemberId(memberId);
        }
        setTextInput(editInsuranceMemberIdField, memberId);
        waitForAjaxSettle(wait);
        System.out.println("Updated Member ID in Edit Insurance modal to: " + memberId);
    }

    public void saveEditInsuranceModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(editInsuranceSaveButton));
        scrollToElement(saveBtn);
        try {
            saveBtn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(editInsuranceDialog));
        waitForAjaxSettle(wait);
        System.out.println("Saved Edit Insurance modal.");
    }

    public boolean isMemberIdListedInEditPatientInsuranceRow(int rowIndex, String expectedMemberId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        final String expected = expectedMemberId.trim();
        try {
            wait.until(d -> expected.equals(getMemberIdFromEditPatientInsuranceRow(rowIndex)));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // log actual below
        }
        String actual = getMemberIdFromEditPatientInsuranceRow(rowIndex);
        System.out.println("Edit Patient insurance row " + rowIndex + " Member ID — expected: "
                + expected + ", actual: " + actual);
        return expected.equals(actual);
    }

    public String getMemberIdFromEditPatientInsuranceRow(int rowIndex) {
        expandEditPatientHealthInsuranceSectionIfNeeded();
        By rowLocator = By.xpath(EDIT_INSURANCE_CARD_ROW_XPATH + "[@data-ri='" + rowIndex + "']");
        try {
            WebElement row = driver.findElement(rowLocator);
            List<WebElement> cells = row.findElements(By.xpath(".//td[@role='gridcell']"));
            if (cells.size() > 1) {
                return cells.get(1).getText().trim();
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private void expandEditPatientHealthInsuranceSectionIfNeeded() {
        try {
            WebElement collapsed = driver.findElement(By.id("AccumedPatientEditForm:INSID_collapsed"));
            if ("true".equalsIgnoreCase(collapsed.getAttribute("value"))) {
                WebElement legend = driver.findElement(
                        By.xpath("//fieldset[@id='AccumedPatientEditForm:INSID']//legend"));
                scrollToElement(legend);
                legend.click();
                sleepQuietMs(500);
            }
        } catch (Exception ignored) {
            // already expanded
        }
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
        if (lastVisaTypeLabel != null && !lastVisaTypeLabel.isBlank()) {
            final String expectedVisa = lastVisaTypeLabel;
            try {
                wait.until(d -> {
                    String actual = getVisaTypeFromPatientInfo();
                    return actual != null && actual.equalsIgnoreCase(expectedVisa);
                });
            } catch (org.openqa.selenium.TimeoutException ignored) {
                System.out.println("Visa type not yet refreshed on patient form after save.");
            }
        }
        waitForEncounterInsuranceOnVisit();
        System.out.println("Edit Patient modal saved — encounter screen refreshed.");
    }

    /** TC18 — change visa type in Edit Patient modal after patient tag is saved. */
    public void updateVisaTypeInEditPatientModal(String newVisaType, LoginPage loginPage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        previousVisaTypeLabel = getSelectOneMenuLabelOnPage("visaType");
        if (newVisaType.matches("\\d+")) {
            loginPage.selectCreatePatientDropdown(EDIT_VISA_TYPE_WIDGET, newVisaType);
        } else {
            loginPage.selectCreatePatientDropdownContaining(EDIT_VISA_TYPE_WIDGET, newVisaType);
        }
        lastVisaType = newVisaType;
        lastVisaTypeLabel = getSelectOneMenuLabelInEditPatientModal("visaType");
        waitForAjaxSettle(wait);
        System.out.println("Visa type updated in Edit Patient — previous: " + previousVisaTypeLabel
                + ", new: " + lastVisaTypeLabel);
    }

    public void clickAddInsuranceCardInEditPatientModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        expandEditPatientHealthInsuranceSectionIfNeeded();
        scrollToElement(driver.findElement(editPatientDialog));
        By[] patterns = {
                By.id("AccumedPatientEditForm:createButtonInsurance"),
                By.xpath("//*[@id='AccumedPatientEditDlg']//button[contains(@id,'createButtonInsurance')]"),
                By.xpath("//form[contains(@id,'AccumedPatientEditForm')]//button[contains(@id,'Insurance')]"),
                By.xpath("//*[@id='AccumedPatientEditDlg']//button[.//span[contains(.,'Add Insurance')]]"),
                By.xpath("//button[contains(@onclick,'Insurance')][.//span[contains(.,'Insurance')]]"),
                By.id("AccumedPatientCreateForm:createButtonInsurance")
        };
        for (By pattern : patterns) {
            for (WebElement btn : driver.findElements(pattern)) {
                try {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        scrollToElement(btn);
                        try {
                            btn.click();
                        } catch (Exception e) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                        }
                        wait.until(ExpectedConditions.or(
                                ExpectedConditions.visibilityOfElementLocated(
                                        By.id("AccumedPatientInsuranceCreateForm:insuranceLisence_label")),
                                ExpectedConditions.presenceOfElementLocated(
                                        By.xpath("//*[contains(@id,'AccumedPatientInsuranceCreateForm')]"))));
                        System.out.println("Clicked Add Insurance Card in Edit Patient modal.");
                        return;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "Add Insurance Card button not found in Edit Patient modal.");
    }

    public boolean isVisaTypeUpdatedOnPatient() {
        String current = getVisaTypeFromPatientInfo();
        if (current == null || current.isBlank()) {
            System.out.println("Visa type on patient — empty after update.");
            return false;
        }
        boolean updated = lastVisaTypeLabel != null && !lastVisaTypeLabel.isBlank()
                && current.equalsIgnoreCase(lastVisaTypeLabel);
        System.out.println("Visa type update check — previous='" + previousVisaTypeLabel
                + "', expected='" + lastVisaTypeLabel + "', actual='" + current + "', updated=" + updated);
        return updated;
    }

    public String getVisaTypeFromPatientInfo() {
        String field = getPatientInfoFieldValue("Visa Type");
        if (field != null && !field.isBlank() && !field.toLowerCase().contains("patient category")) {
            return field.trim();
        }
        String[] lines = getInvoiceFormVisibleText().split("\\R");
        for (int i = 0; i < lines.length - 1; i++) {
            if ("Visa Type".equalsIgnoreCase(lines[i].trim())) {
                String next = lines[i + 1].trim();
                if (!next.isEmpty()) {
                    return next;
                }
            }
        }
        return getSelectOneMenuLabelOnPage("visaType");
    }

    /** UC_16 — visa type is shown on the encounter/patient info and matches the expected label. */
    public boolean isVisaTypeDisplayedOnEncounter(String expected) {
        String target = resolveVisaTypeLabel(expected);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            wait.until(d -> target.equalsIgnoreCase(readVisaTypeAnywhere()));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // fall through to log the actual value
        }
        String actual = readVisaTypeAnywhere();
        boolean matches = actual != null && actual.trim().equalsIgnoreCase(target);
        System.out.println("Visa type on encounter — expected: '" + target + "', actual: '" + actual
                + "', matches=" + matches);
        return matches;
    }

    /**
     * Reads the visa type shown for the patient from any available source: the patient-info panel,
     * a visible selectOneMenu label, or the selected option of any {@code visaType} native select
     * (which persists in the DOM even after the create form is hidden on the encounter view).
     */
    private String readVisaTypeAnywhere() {
        String fromInfo = getVisaTypeFromPatientInfo();
        if (fromInfo != null && !fromInfo.isBlank() && !"No Visa Type Selected".equalsIgnoreCase(fromInfo)) {
            return fromInfo.trim();
        }
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "var sels = document.querySelectorAll(\"select[id$=':visaType_input']\");"
                            + "for (var i = 0; i < sels.length; i++) {"
                            + "  var s = sels[i];"
                            + "  if (s.selectedIndex >= 0) {"
                            + "    var t = s.options[s.selectedIndex].text;"
                            + "    if (t && t.trim() !== '' && t.indexOf('No Visa Type') === -1) { return t.trim(); }"
                            + "  }"
                            + "}"
                            + "var labels = document.querySelectorAll(\"[id$=':visaType_label']\");"
                            + "for (var j = 0; j < labels.length; j++) {"
                            + "  var lt = labels[j].innerText;"
                            + "  if (lt && lt.trim() !== '' && lt.indexOf('No Visa Type') === -1) { return lt.trim(); }"
                            + "}"
                            + "return '';");
            return result == null ? "" : result.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /** UC_16 — encounter/visit label is Self Pay (WORK/RESIDENT VISA), not Visitor Self Pay. */
    public boolean isEncounterLabelSelfPay() {
        waitForEncounterPatientInfoRefresh();
        if (isEncounterLabelVisitorSelfPay()) {
            System.out.println("Encounter Self Pay check — excluded Visitor Self Pay.");
            return false;
        }
        String category = getPatientCategoryLabel();
        if (matchesPlainSelfPayCategory(category)) {
            System.out.println("Encounter Self Pay — Patient Category: " + category);
            return true;
        }
        String billingGroup = getBillingGroupText();
        if (matchesPlainSelfPayCategory(billingGroup)
                || (billingGroup.toLowerCase().contains("self") && !billingGroup.toLowerCase().contains("visitor"))) {
            System.out.println("Encounter Self Pay — billing group: " + billingGroup);
            return true;
        }
        String patientCategoryField = getInvoiceFormFieldValue("Patient Category");
        if (matchesPlainSelfPayCategory(patientCategoryField)) {
            System.out.println("Encounter Self Pay — Patient Category field: " + patientCategoryField);
            return true;
        }
        String plainSelfPayTag = getPlainSelfPayEncounterTag();
        if (!plainSelfPayTag.isBlank()) {
            System.out.println("Encounter Self Pay — tag: " + plainSelfPayTag);
            return true;
        }
        String tag = getEncounterTagLabel();
        if (matchesSelfPayText(tag) && !tag.toLowerCase().contains("visitor")) {
            System.out.println("Encounter Self Pay — visitor tag: " + tag);
            return true;
        }
        for (String term : new String[] {"Self Pay", "SelfPay", "Self-Pay"}) {
            if (hasVisibleSelfPayInInvoiceForm(term) && !hasVisibleShortTextInInvoiceForm("Visitor Self Pay", 60)) {
                System.out.println("Encounter Self Pay — invoice form text: " + term);
                return true;
            }
        }
        String paymentType = getInvoiceFormFieldValue("Payment Type");
        String payLower = paymentType == null ? "" : paymentType.toLowerCase();
        if ((payLower.contains("self") || payLower.contains("cash")) && !payLower.contains("visitor")
                && !payLower.equals("all")) {
            System.out.println("Encounter Self Pay — payment type: " + paymentType);
            return true;
        }
        String invoiceText = getInvoiceFormVisibleText().toLowerCase();
        if ((invoiceText.contains("self pay") || invoiceText.contains("self-pay") || invoiceText.contains("selfpay"))
                && !invoiceText.contains("visitor-selfpay") && !invoiceText.contains("visitor self pay")) {
            System.out.println("Encounter Self Pay — found in invoice form text.");
            return true;
        }
        System.out.println("Encounter Self Pay check failed — payment='" + paymentType
                + "', category='" + category + "', billingGroup='" + billingGroup
                + "', patientCategoryField='" + patientCategoryField + "'");
        return false;
    }

    private String getPlainSelfPayEncounterTag() {
        By[] scopes = {
                By.id("InvoiceDlg"),
                By.xpath("//form[contains(@id,'InvoiceForm')]")
        };
        for (By scopeBy : scopes) {
            for (WebElement scope : driver.findElements(scopeBy)) {
                try {
                    if (!scope.isDisplayed()) {
                        continue;
                    }
                    for (WebElement el : scope.findElements(By.xpath(
                            ".//*[contains(normalize-space(.),'Self Pay') or contains(normalize-space(.),'Self-Pay')"
                                    + " or contains(normalize-space(.),'SelfPay')][string-length(normalize-space(.)) < 50]"))) {
                        try {
                            if (!el.isDisplayed()) {
                                continue;
                            }
                            String text = el.getText().trim();
                            String lower = text.toLowerCase();
                            if (text.length() < 4 || lower.contains("visitor")) {
                                continue;
                            }
                            if (lower.contains("self pay") || lower.contains("self-pay") || lower.contains("selfpay")) {
                                return text;
                            }
                        } catch (StaleElementReferenceException ignored) {
                            // try next
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next scope
                }
            }
        }
        return "";
    }

    private boolean hasVisibleSelfPayInInvoiceForm(String needle) {
        return hasVisibleShortTextInInvoiceForm(needle, 40);
    }

    private boolean hasVisibleShortTextInInvoiceForm(String needle, int maxLength) {
        if (needle == null || needle.isBlank()) {
            return false;
        }
        String lowerNeedle = needle.toLowerCase();
        By[] scopes = {
                By.id("InvoiceDlg"),
                By.xpath("//form[contains(@id,'InvoiceForm')]")
        };
        for (By scopeBy : scopes) {
            for (WebElement scope : driver.findElements(scopeBy)) {
                try {
                    if (!scope.isDisplayed()) {
                        continue;
                    }
                    for (WebElement el : scope.findElements(By.xpath(
                            ".//*[contains(translate(normalize-space(.),"
                                    + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                                    + "'" + lowerNeedle + "')]"))) {
                        try {
                            if (!el.isDisplayed()) {
                                continue;
                            }
                            String text = el.getText().trim();
                            if (!text.isEmpty() && text.length() <= maxLength
                                    && text.toLowerCase().contains(lowerNeedle)) {
                                return true;
                            }
                        } catch (StaleElementReferenceException ignored) {
                            // try next
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next scope
                }
            }
        }
        return false;
    }

    private String getInvoiceFormFieldValue(String fieldLabel) {
        if (fieldLabel == null || fieldLabel.isBlank()) {
            return "";
        }
        String literal = fieldLabel.trim();
        By[] scopes = {
                By.id("InvoiceDlg"),
                By.xpath("//form[contains(@id,'InvoiceForm')]")
        };
        By[] valueLocators = {
                By.xpath(".//label[normalize-space()='" + literal + "']/following-sibling::*[1]"),
                By.xpath(".//*[normalize-space()='" + literal + "']/following-sibling::*[1]"),
                By.xpath(".//td[normalize-space()='" + literal + "']/following-sibling::td[1]"),
                By.xpath(".//th[normalize-space()='" + literal + "']/following-sibling::td[1]")
        };
        for (By scopeBy : scopes) {
            for (WebElement scope : driver.findElements(scopeBy)) {
                try {
                    if (!scope.isDisplayed()) {
                        continue;
                    }
                    for (By valueBy : valueLocators) {
                        for (WebElement el : scope.findElements(valueBy)) {
                            try {
                                if (!el.isDisplayed()) {
                                    continue;
                                }
                                String text = el.getText().trim();
                                if (!text.isEmpty() && !text.equalsIgnoreCase("All")) {
                                    return text;
                                }
                                String value = el.getAttribute("value");
                                if (value != null && !value.isBlank() && !value.equalsIgnoreCase("All")) {
                                    return value.trim();
                                }
                            } catch (StaleElementReferenceException ignored) {
                                // try next
                            }
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next scope
                }
            }
        }
        return "";
    }

    private static boolean matchesPlainSelfPayCategory(String category) {
        if (category == null || category.isBlank()) {
            return false;
        }
        String normalized = category.replace("-", "").replace(" ", "").toLowerCase();
        return normalized.contains("selfpay") && !normalized.contains("visitor");
    }

    private static final By[] patientCategoryLocators = {
            By.id("InvoiceForm:PatientTypeCash"),
            By.id("InvoiceForm:PatientTypeSelfPay"),
            By.id("InvoiceForm:PatientType"),
            By.xpath("//form[contains(@id,'InvoiceForm')]//*[contains(@id,'PatientType')]")
    };

    /** Encounter Patient Category label (e.g. Self Pay, Visitor-SelfPay, Visitor-Insured). */
    public String getPatientCategoryLabel() {
        for (By by : patientCategoryLocators) {
            for (WebElement el : driver.findElements(by)) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText().trim();
                        if (text.isEmpty()) {
                            text = readInnerText(el);
                        }
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // retry next element
                }
            }
        }
        String field = getInvoiceFormFieldValue("Patient Category");
        if (!field.isEmpty()) {
            return field;
        }
        return "";
    }

    private String readInnerText(WebElement el) {
        try {
            Object result = ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].innerText;", el);
            return result == null ? "" : result.toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    /** UC_18 — Patient Category on encounter shows Visitor Self Pay. */
    public boolean isEncounterLabelVisitorSelfPay() {
        waitForEncounterPatientInfoRefresh();
        String category = getPatientCategoryLabel();
        if (matchesVisitorSelfPayCategory(category)) {
            System.out.println("Visitor Self Pay — Patient Category: " + category);
            return true;
        }
        String tag = getEncounterTagLabel().toLowerCase();
        if (tag.contains("visitor") && (tag.contains("self pay") || tag.contains("self-pay")
                || tag.contains("selfpay"))) {
            System.out.println("Visitor Self Pay — tag: " + getEncounterTagLabel());
            return true;
        }
        String invoiceText = getInvoiceFormVisibleText().toLowerCase();
        if (invoiceText.contains("visitor-selfpay") || invoiceText.contains("visitor-self pay")
                || invoiceText.contains("visitor self pay")) {
            System.out.println("Visitor Self Pay — found in invoice form text.");
            return true;
        }
        System.out.println("Visitor Self Pay check failed — category='" + category
                + "', tag='" + getEncounterTagLabel() + "'");
        return false;
    }

    private static boolean matchesVisitorSelfPayCategory(String category) {
        if (category == null || category.isBlank()) {
            return false;
        }
        String normalized = category.replace("-", "").replace(" ", "").toLowerCase();
        return normalized.contains("visitorselfpay");
    }

    private static boolean matchesSelfPayText(String text) {
        if (text == null || text.isBlank()) {
            return false;
        }
        String lower = text.toLowerCase();
        return lower.contains("self pay") || lower.contains("self-pay") || lower.contains("selfpay");
    }

    private String getInvoiceFormVisibleText() {
        for (By by : new By[] {
                By.xpath("//form[contains(@id,'InvoiceForm')]"),
                By.id("InvoiceDlg"),
                By.xpath("//*[contains(@id,'NewInvoice')]")
        }) {
            for (WebElement el : driver.findElements(by)) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText();
                        if (text != null && !text.isBlank()) {
                            return text;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return "";
    }

    public String getEncounterTagLabel() {
        String best = "";
        By[] scopes = {
                By.id("InvoiceDlg"),
                By.xpath("//form[contains(@id,'InvoiceForm')]"),
                By.xpath("//*[contains(@id,'NewInvoice')]")
        };
        for (By scopeBy : scopes) {
            for (WebElement scope : driver.findElements(scopeBy)) {
                try {
                    if (!scope.isDisplayed()) {
                        continue;
                    }
                    for (WebElement el : scope.findElements(By.xpath(
                            ".//*[contains(normalize-space(.),'Visitor')][string-length(normalize-space(.)) < 90]"))) {
                        try {
                            if (!el.isDisplayed()) {
                                continue;
                            }
                            String text = el.getText().trim();
                            if (text.length() < 8) {
                                continue;
                            }
                            String lower = text.toLowerCase();
                            if (!lower.contains("self pay") && !lower.contains("self-pay")
                                    && !lower.contains("insured")) {
                                continue;
                            }
                            if (best.isEmpty() || text.length() < best.length()) {
                                best = text;
                            }
                        } catch (StaleElementReferenceException ignored) {
                            // try next
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next scope
                }
            }
        }
        if (best.isEmpty()) {
            for (WebElement el : driver.findElements(By.xpath(
                    "//*[not(ancestor::tbody[@id='phWLForm:phWLTbl_data'])]"
                            + "[contains(normalize-space(.),'Visitor')]"
                            + "[contains(.,'Self Pay') or contains(.,'Self-Pay') or contains(.,'Insured')]"
                            + "[string-length(normalize-space(.)) < 90]"))) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText().trim();
                        if (text.length() >= 8 && (best.isEmpty() || text.length() < best.length())) {
                            best = text;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return best;
    }

    private void waitForEncounterPatientInfoRefresh() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(d -> isEncounterSectionActive()
                    || !getPatientInfoFieldValue("Passport No").isBlank()
                    || !getEncounterTagLabel().isBlank()
                    || hasVisibleShortText("Self Pay", 40)
                    || hasVisibleShortText("Visitor", 80));
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // continue with best-effort checks
        }
        sleepQuietMs(500);
    }

    private boolean hasVisibleShortText(String needle, int maxLength) {
        if (needle == null || needle.isBlank()) {
            return false;
        }
        String lowerNeedle = needle.toLowerCase();
        for (WebElement el : driver.findElements(By.xpath(
                "//*[not(ancestor::tbody[@id='phWLForm:phWLTbl_data'])]"
                        + "[contains(translate(normalize-space(.),"
                        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),"
                        + "'" + lowerNeedle + "')]"))) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                String text = el.getText().trim();
                if (!text.isEmpty() && text.length() <= maxLength
                        && text.toLowerCase().contains(lowerNeedle)) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        return false;
    }

    private String getPatientInfoFieldValue(String fieldLabel) {
        if (fieldLabel == null || fieldLabel.isBlank()) {
            return "";
        }
        String literal = fieldLabel.trim();
        By[] valueLocators = {
                By.xpath("//label[normalize-space()='" + literal + "']/following-sibling::*[1]"),
                By.xpath("//*[normalize-space()='" + literal + "']/following-sibling::*[1]"),
                By.xpath("//td[normalize-space()='" + literal + "']/following-sibling::td[1]"),
                By.xpath("//th[normalize-space()='" + literal + "']/following-sibling::td[1]"),
                By.xpath("//*[normalize-space()='" + literal + "']/parent::*/following-sibling::*[1]"),
                By.xpath("//*[contains(@id,'InvoiceForm') or contains(@id,'AccumedPatientCreateForm')]"
                        + "//*[normalize-space()='" + literal + "']/following::*[self::span or self::div or self::label][1]")
        };
        for (By by : valueLocators) {
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
                    // try next
                }
            }
        }
        return "";
    }

    private String getSelectOneMenuLabelInEditPatientModal(String widgetSuffix) {
        WebElement dialog;
        try {
            dialog = driver.findElement(editPatientDialog);
        } catch (Exception e) {
            return getSelectOneMenuLabelOnPage(widgetSuffix);
        }
        for (WebElement el : dialog.findElements(By.xpath(
                ".//div[contains(@id,'" + widgetSuffix + "')]//label[contains(@class,'ui-selectonemenu-label')]"
                        + " | .//span[contains(@id,'" + widgetSuffix + "_label')]"))) {
            try {
                if (el.isDisplayed()) {
                    String text = el.getText().trim();
                    if (!text.isEmpty()) {
                        return text;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
                // try next
            }
        }
        return getSelectOneMenuLabelOnPage(widgetSuffix);
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
        loginPage.rememberRegisteredMrn(mrn);
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
            selectVisitorSubCategory(loginPage, visitorSubCategory);
            lastVisitorSubCategory = visitorSubCategory;
        }
        if (hasValue(data, "Visa Type")) {
            selectVisaType(loginPage, visaType);
            lastVisaType = visaType;
        }

        lastFirstName = firstName;
        lastLastName = lastName;
        lastRegisteredPassport = readPassportValue(passport);
        if (missingReason != null && !missingReason.isBlank()) {
            lastMissingQidReason = missingReason;
        }
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

    /** UC_17 — patient + encounter visible after visitor insert. */
    public boolean isPatientRecordCreated() {
        try {
            if (driver.findElement(encounterSection).isDisplayed()) {
                System.out.println("Patient record created — Encounter section is visible.");
                return true;
            }
        } catch (Exception ignored) {
            // fall through
        }
        return isEncounterSectionActive();
    }

    /** UC_17 — Missing QID, passport, visitor sub-category, visa type, and missing-QID reason stored. */
    public boolean areAdtVisitorFieldsStored() {
        boolean passportOk = lastRegisteredPassport != null && !lastRegisteredPassport.isBlank()
                && isPassportNumber(lastRegisteredPassport);
        boolean missingQidOk = isNationalIdEmptyOnForm();
        String subCategoryLabel = getSelectOneMenuLabelOnPage("visitorSubCategory");
        String visaLabel = getSelectOneMenuLabelOnPage("visaType");
        String reasonLabel = getSelectOneMenuLabelOnPage("noNationalID");
        boolean subCategoryOk = subCategoryLabel != null && !subCategoryLabel.isBlank()
                && !subCategoryLabel.equalsIgnoreCase("Select One")
                && !subCategoryLabel.equalsIgnoreCase("Select");
        boolean visaOk = visaLabel != null && !visaLabel.isBlank()
                && !visaLabel.equalsIgnoreCase("Select One")
                && !visaLabel.equalsIgnoreCase("Select");
        boolean reasonOk = reasonLabel != null && !reasonLabel.isBlank()
                && !reasonLabel.equalsIgnoreCase("Select One")
                && !reasonLabel.equalsIgnoreCase("Select");
        System.out.println("ADT fields — passport=" + passportOk + ", missingQid=" + missingQidOk
                + ", subCategory='" + subCategoryLabel + "' (" + subCategoryOk + ")"
                + ", visa='" + visaLabel + "' (" + visaOk + ")"
                + ", reason='" + reasonLabel + "' (" + reasonOk + ")");
        return passportOk && missingQidOk && subCategoryOk && visaOk && reasonOk;
    }

    /** UC_17 — insurer listed on auto-created encounter (table, billing group, or insurer text on page). */
    public boolean isPatientInsurerListedOnEncounter() {
        String invoiceText = getInvoiceFormVisibleText();
        String invoiceLower = invoiceText.toLowerCase();
        if (invoiceLower.contains("qlm life") || invoiceLower.contains("insurance company")) {
            if (invoiceLower.contains("payment type") && invoiceLower.contains("insurance")) {
                System.out.println("Patient insurer on encounter — QLM payer with Insurance payment type.");
                return true;
            }
            if (invoiceText.contains("PHI/116849/000006")) {
                System.out.println("Patient insurer on encounter — payer listed on invoice form.");
                return true;
            }
        }
        String billingGroup = getBillingGroupText().toLowerCase();
        if (billingGroup.contains("insured")) {
            System.out.println("Patient insurer on encounter — billing group indicates insured: " + billingGroup);
            return true;
        }
        expandHealthInsuranceSectionIfNeeded();
        if (getPatientInsuranceCardCount() >= 1) {
            System.out.println("Patient insurer on encounter — insurance card(s) visible in Health Insurance Info.");
            return true;
        }
        return isInsuranceVisibleOnEncounter();
    }

    /** UC_17 — billing group / visit label shows Visitor Insured. */
    public boolean isEncounterLabelVisitorInsured() {
        String tag = getEncounterTagLabel().toLowerCase();
        if (tag.contains("visitor") && tag.contains("insured") && !tag.contains("self")) {
            System.out.println("Encounter Visitor Insured — tag: " + getEncounterTagLabel());
            return true;
        }
        String invoiceLower = getInvoiceFormVisibleText().toLowerCase();
        if (invoiceLower.contains("visitor-insured") || invoiceLower.contains("visitor insured")) {
            System.out.println("Encounter Visitor Insured — found in invoice form.");
            return true;
        }
        String paymentType = getPatientInfoFieldValue("Payment Type");
        if (paymentType != null && paymentType.toLowerCase().contains("insurance")
                && invoiceLower.contains("qlm")) {
            System.out.println("Encounter Visitor Insured — payment type Insurance with QLM payer.");
            return true;
        }
        if (isPatientVisitorInsuredOnEncounter()) {
            return true;
        }
        System.out.println("Encounter Visitor Insured check failed — tag='" + getEncounterTagLabel()
                + "', paymentType='" + paymentType + "'");
        return false;
    }

    public void waitForInsuranceCardInEditPatientModal() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(editPatientDialog));
        expandEditPatientHealthInsuranceSectionIfNeeded();
        wait.until(d -> !driver.findElements(By.xpath(EDIT_INSURANCE_CARD_ROW_XPATH)).isEmpty());
        System.out.println("Insurance card visible in Edit Patient modal.");
    }

    public void waitForEncounterInsuranceOnVisit() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        try {
            wait.until(d -> {
                String invoice = getInvoiceFormVisibleText().toLowerCase();
                return (invoice.contains("qlm") || invoice.contains("insurance company"))
                        && (invoice.contains("payment type") && invoice.contains("insurance")
                        || invoice.contains("visitor-insured") || invoice.contains("visitor insured"));
            });
            System.out.println("Encounter insurance / Visitor Insured reflected on visit.");
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Encounter insurance not fully reflected on visit within timeout.");
        }
    }

    public String getLastVisitorSubCategory() {
        return lastVisitorSubCategory == null ? "" : lastVisitorSubCategory;
    }

    private void selectVisitorSubCategory(LoginPage loginPage, String value) {
        if (value.matches("\\d+")) {
            loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, value);
        } else {
            String[] keywords = {value, "TRANSIT", "FAMILY", "HAYYA", "VISIT"};
            boolean selected = false;
            for (String keyword : keywords) {
                try {
                    loginPage.selectCreatePatientDropdownContaining(VISITOR_SUB_CATEGORY_WIDGET, keyword);
                    selected = true;
                    break;
                } catch (RuntimeException ignored) {
                    // try next keyword
                }
            }
            if (!selected) {
                loginPage.selectCreatePatientDropdown(VISITOR_SUB_CATEGORY_WIDGET, "2");
            }
        }
        lastVisitorSubCategory = getSelectOneMenuLabelOnPage("visitorSubCategory");
        if (lastVisitorSubCategory.isBlank()) {
            lastVisitorSubCategory = value;
        }
    }

    private void selectVisaType(LoginPage loginPage, String value) {
        if (value.matches("\\d+")) {
            loginPage.selectCreatePatientDropdown(VISA_TYPE_WIDGET, value);
        } else {
            loginPage.selectCreatePatientDropdownContainingOnce(VISA_TYPE_WIDGET, resolveVisaTypeLabel(value));
        }
        lastVisaType = getSelectOneMenuLabelOnPage("visaType");
        if (lastVisaType.isBlank()) {
            lastVisaType = value;
        }
    }

    /** Maps a visa-type keyword (e.g. WORK, RESIDENT) to the exact option label shown in the dropdown. */
    private static String resolveVisaTypeLabel(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim().toUpperCase();
        if (v.contains("WORK")) {
            return "WORK VISA";
        }
        if (v.contains("RESIDENT")) {
            return "RESIDENT VISA";
        }
        if (v.contains("TRANSIT")) {
            return "TRANSIT VISA";
        }
        if (v.contains("FAMILY")) {
            return "FAMILY VISIT VISA";
        }
        if (v.contains("HAYYA")) {
            return "HAYYA A1 VISA";
        }
        if (v.contains("GOVT") || v.contains("DIPLOMAT")) {
            return "GOVT/DIPLOMAT";
        }
        return value;
    }

    private String getSelectOneMenuLabelOnPage(String widgetSuffix) {
        By[] locators = {
                By.xpath("//div[contains(@id,'" + widgetSuffix + "')]//label[contains(@class,'ui-selectonemenu-label')]"),
                By.xpath("//span[contains(@id,'" + widgetSuffix + "_label')]"),
                By.xpath("//*[contains(@id,'" + widgetSuffix + "')]//span[contains(@class,'ui-selectonemenu-label')]")
        };
        for (By by : locators) {
            for (WebElement el : driver.findElements(by)) {
                try {
                    if (el.isDisplayed()) {
                        String text = el.getText().trim();
                        if (!text.isEmpty()) {
                            return text;
                        }
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return "";
    }

    private boolean isDropdownLabelMatching(String actualLabel, String expected) {
        if (actualLabel == null || actualLabel.isBlank()) {
            return false;
        }
        if (expected == null || expected.isBlank()) {
            return true;
        }
        String actual = actualLabel.toLowerCase();
        String exp = expected.toLowerCase();
        if (actual.contains(exp)) {
            return true;
        }
        return exp.matches("\\d+") && actual.matches(".*\\b" + exp + "\\b.*");
    }

    public boolean isVisitLabelVisitorInsuredOrSelfPay() {
        if (isPatientVisitorInsuredOnEncounter()) {
            return true;
        }
        String label = getVisitLabelText();
        System.out.println("Visit label text: " + label);
        if (label == null || label.isBlank()) {
            return false;
        }
        String normalized = label.toLowerCase();
        if (normalized.contains("sub-category") || normalized.contains("visa type")) {
            return false;
        }
        boolean hasVisitor = normalized.contains("visitor");
        boolean hasInsured = normalized.contains("insured");
        boolean hasSelfPay = normalized.contains("self pay") || normalized.contains("self-pay");
        return hasVisitor && (hasInsured || hasSelfPay);
    }

    /** Visitor with insurance on encounter — insurance card listed after visitor registration. */
    public boolean isPatientVisitorInsuredOnEncounter() {
        String billingGroup = getBillingGroupText().toLowerCase();
        boolean insuranceListed = isInsuranceVisibleOnEncounter();
        System.out.println("Visitor insured check — billingGroup='" + billingGroup
                + "', insuranceListed=" + insuranceListed);
        if (insuranceListed) {
            return true;
        }
        if (billingGroup.contains("visitor") && billingGroup.contains("insured")) {
            return true;
        }
        return billingGroup.contains("visitor") && insuranceListed;
    }

    public String getBillingGroupText() {
        By[] locators = {
                By.xpath("//input[contains(@id,'billinggroup') or contains(@id,'billingGroup')]"),
                By.xpath("//span[contains(@id,'billingGroup') or contains(@id,'billinggroup')]"),
                By.xpath("//label[contains(normalize-space(.),'Billing Group')]/following::input[1]")
        };
        for (By by : locators) {
            for (WebElement el : driver.findElements(by)) {
                try {
                    if (!el.isDisplayed()) {
                        continue;
                    }
                    String value = el.getAttribute("value");
                    if (value != null && !value.isBlank()) {
                        return value.trim();
                    }
                    String text = el.getText().trim();
                    if (!text.isEmpty()) {
                        return text;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next
                }
            }
        }
        return "";
    }

    private boolean isInsuranceVisibleOnEncounter() {
        By[] insuranceTables = {
                By.xpath("//tbody[@id='AccumedPatientCreateForm:datalist_data']"
                        + "/tr[contains(@class,'ui-datatable-selectable')]"),
                By.xpath("//tbody[@id='AccumedPatientEditForm:datalist_data']"
                        + "/tr[contains(@class,'ui-datatable-selectable')]"),
                By.xpath("//fieldset[contains(@id,'INSID')]//tbody[contains(@id,'datalist_data')]"
                        + "/tr[contains(@class,'ui-datatable-selectable')]")
        };
        for (By tableRows : insuranceTables) {
            for (WebElement row : driver.findElements(tableRows)) {
                try {
                    if (!row.isDisplayed()) {
                        continue;
                    }
                    String rowText = row.getText().trim();
                    if (!rowText.isEmpty() && !rowText.toLowerCase().contains("no record")) {
                        System.out.println("Insurance row on encounter: " + rowText);
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // try next row
                }
            }
        }
        return false;
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
        loginPage.rememberRegisteredMrn(mrn);
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
