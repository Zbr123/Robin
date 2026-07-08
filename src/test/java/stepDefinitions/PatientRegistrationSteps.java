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
                "Visit should indicate Visitor Insured or Self Pay Visitor. Billing group: "
                        + patientRegistrationPage.getBillingGroupText());
    }

    @Then("the patient should be classified as visitor insured")
    public void thePatientShouldBeClassifiedAsVisitorInsured() {
        Assert.assertTrue(patientRegistrationPage.isPatientVisitorInsuredOnEncounter(),
                "Patient should be classified as visitor insured. Billing group: "
                        + patientRegistrationPage.getBillingGroupText());
    }

    @Then("the patient record should be created successfully")
    public void thePatientRecordShouldBeCreatedSuccessfully() {
        Assert.assertTrue(patientRegistrationPage.isPatientRecordCreated(),
                "Patient record should be created and encounter screen should be available.");
    }

    @And("the ADT visitor fields should be stored on the patient")
    public void theAdtVisitorFieldsShouldBeStoredOnThePatient() {
        Assert.assertTrue(patientRegistrationPage.areAdtVisitorFieldsStored(),
                "Missing QID, passport, visitor sub-category, visa type, and reason for missing QID should be stored.");
    }

    @Then("the visa type {string} should be displayed on the encounter page")
    public void theVisaTypeShouldBeDisplayedOnTheEncounterPage(String expectedVisaType) {
        Assert.assertTrue(patientRegistrationPage.isVisaTypeDisplayedOnEncounter(expectedVisaType),
                "Visa type '" + expectedVisaType + "' should be displayed on the encounter page.");
    }

    @And("the patient insurer should be listed on the encounter")
    public void thePatientInsurerShouldBeListedOnTheEncounter() {
        Assert.assertTrue(patientRegistrationPage.isPatientInsurerListedOnEncounter(),
                "Patient insurer should be listed on the auto-created encounter.");
    }

    @And("the encounter label should be Visitor Insured")
    public void theEncounterLabelShouldBeVisitorInsured() {
        Assert.assertTrue(patientRegistrationPage.isEncounterLabelVisitorInsured(),
                "Encounter label should be Visitor Insured. Billing group: "
                        + patientRegistrationPage.getBillingGroupText()
                        + ", visit label: " + patientRegistrationPage.getVisitLabelText());
    }

    @And("the encounter label should be Self Pay")
    public void theEncounterLabelShouldBeSelfPay() {
        Assert.assertTrue(patientRegistrationPage.isEncounterLabelSelfPay(),
                "Encounter label should indicate Self Pay. Payment type / tag: "
                        + patientRegistrationPage.getEncounterTagLabel());
    }

    @And("the encounter label should be Visitor Self Pay")
    public void theEncounterLabelShouldBeVisitorSelfPay() {
        Assert.assertTrue(patientRegistrationPage.isEncounterLabelVisitorSelfPay(),
                "Encounter label should be Visitor Self Pay. Tag: "
                        + patientRegistrationPage.getEncounterTagLabel());
    }

    @And("I update the visa type in Edit Patient modal to {string}")
    public void iUpdateTheVisaTypeInEditPatientModalTo(String newVisaType) {
        patientRegistrationPage.updateVisaTypeInEditPatientModal(newVisaType, loginPage);
    }

    @And("I click on Add Insurance Card button in Edit Patient modal")
    public void iClickOnAddInsuranceCardButtonInEditPatientModal() {
        patientRegistrationPage.clickAddInsuranceCardInEditPatientModal();
    }

    @Then("the visa type on the patient should be updated")
    public void theVisaTypeOnThePatientShouldBeUpdated() {
        Assert.assertTrue(patientRegistrationPage.isVisaTypeUpdatedOnPatient(),
                "Visa type should reflect the correction saved from Edit Patient.");
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

    @When("I reopen the created visit from Patient Access")
    public void iReopenTheCreatedVisitFromPatientAccess() {
        loginPage.closeVisitDialogIfOpen();
        String mrn = loginPage.getLastRegisteredMrn();
        loginPage.returnToPatientAccessVisitList();
        loginPage.searchVisitInPatientAccess(mrn);
        loginPage.openVisitFromPatientAccessListByMrn(mrn);
        System.out.println("Reopened created visit from Patient Access for MRN: " + mrn);
    }

    @When("I close the encounter tab without saving the visit")
    public void iCloseTheEncounterTabWithoutSavingTheVisit() {
        loginPage.closeEncounterTabWithoutSavingVisit();
    }

    @And("I search for the created patient visit in Patient Access")
    public void iSearchForTheCreatedPatientVisitInPatientAccess() {
        loginPage.returnToPatientAccessVisitList();
        String mrn = loginPage.getLastRegisteredMrn();
        loginPage.searchVisitInPatientAccess(mrn);
    }

    @Then("no visit should be found for the created patient")
    public void noVisitShouldBeFoundForTheCreatedPatient() {
        String mrn = loginPage.getLastRegisteredMrn();
        Assert.assertTrue(loginPage.isVisitAbsentFromPatientAccessList(mrn),
                "No visit should exist in Patient Access for MRN: " + mrn);
    }

    @And("I save the Edit Patient modal after adding insurance")
    public void iSaveEditPatientModalAfterAddingInsurance() {
        patientRegistrationPage.waitForInsuranceCardInEditPatientModal();
        patientRegistrationPage.saveEditPatientModal();
    }

    @When("I click on edit insurance card row {int} in Edit Patient modal")
    public void iClickOnEditInsuranceCardRowInEditPatientModal(int rowIndex) {
        patientRegistrationPage.clickEditInsuranceCardInEditPatientModal(rowIndex);
    }

    @And("I update the insurance Member ID in Edit Insurance modal to a new value")
    public void iUpdateInsuranceMemberIdInEditInsuranceModalToNewValue() {
        patientRegistrationPage.updateMemberIdInEditInsuranceModal("auto", loginPage);
    }

    @And("I save the Edit Insurance modal")
    public void iSaveEditInsuranceModal() {
        patientRegistrationPage.saveEditInsuranceModal();
    }

    @Then("the insurance Member ID in Edit Patient insurance list row {int} should match the registered value")
    public void theInsuranceMemberIdInEditPatientListRowShouldMatch(int rowIndex) {
        Assert.assertTrue(
                patientRegistrationPage.isMemberIdListedInEditPatientInsuranceRow(
                        rowIndex, loginPage.getLastRegisteredMemberId()),
                "Member ID in Edit Patient insurance list row " + rowIndex
                        + " should match: " + loginPage.getLastRegisteredMemberId());
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
