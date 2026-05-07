package stepDefinitions;

import io.cucumber.java.en.*;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import Pages.LoginPage;
import utils.DriverManager;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.testng.Assert;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import java.util.Map;
import java.util.LinkedHashMap;
import org.openqa.selenium.JavascriptExecutor;


public class LoginSteps {
    WebDriver driver;
    LoginPage loginPage;

    @Before
    public void setUp() {
        System.out.println("Setting up WebDriver...");
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
    }


    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        String url = "https://auto-robin-qtr.santechture.com/ROBIN";

        System.out.println("Opening URL: " + url);
        driver.get(url);

        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getLoginHeaderLocator()));

        // Assert the login header is displayed
        Assert.assertTrue(loginPage.isLoginHeaderDisplayed(), "Login header is NOT displayed!");
        System.out.println("Login page loaded successfully.");
    }

    @When("I enter username and password")
    public void enterUsernameAndPassword() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername("QFacilityAdmin");
        loginPage.enterPassword("QFacilityAdmin123");
    }

    @And("I click on signin button")
    public void iClickOnSignInButton() {
        loginPage.clickSignIn();
        System.out.println("Clicked on the Sign In button.");
    }

    @And("I click on Patient Access")
    public void iClickOnPatientAccess() {
        loginPage.clickPatientAccess();
        System.out.println("Clicked on the Patient Access button.");

        Assert.assertTrue(loginPage.isPatientAccessScreenDisplayed(), "Patient Access screen is not displayed.");
        System.out.println("Verified that Patient Journey Management - Patient Access screen is displayed.");

    }

    @And("I click on New visit button")
    public void iClickOnNewVisitButton() {
        loginPage.clickNewVisitButton();
        System.out.println("Clicked on the New Visit button.");

        // Wait for "Patient and Encounter" screen to be visible before assertion
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getPatientAndEncounterScreenLocator()));

        // Assertion to verify "Patient and Encounter" screen is displayed
        Assert.assertTrue(loginPage.isPatientAndEncounterScreenDisplayed(), "Patient and Encounter screen is not displayed.");
        System.out.println("Verified that 'Patient and Encounter' screen is displayed.");
    }

    @And("I click on add Patient button")
    public void iClickOnAddPatientButton() {
        loginPage.clickAddPatientButton();
        System.out.println("Clicked on the Add Patient button.");

        // Wait for "Create Patient" screen to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getCreatePatientScreenLocator()));

        // Assertion to verify "Create Patient" screen is displayed
        Assert.assertTrue(loginPage.isCreatePatientScreenDisplayed(), "Create Patient screen is not displayed.");
        System.out.println("Verified that 'Create Patient' screen is displayed.");
    }

