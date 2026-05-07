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
    And I fill mandatory insurance details in Add Insurance modal
      | Field             | Value                                                      |
      | Member ID         | auto                                                       |
      | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
      | Network Name      | QLM Network                                                |
      | Plan              | QLM Plan                                                   |
      | Policy Number     | auto                                                       |
      | Policy Start Date | 01/03/2026                                                 |
      | Policy End Date   | 01/02/2027                                                 |
      | Priority          | Primary                                                    |
    And I click on Save button
    And I click on Insert Patient button
    Then I should see Encounter Section
    When I fill mandatory encounter details
      | Field               | Value                                      |
      | Location            | Hamad                                      |
      | Department          | 5079                                       |
      | Attending Clinician | TP82743:Umair Mohamed Z I M Zain           |
      | Medical Service     | Aids and Appliances                        |
      | ED Triage Code      | 1 - Resuscitation Immediate                |
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


