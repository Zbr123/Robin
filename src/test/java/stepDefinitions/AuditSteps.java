package stepDefinitions;

import Pages.AuditPage;
import Pages.LoginPage;
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

public class AuditSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private AuditPage auditPage;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        auditPage = new AuditPage(driver, loginPage);
    }

    @When("I return to Patient Access and search for the created visit")
    public void iReturnToPatientAccessAndSearchForTheCreatedVisit() {
        auditPage.returnToPatientAccessAndSearchCreatedVisit();
    }

    @When("I open the created visit from Patient Access list")
    public void iOpenTheCreatedVisitFromPatientAccessList() {
        auditPage.openCreatedVisitFromPatientAccessList();
    }

    @Then("the visit should show Ready to Bill in Patient Access list")
    public void theVisitShouldShowReadyToBillInPatientAccessList() {
        String mrn = loginPage.getLastRegisteredMrn();
        Assert.assertTrue(auditPage.isVisitReadyToBillInPatientAccessList(mrn),
                "Visit for MRN " + mrn + " should show Ready to Bill in Patient Access list.");
    }

    @Then("the visit should be opened and marked as Ready to Bill")
    public void theVisitShouldBeOpenedAndMarkedAsReadyToBill() {
        Assert.assertTrue(auditPage.isVisitOpenedAndMarkedReadyToBill(),
                "Visit should be open on Patient Access and marked as Ready to Bill.");
    }

    @When("I modify the claim by editing a service from Patient Access")
    public void iModifyTheClaimByEditingServiceFromPatientAccess(DataTable dataTable) {
        auditPage.modifyClaimByEditingService(toMap(dataTable));
    }

    @And("I remember the added service code {string} for audit verification")
    public void iRememberTheAddedServiceCodeForAuditVerification(String serviceCode) {
        auditPage.rememberServiceCode(serviceCode);
    }

    @Then("the claim modification should succeed without errors")
    public void theClaimModificationShouldSucceedWithoutErrors() {
        Assert.assertTrue(auditPage.isClaimModificationSuccessful(),
                "Claim should be modifiable while Ready to Bill — update must succeed without errors.");
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
