@UC_16 @Billing
Feature: UC_16 Paymode and Payment Collection

  # Run:  mvn test "-Dtest=BillingTestRunner" "-Dheaded=true"
  # Run TC02: mvn test "-Dtest=BillingTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@UC_16 and @TestCase02 and @UI"

  Background:
    Given I am on the login page
    When I login as cashier
    And I click on signin button
    Then I should be logged in successfully with cashier privileges

  @UC_16 @TestCase02 @UI
  Scenario: Cashier can select one or multiple bills and collect payment with one paymode
    # Prerequisite — create Ready to Bill claim with billable services
    And I click on Patient Access
    And I click on New visit button
    And I click on add Patient button
    And I fill data in patient form
    And I click on Add Insurance Card button
    And I fill mandatory insurance details in Add Insurance modal
      | Field             | Value                                                     |
      | Member ID         | auto                                                      |
      | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
      | Network Name      | QLM Network                                               |
      | Plan              | QLM Plan                                                  |
      | Policy Number     | auto                                                      |
      | Policy Start Date | 01/03/2026                                                |
      | Policy End Date   | 01/02/2027                                                |
      | Priority          | Primary                                                   |
    And I click on Save button
    And I click on Insert Patient button
    Then I should see Encounter Section
    When I fill mandatory encounter details
      | Field               | Value                       |
      | Location            | AW Emergency Department     |
      | Department          | AW OBGYN ED Hold            |
      | Attending Clinician | Omair Mohamed Z I M Zain    |
      | Medical Service     | Aids and Appliances         |
      | ED Triage Code      | 1 - Resuscitation Immediate |
      | ED Disposition Code | Admitted                    |
      | Encounter Class     | Emergency                   |
      | Encounter Status    | In-Progress                 |
    And I click on Diagnosis and Interventions
    And I fill mandatory add service details
      | Field         | Value                                                            |
      | Activity Type | RADIOLOGY_ACHI                                                   |
      | Code          | 56223-00 \| Computerised tomography of spine, lumbosacral region |
      | Date          | 21/06/2026 00:00                                                 |
    And I click on Add Service button
    When I click on button with text "Insert Visit"
    Then I should see success message "Visit has been created successfully."
    And I wait "2" seconds
    When I edit activity in Diagnosis and Interventions tab
      | Field        | Value     |
      | Row Index    | 0         |
      | Menu Item    | Edit      |
      | Order Status | Completed |
    And I click on button with text "Update action"
    And I wait "2" seconds
    And I click on Mark As Ready To Bill button
    And I wait "2" seconds
    # Step 2 — Billing screen: open claim from Bill Management and generate bills
    When I navigate to Bill Management page
    And I click on created patient in bill management list
    And I click on Generate Bill button
    And I click alert dialog OK button
    And I wait "2" seconds
    Then the billing claim should load with services and categorized bills
    # Step 3 — Select one bill
    When I select categorized bill at row 0 for payment
    Then 1 bill should be selected for payment collection
    # Step 4 — Select multiple bills (select all when multiple rows exist)
    When I select all categorized bills for payment
    Then at least 1 bill should be selected for payment collection
    # Step 5 — Pay all selected bills using one payment method
    And I click on button with text "Add Payment"
    And I collect payment using payment type "Cash"
    Then I should see success message "Payment Added Successfully!"