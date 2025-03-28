package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Random;
import org.openqa.selenium.Keys;

import java.util.UUID;


public class LoginPage {
    WebDriver driver;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    private String randomFirstName;
    private String randomLastName;

    // Locators
    private By loginHeader = By.xpath("//h1[contains(text(),'Sign in to your account')]");
    private By usernameField = By.xpath("//input[@id='username']");
    private By passwordField = By.xpath("//input[@id='password']");
    private By signInButton = By.xpath("//input[@value='Sign In']");
    private By patientAccess = By.xpath("//h4[text()='Patient Access']");
    private By patientAccessScreen = By.xpath("//nav//a[span[text()='Patient Access']]");
    private By newVisitButton = By.xpath("//span[text()='New Visit']");
    private By patientAndEncounterScreen = By.xpath("//a[contains(text(),'Patient and Encounter')]");
    private By addPatientButton = By.xpath("//button[@type='submit' and contains(@class, 'ui-button-success')]");
    private By createPatientScreen = By.xpath("//span[text()='Create Patient']");
//    private By mrnField = By.xpath("//input[@name='AccumedPatientCreateForm:mrn']");
//    private By communityMrnField = By.xpath("//input[@id='AccumedPatientCreateForm:communityMrn']");
//    private By nationalIdField = By.xpath("//input[@id='AccumedPatientCreateForm:emiratesId1']");
//    private By firstNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientName']");
//    private By lastNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientSurname']");
    private By mrnField = By.xpath("//input[@name='AccumedPatientCreateForm:mrn']");
    private By communityMrnField = By.xpath("//input[@id='AccumedPatientCreateForm:communityMrn']");
    private By nationalIdField = By.xpath("//input[@id='AccumedPatientCreateForm:emiratesId1']");
    private By firstNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientName']");
    private By lastNameField = By.xpath("//input[@id='AccumedPatientCreateForm:patientSurname']");

