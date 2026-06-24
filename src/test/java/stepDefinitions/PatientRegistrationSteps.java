package stepDefinitions;

import Pages.LoginPage;
import Pages.PatientRegistrationPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.DriverManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class PatientRegistrationSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private PatientRegistrationPage patientRegistrationPage;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        patientRegistrationPage = new PatientRegistrationPage(driver);
    }

    @When("I fill visitor patient registration form with the following data")
    public void iFillVisitorPatientRegistrationForm(DataTable dataTable) {
        patientRegistrationPage.fillVisitorPatientForm(toMap(dataTable), loginPage);
        System.out.println("Filled visitor patient registration form.");
    }

    @When("I fill visitor patient registration form without QID and missing reason")
    public void iFillVisitorPatientFormWithoutQidAndReason(DataTable dataTable) {
        patientRegistrationPage.fillVisitorPatientFormWithoutQidAndReason(toMap(dataTable), loginPage);
    }

    @When("I fill visitor patient registration form with no QID and no passport")
    public void iFillVisitorPatientFormWithNoQidAndNoPassport(DataTable dataTable) {
        patientRegistrationPage.fillVisitorPatientFormWithNoQidAndNoPassport(toMap(dataTable), loginPage);
        System.out.println("Filled visitor form with No QID reason and no passport.");
    }

    @When("I uncheck No QID and enter QID details with the following data")
    public void iUncheckNoQidAndEnterQidDetails(DataTable dataTable) {
        patientRegistrationPage.uncheckNoQidAndFillQidDetails(toMap(dataTable), loginPage);
    }

    @When("I start a new patient visit from Patient Access")
    public void iStartNewPatientVisitFromPatientAccess() {
        loginPage.clickPatientAccess();
        loginPage.clickNewVisitButton();
        System.out.println("Started new patient visit from Patient Access.");
    }

    @When("I fill baby visitor patient registration form with the following data")
    public void iFillBabyVisitorPatientRegistrationForm(DataTable dataTable) {
        patientRegistrationPage.fillBabyVisitorPatientForm(toMap(dataTable), loginPage);
        System.out.println("Filled baby visitor patient registration form.");
    }

    @And("I fill child insurance with relationship {word} linked to the created mother patient")
    public void iFillChildInsuranceLinkedToMother(String relationship) {
        loginPage.fillChildInsuranceLinkedToExistingPatient(
                relationship, patientRegistrationPage.getLastPatientFullName());
    }

    @Then("the Add Insurance modal should be displayed")
    public void theAddInsuranceModalShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isAddInsuranceModalDisplayed(), "Add Insurance modal should be displayed.");
    }

    @Then("the visit label should be visitor insured or self pay visitor")
    public void theVisitLabelShouldBeVisitorInsuredOrSelfPayVisitor() {
        Assert.assertTrue(patientRegistrationPage.isVisitLabelVisitorInsuredOrSelfPay(),
                "Visit label should indicate Visitor Insured or Self Pay Visitor. Actual: "
                        + patientRegistrationPage.getVisitLabelText());
    }

    @Then("the patient should have {int} insurance card on the create patient form")
    public void thePatientShouldHaveInsuranceCardOnCreatePatientForm(int expectedCount) {
        patientRegistrationPage.waitForInsuranceCardCount(expectedCount);
        Assert.assertEquals(patientRegistrationPage.getPatientInsuranceCardCount(), expectedCount,
                "Expected " + expectedCount + " insurance card(s) in Health Insurance Info.");
    }

    @Then("the Health Insurance Info section should contain insurance for {string}")
    public void theHealthInsuranceInfoSectionShouldContainInsuranceFor(String insuranceName) {
        Assert.assertTrue(patientRegistrationPage.isHealthInsuranceSectionDisplayed(),
                "Health Insurance Info section should be visible.");
        Assert.assertTrue(patientRegistrationPage.isInsuranceCardListedContaining(insuranceName),
                "Health Insurance Info should list insurance containing: " + insuranceName);
    }

    @Then("Insert Patient should be blocked by validation")
    public void insertPatientShouldBeBlockedByValidation() {
        patientRegistrationPage.clickInsertPatientOnly();
        Assert.assertTrue(patientRegistrationPage.isPatientInsertBlocked(),
                "Expected validation error — patient must not be added without QID reason.");
    }

    @Then("Insert Patient should be blocked without reaching Encounter Section")
    public void insertPatientShouldBeBlockedWithoutEncounter() {
        patientRegistrationPage.clickInsertPatientOnly();
        Assert.assertTrue(patientRegistrationPage.isPatientInsertBlocked(),
                "Expected insert to be blocked when No QID is checked without passport.");
        Assert.assertTrue(patientRegistrationPage.isInsertPatientButtonStillAvailable(),
                "Create Patient screen should remain — Insert Patient must still be available.");
    }

    @When("I click on Edit Patient button")
    public void iClickOnEditPatientButton() {
        patientRegistrationPage.clickEditPatientButton();
    }

    @And("I update the patient passport number in Edit Patient modal to a new value")
    public void iUpdatePassportInEditPatientModal() {
        patientRegistrationPage.updatePassportInEditPatientModal("auto");
    }

    @And("I save the Edit Patient modal")
    public void iSaveEditPatientModal() {
        patientRegistrationPage.saveEditPatientModal();
    }

    @Then("the patient passport on the form should match the registered value")
    public void thePatientPassportOnFormShouldMatch() {
        Assert.assertTrue(
                patientRegistrationPage.isPassportNumber(patientRegistrationPage.getLastRegisteredPassport()),
                "Passport on form should match: " + patientRegistrationPage.getLastRegisteredPassport());
    }

    @And("I uncheck No QID and enter National ID details in Edit Patient modal with the following data")
    public void iUncheckNoQidAndEnterNationalIdInEditPatientModal(DataTable dataTable) {
        patientRegistrationPage.uncheckNoQidAndFillNationalIdInEditPatientModal(toMap(dataTable));
    }

    @Then("the patient should have no National ID saved on the form")
    public void thePatientShouldHaveNoNationalIdSavedOnForm() {
        Assert.assertTrue(patientRegistrationPage.isNationalIdEmptyOnForm(),
                "National ID should be empty when patient was registered with No QID.");
    }

    @Then("the patient National ID on the form should match the registered value")
    public void thePatientNationalIdOnFormShouldMatch() {
        Assert.assertTrue(
                patientRegistrationPage.isNationalIdOnForm(patientRegistrationPage.getLastRegisteredNationalId()),
                "National ID on form should match: " + patientRegistrationPage.getLastRegisteredNationalId());
    }

    private static LinkedHashMap<String, String> toMap(DataTable dataTable) {
        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        for (Map<String, String> row : dataTable.asMaps(String.class, String.class)) {
            String field = row.get("Field");
            String value = row.get("Value");
            if (field != null && value != null) {
                data.put(field.trim(), value.trim());
            }
        }
        return data;
    }
}