//    @And("I fill data in patient form")
//    public void iFillDataInPatientForm(DataTable dataTable) {
//        Map<String, String> data = dataTable.asMap(String.class, String.class);
//
//        // **Calling the method from `PatientFormPage` to fill the form**
//        loginPage.fillPatientForm(
//                data.get("MRN"),
//                data.get("CommunityMRN"),
//                data.get("NationalID"),
//                data.get("FirstName"),
//                data.get("LastName"),
//                data.get("DOB")
//        );
//
//        loginPage.selectGender();
//        System.out.println("Filled the Patient Form successfully.");
//    }

    @And("I fill data in patient form")
    public void iFillDataInPatientForm() {
        loginPage.fillPatientFormWithRandomData();
        loginPage.selectNationalityAmerican();
        loginPage.selectGender();
    }


    @And("I click on Add Insurance Card button")
    public void iClickOnAddInsuranceCardButton() {
        loginPage.clickAddInsuranceCard();
        System.out.println("Clicked on 'Add Insurance Card' button.");
    }

    @And("I fill mandatory insurance details in Add Insurance modal")
    public void iFillMandatoryInsuranceDetailsInAddInsuranceModal(DataTable dataTable) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String field = row.get("Field");
            String value = row.get("Value");
            if (field != null && value != null) {
                data.put(field.trim(), value.trim());
            }
        }
        loginPage.fillAddInsuranceModalMandatoryFields(data);
        System.out.println("Filled mandatory Add Insurance modal fields.");
    }

    @And("I click on Save button")
    public void iClickOnSaveButton() {
        loginPage.clickSaveInsuranceCard();
        System.out.println("Clicked on the Save button.");
        try {
            Thread.sleep(3000);  // 3-second pause
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("I click on Insert Patient button")
    public void iClickOnInsertPatientButton() {
        loginPage.clickInsertPatientButton();
    }

    @Then("I should see Encounter Section")
    public void iShouldSeeEncounterSection() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginPage.getEncounterSectionLocator()));

        Assert.assertTrue(loginPage.isEncounterSectionVisible(), "Encounter section is not visible");
        System.out.println("Verified that the Encounter Section is visible.");
    }

    @When("I fill mandatory encounter details")
    public void iFillMandatoryEncounterDetails(DataTable dataTable) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String field = row.get("Field");
            String value = row.get("Value");
            if (field != null && value != null) {
                data.put(field.trim(), value.trim());
            }
        }
        loginPage.fillMandatoryEncounterFields(data);
        System.out.println("Filled mandatory Encounter section fields.");
    }

    @And("I click on Diagnosis and Interventions")
    public void iClickOnDiagnosisAndInterventions() {
        loginPage.clickDiagnosisAndInterventions();
        System.out.println("Clicked on 'Diagnosis and Interventions'.");
    }

    @Then("I should see Add bulk Diagnosis button")
    public void iShouldSeeAddBulkDiagnosisButton() {
        boolean isDisplayed = loginPage.isAddBulkDiagnosisButtonDisplayed();
        Assert.assertTrue(isDisplayed, "Add Bulk Diagnosis button is not displayed.");
        System.out.println("Verified that 'Add Bulk Diagnosis' button is displayed.");
    }

    @When("I select Activity Type as CPT")
    public void iSelectActivityTypeCPT() {
        loginPage.selectActivityTypeCPT();
        System.out.println("Selected 'CPT' from Activity Type dropdown.");
    }

    @And("I enter Code as 70030")
    public void iEnterCode70030() {
        loginPage.enterCode70030();
        System.out.println("Entered '70030' as the code.");
    }

    @And("I click on Add Service button")
    public void iClickOnAddServiceButton() {
        loginPage.clickAddServiceButton();
        System.out.println("Clicked on 'Add Service' button.");
    }

    @Then("I should see service as added")
    public void iShouldSeeTheTextCompleted() {
        boolean isDisplayed = loginPage.isCompletedTextDisplayed();
        Assert.assertTrue(isDisplayed, "'Completed' text is not displayed.");
        System.out.println("Verified that 'Completed' text is displayed.");
    }

    @When("I click on Insert visit button")
    public void iClickOnInsertVisitButton() {
        loginPage.clickInsertVisitButton();
        System.out.println("Clicked on Insert Visit button.");
    }

    @When("I click on Mark As Ready To Bill button")
    public void iClickOnMarkAsReadyToBillButton() {
        loginPage.clickMarkAsReadyToBillButton();
        System.out.println("Clicked on Mark As Ready To Bill button.");
    }

    @Then("I should see success message")
    public void iShouldSeeSuccessMessage() {
        loginPage.verifySuccessMessage();
        System.out.println("Verified success message 'Marked As Ready To Bill Successfully'.");
        loginPage.clickOkButton();
        System.out.println("Clicked on OK button.");
    }

    @When("I navigate to Billing-OP Receipts page")
    public void iNavigateToBillingOpReceiptsPage() {
        loginPage.navigateToBillingOpReceiptsPage();
        System.out.println("Navigated to Billing-OP Receipts page.");
    }

    @When("I click on created visit")
    public void iClickOnCreatedVisit() {
        // Click the created visit using the generated first and last name
        loginPage.clickCreatedVisit();
    }

    @And("I select payment type cash")
    public void iSelectPaymentTypeCash() {
        loginPage.selectPaymentTypeCash(); // no params
    }

    @And("I type 10000 amount in Payment Amount field")
    public void i_type_10000_amount_in_payment_amount_field() {
        loginPage.enterPaymentAmount10000(); // no params
    }
    @And("I select payment type Cheque")
    public void i_select_payment_type_cheque() {
        loginPage.selectPaymentTypeCheque(); // no params
    }

    @And("I type 0 amount in Payment Amount field")
    public void i_type_0_amount_in_payment_amount_field() {
        loginPage.enterPaymentAmount0(); // no params
    }

    @And("I click on Generate Receipt Button")
    public void i_click_on_generate_receipt_button() {
        loginPage.clickGenerateReceiptButton();
        System.out.println("Clicked on 'Generate Receipt & Invoice' button.");
    }


    @Then("I should see Receipt generated success message")
    public void i_should_see_success_message() {
        loginPage.verifyReceiptSuccessMessage();
        System.out.println("Verified success message: 'Receipt generated Successfully'.");
        loginPage.clickOkButton();
        System.out.println("Clicked on OK button.");
    }
}
