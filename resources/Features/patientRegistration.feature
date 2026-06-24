@UC_01 @PatientRegistration
Feature: Patient Registration and Visitor Identification

  # UI-only visitor patient registration (Cerner ADT scenarios removed — add separately later).
  # Run all:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true"
  # Run TC01:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC01 and @UI"
  # Run TC02:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC02 and @UI"
  # Run TC03:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC03 and @UI"
  # Run TC04:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC04 and @UI"
  # Run TC05:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC05 and @UI"
  # Run TC06:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC06 and @UI"
  # Run TC07:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC07 and @UI"

  Background:
    Given I am on the login page
    When I enter username and password
    And I click on signin button

  # @TC01 @UI
  # Scenario: Passport Registration with Reason for Missing QID (UI)
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Test       |
  #     | Last Name                 | Visitor    |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #     | Gender                    | Male       |
  #   And the patient passport on the form should match the registered value
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section

  # @TC02 @UI @IntegrationGuardrail
  # Scenario: Drop registration when Passport provided without Missing QID Reason (UI)
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form without QID and missing reason
  #     | Field                | Value      |
  #     | MRN                  | auto       |
  #     | First Name           | Test       |
  #     | Last Name            | NoReason   |
  #     | DOB                  | 11/04/1966 |
  #     | Passport             | auto       |
  #     | Visitor Sub-Category | 2          |
  #     | Visa Type            | 2          |
  #     | Nationality          | ALB        |
  #   Then Insert Patient should be blocked by validation

  # @TC03 @UI
  # Scenario: Baby insured via Child relationship to visitor mother insurer
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Mother     |
  #     | Last Name                 | Insurer    |
  #     | DOB                       | 11/04/1985 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And I click on Add Insurance Card button
  #   Then the Add Insurance modal should be displayed
  #   And I fill mandatory insurance details in Add Insurance modal
  #     | Field             | Value                                                     |
  #     | Member ID         | auto                                                      |
  #     | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
  #     | Network Name      | QLM Network                                               |
  #     | Plan              | QLM Plan                                                  |
  #     | Policy Number     | auto                                                      |
  #     | Policy Start Date | 01/03/2026                                                |
  #     | Policy End Date   | 01/02/2027                                                |
  #     | Priority          | Primary                                                   |
  #   And I click on Save button
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section

  #   When I start a new patient visit from Patient Access
  #   And I click on add Patient button
  #   When I fill baby visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Baby       |
  #     | DOB                       | 01/06/2026 |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | New Born   |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And I click on Add Insurance Card button
  #   Then the Add Insurance modal should be displayed
  #   And I fill child insurance with relationship Child linked to the created mother patient
  #   And I click on Save button
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section
  #   And the visit label should be visitor insured or self pay visitor

  # @TC04 @UI
  # Scenario: Visitor patient with multiple insurance cards on create patient form
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Multi      |
  #     | Last Name                 | Insured    |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #     | Gender                    | Male       |
  #   And I click on Add Insurance Card button
  #   Then the Add Insurance modal should be displayed
  #   And I fill mandatory insurance details in Add Insurance modal
  #     | Field             | Value                                                     |
  #     | Member ID         | auto                                                      |
  #     | Receiver          | PHI/26872/000007 \| Alkoot Insurance and Reinsurance Co   |
  #     | Network Name      | AlKoot Network                                            |
  #     | Plan              | Alkoot Plan                                               |
  #     | Policy Number     | auto                                                      |
  #     | Policy Start Date | 01/03/2026                                                |
  #     | Policy End Date   | 01/02/2027                                                |
  #     | Priority          | Primary                                                   |
  #   And I click on Save button
  #   Then the patient should have 1 insurance card on the create patient form
  #   And the Health Insurance Info section should contain insurance for "Alkoot"

  #   And I click on Add Insurance Card button
  #   Then the Add Insurance modal should be displayed
  #   And I fill mandatory insurance details in Add Insurance modal
  #     | Field             | Value                                                     |
  #     | Member ID         | auto                                                      |
  #     | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
  #     | Network Name      | QLM Network                                               |
  #     | Plan              | QLM Plan                                                  |
  #     | Policy Number     | auto                                                      |
  #     | Policy Start Date | 01/03/2026                                                |
  #     | Policy End Date   | 01/02/2027                                                |
  #     | Priority          | Secondary                                                 |
  #   And I click on Save button
  #   Then the patient should have 2 insurance card on the create patient form
  #   And the Health Insurance Info section should contain insurance for "QLM"

  #   And I click on Add Insurance Card button
  #   Then the Add Insurance modal should be displayed
  #   And I fill mandatory insurance details in Add Insurance modal
  #     | Field             | Value                                                   |
  #     | Member ID         | auto                                                    |
  #     | Receiver          | PHI/26872/000007 \| Alkoot Insurance and Reinsurance Co |
  #     | Network Name      | Visitor Plan                                            |
  #     | Plan              | Emergency Only                                          |
  #     | Policy Number     | auto                                                    |
  #     | Policy Start Date | 01/03/2026                                              |
  #     | Policy End Date   | 01/02/2027                                              |
  #     | Priority          | Tertiary                                                |
  #   And I click on Save button
  #   Then the patient should have 3 insurance card on the create patient form
  #   And the Health Insurance Info section should contain insurance for "Visitor Plan"
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section

  @TC05 @UI
  Scenario: Correct missing QID by entering QID after insert blocked without passport
    And I click on Patient Access
    And I click on New visit button
    And I click on add Patient button
    When I fill visitor patient registration form with no QID and no passport
      | Field                     | Value      |
      | MRN                       | auto       |
      | First Name                | QidFix     |
      | Last Name                 | Visitor    |
      | DOB                       | 11/04/1966 |
      | Reason for Absence of QID | 1          |
      | Visitor Sub-Category      | 2          |
      | Visa Type                 | 2          |
      | Nationality               | ALB        |
    Then Insert Patient should be blocked without reaching Encounter Section
    When I uncheck No QID and enter QID details with the following data
      | Field           | Value      |
      | National ID     | auto       |
      | QID Expiry Date | 21/04/2027 |
    And I click on Insert Patient button
    Then I should see Encounter Section

  # @TC06 @UI
  # Scenario: Visitor with passport and missing QID reason; passport number updated later
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Passport   |
  #     | Last Name                 | Update     |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And the patient passport on the form should match the registered value
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section
  #   When I click on Edit Patient button
  #   And I update the patient passport number in Edit Patient modal to a new value
  #   And I save the Edit Patient modal
  #   Then the patient passport on the form should match the registered value

  # @TC07 @UI
  # Scenario: Patient initially No QID as Visitor; National ID provided later via Edit Patient
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | QidLater   |
  #     | Last Name                 | Visitor    |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section
  #   And the patient should have no National ID saved on the form
  #   When I click on Edit Patient button
  #   And I uncheck No QID and enter National ID details in Edit Patient modal with the following data
  #     | Field           | Value      |
  #     | National ID     | auto       |
  #     | QID Expiry Date | 21/04/2027 |
  #   And I save the Edit Patient modal
  #   Then the patient National ID on the form should match the registered value
