Feature: Complete Patient Journey with Billing

  Scenario: Create a new patient, book an encounter, add services, and complete billing
    Given I am on the login page
    When I enter username and password
    And I click on signin button
    And I click on Patient Access
    And I click on New visit button
    And I click on add Patient button
    And I fill data in patient form
    And I click on Add Insurance Card button
    And I add Card details
      | Field        | Value                                        |
      | Card Number  | 12345678901                                  |
      | Start Date   | 01/01/2025                                   |
      | End Date     | 31/12/2025                                   |
    And I click on Save button
    And I click on Insert Patient button
    Then I should see Encounter Section
    When I select home from Encounter Type dropdown
    And I type location as Family Medicine Clinic
    And I type and select department as Family Medicine
    And I type and select Attending Clinician
    And I type and select Ordering Clinician
    And I select Start Type as Elective
    And I select End Type as Discharged with approval
    And I select Visit Type as New
    And I enter end date as "31/12/2025"
    And I click on Diagnosis and Interventions
    Then I should see Add bulk Diagnosis button
    When I select Activity Type as CPT
    And I enter Code as 70030
    And I click on Add Service button
    Then I should see service as added
    When I click on Insert visit button
    And I click on Mark As Ready To Bill button
    Then I should see success message
    When I navigate to Billing-OP Receipts page
    And I click on created visit
    And I select payment type cash
    And I type 10000 amount in Payment Amount field
    And I select payment type Cheque
    And I type 0 amount in Payment Amount field
    And I click on Generate Receipt Button
    Then I should see Receipt generated success message


