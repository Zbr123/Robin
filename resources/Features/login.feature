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
    Then I should see success message "Patient has been added successfully."
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
    Then I should see service as added with service code "56223-00"
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
#    Then I should see success message "Marked as Ready to Bill"
    When I navigate to Bill Management page
    And I click on created patient in bill management list
    And I click on Generate Bill button
    And I click alert dialog OK button
    And I wait "2" seconds
    And I select all categorized bills
    And I click on button with text "Add Payment"
    And I click payment Save button
    Then I should see success message "Payment Added Successfully!"