    private By dobField = By.xpath("//input[@id='AccumedPatientCreateForm:dateOfBirth2_input']");
    private By genderDropdown = By.xpath("//div[@id='AccumedPatientCreateForm:genderId']//span[contains(@class, 'ui-icon-triangle-1-s')]");
    private By genderOption = By.xpath("//li[text()='Male']");
    private By addInsuranceCardButton = By.xpath("//span[text()='Add Insurance Card']");
    private By cardNumberField = By.xpath("//input[@id='AccumedPatientInsuranceCreateForm:patientInsuranceId']");
    private By receiverDropdown = By.xpath("//div[@id='AccumedPatientInsuranceCreateForm:insuranceLisence']");
    private By receiverOption = By.xpath("//li[text()='INS026 | NATIONAL HEALTH INSURANCE COMPANY (DAMAN)']");
    private By startDateField = By.xpath("//input[@id='AccumedPatientInsuranceCreateForm:startDate_input']");
    private By endDateField = By.xpath("//input[@id='AccumedPatientInsuranceCreateForm:endDate_input']");
    private By verifyDropdown = By.xpath("//div[@id='AccumedPatientInsuranceCreateForm:isver']");
    private By verifyTrueOption = By.xpath("//li[@data-label='True']");
    private By networkNameDropdown = By.xpath("//div[@id='AccumedPatientInsuranceCreateForm:networkId']");
    private By networkNameOption = By.xpath("//li[@data-label='Network 1']");
    private By saveInsuranceCardButton = By.xpath("//button[@id='AccumedPatientInsuranceCreateForm:save']");
    private By insertPatientButton = By.xpath("//button[@id='AccumedPatientCreateForm:insertNewPatient']");
    private By encounterSection = By.xpath("//legend[normalize-space()='Encounter']");
    private By encounterTypeDropdown = By.xpath("//label[@id='InvoiceForm:icd_label']");
    private By encounterTypeHomeOption = By.xpath("//li[text()='Home']");
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
    private By code70030Option = By.xpath("//td[text()='70030']");
    private By addServiceButton = By.xpath("//button[@id='AccumedHaadActivityListForm:addServiceBTN']");
    private By completedText = By.xpath("//td[text()='Completed']");
    private By insertVisitButton = By.xpath("//button[span[text()='Insert Visit']]");
    private By markAsReadyToBillButton = By.xpath("//button[span[text()='Mark As Ready To Bill']]");
    private By successMessage = By.xpath("//span[text()='Marked As Ready To Bill Successfully']");
    private By okButton = By.xpath("//span[text()='OK']");
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
        driver.findElement(patientAccess).click();
    }

    // Method to verify Patient Access screen
    public boolean isPatientAccessScreenDisplayed() {
        return driver.findElement(patientAccessScreen).isDisplayed();
    }

    // Method to click on New Visit button
    public void clickNewVisitButton() {
        driver.findElement(newVisitButton).click();
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
        driver.findElement(communityMrnField).sendKeys(randomCommunityMRN);
        driver.findElement(nationalIdField).sendKeys(randomNationalID);
        driver.findElement(firstNameField).sendKeys(randomFirstName);
        driver.findElement(lastNameField).sendKeys(randomLastName);
        driver.findElement(dobField).sendKeys(hardcodedDOB);

        System.out.println("Filled Patient Form with Random Data:");
        System.out.println("MRN: " + randomMRN);
        System.out.println("Community MRN: " + randomCommunityMRN);
        System.out.println("National ID: " + randomNationalID);
        System.out.println("DOB: " + hardcodedDOB);  // Always the same
        System.out.println("First Name: " + randomFirstName);
        System.out.println("Last Name: " + randomLastName);

        this.randomFirstName = randomFirstName;
        this.randomLastName = randomLastName;
    }
    public String getRandomFirstName() {
        return randomFirstName;
    }

    public String getRandomLastName() {
        return randomLastName;
    }


    public void selectGender() {
        driver.findElement(genderDropdown).click();
        driver.findElement(genderOption).click();
    }

    // **Method to Click "Add Insurance Card"**
    public void clickAddInsuranceCard() {
        WebElement button = waitForElementToBeClickable(addInsuranceCardButton);
        scrollToElement(button);
        button.click();
    }

    // **Method to Fill Insurance Card Details**
    public void fillInsuranceCardDetails(String cardNumber, String startDate, String endDate) {
        WebElement cardField = waitForElementToBeVisible(cardNumberField);
        scrollToElement(cardField);
        cardField.sendKeys(cardNumber);

        WebElement startDateInput = waitForElementToBeVisible(startDateField);
        scrollToElement(startDateInput);
        startDateInput.sendKeys(startDate);

        WebElement endDateInput = waitForElementToBeVisible(endDateField);
        scrollToElement(endDateInput);
        endDateInput.sendKeys(endDate);
    }

    // **Method to Select Receiver**
    public void selectReceiver() {
        WebElement dropdown = waitForElementToBeClickable(receiverDropdown);
        scrollToElement(dropdown);
        dropdown.click();

        WebElement option = waitForElementToBeClickable(receiverOption);
        option.click();
    }

    // **Method to Select Verify Option**
    public void selectVerifyTrue() {
        WebElement dropdown = waitForElementToBeClickable(verifyDropdown);
        scrollToElement(dropdown);
        dropdown.click();

        WebElement option = waitForElementToBeClickable(verifyTrueOption);
        option.click();
    }

    public void selectNetworkName() {
        driver.findElement(networkNameDropdown).click(); // Click to open the dropdown
        driver.findElement(networkNameOption).click(); // Click the option "Network 1"
    }
    public void clickSaveInsuranceCard() {
        driver.findElement(saveInsuranceCardButton).click();
    }

    public void clickInsertPatientButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.elementToBeClickable(insertPatientButton)).click();
        System.out.println("Clicked on Insert Patient button.");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isEncounterSectionVisible() {
        return driver.findElement(encounterSection).isDisplayed();
    }

    public By getEncounterSectionLocator() {
        return encounterSection;
    }

    public void selectEncounterTypeHome() {
        // Click the dropdown icon (not just the div)
        WebElement dropdownIcon = driver.findElement(encounterTypeDropdown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownIcon);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(ExpectedConditions.visibilityOfElementLocated(encounterTypeHomeOption));
        driver.findElement(encounterTypeHomeOption).click();
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
        driver.findElement(diagnosisAndInterventionsLink).click();
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
        driver.findElement(addServiceButton).click();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isCompletedTextDisplayed() {
        return driver.findElement(completedText).isDisplayed();
    }

    public void clickInsertVisitButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(insertVisitButton));
        driver.findElement(insertVisitButton).click();
    }

    public void clickMarkAsReadyToBillButton() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(markAsReadyToBillButton));
        driver.findElement(markAsReadyToBillButton).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void verifySuccessMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
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





