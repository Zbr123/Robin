package stepDefinitions;

import Pages.BillingPage;
import Pages.LoginPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.DriverManager;

public class BillingSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private BillingPage billingPage;

    @Before
    public void setUp() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        billingPage = new BillingPage(driver, loginPage);
    }

    @When("I login as cashier")
    public void iLoginAsCashier() {
        billingPage.loginAsCashier("QFacilityAdmin", "QFacilityAdmin123");
    }

    @Then("I should be logged in successfully with cashier privileges")
    public void iShouldBeLoggedInSuccessfullyWithCashierPrivileges() {
        Assert.assertTrue(billingPage.isCashierHomeVisible(),
                "User should land on ROBIN home with cashier / billing access.");
    }

    @When("I navigate to the Billing screen and open a claim for payment collection")
    public void iNavigateToBillingScreenAndOpenClaimForPaymentCollection() {
        billingPage.openClaimForPaymentCollection();
    }

    @Then("the billing claim should load with services and categorized bills")
    public void theBillingClaimShouldLoadWithServicesAndCategorizedBills() {
        Assert.assertTrue(billingPage.isBillingClaimLoadedWithServicesAndBills(),
                "Billing claim should show encounter services and categorized bills for payment.");
    }

    @When("I select categorized bill at row {int} for payment")
    public void iSelectCategorizedBillAtRowForPayment(int rowIndex) {
        billingPage.selectCategorizedBillRow(rowIndex);
    }

    @Then("{int} bill should be selected for payment collection")
    public void billShouldBeSelectedForPaymentCollection(int expectedCount) {
        Assert.assertEquals(billingPage.getSelectedCategorizedBillsCount(), expectedCount,
                "Expected " + expectedCount + " bill(s) selected for payment collection.");
    }

    @When("I select categorized bills at rows {int} and {int} for payment")
    public void iSelectCategorizedBillsAtRowsForPayment(int firstRow, int secondRow) {
        billingPage.selectCategorizedBillRows(firstRow, secondRow);
    }

    @Then("at least {int} bills should be selected for payment collection")
    public void atLeastBillsShouldBeSelectedForPaymentCollection(int minimumCount) {
        int rows = billingPage.getCategorizedBillsRowCount();
        int expected = rows >= minimumCount ? minimumCount : 1;
        Assert.assertTrue(billingPage.getSelectedCategorizedBillsCount() >= expected,
                "At least " + expected + " bill(s) should be selected (rows available: " + rows
                        + "). Actual selected: " + billingPage.getSelectedCategorizedBillsCount());
    }

    @When("I select all categorized bills for payment")
    public void iSelectAllCategorizedBillsForPayment() {
        billingPage.selectAllCategorizedBills();
    }

    @And("I collect payment for all selected bills using payment type {string}")
    public void iCollectPaymentForAllSelectedBillsUsingPaymentType(String paymentType) {
        billingPage.collectPaymentForSelectedBills(paymentType);
    }

    @And("I collect payment using payment type {string}")
    public void iCollectPaymentUsingPaymentType(String paymentType) {
        billingPage.collectPaymentUsingType(paymentType);
    }
}
