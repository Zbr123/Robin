@UC_01 @PatientRegistration
Feature: Patient Registration and Visitor Identification

  # UI-only visitor patient registration (Cerner ADT scenarios removed — add separately later).
  # Run all:  mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true"
  
  Background:
    Given I am on the login page
    When I enter username and password
    And I click on signin button

  # @UC_01_Test_Case_01 @UI
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

  # @UC_01_Test_Case_02 @UI @IntegrationGuardrail
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

  # @UC_01_Test_Case_05 @UI
  # Scenario: UC_01 Test Case 05 - Patient created without auto-created visit
  #   # Verify Patient Creation When ADT Message Is Received Without Encounter
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | NoEnc      |
  #     | Last Name                 | Visitor    |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And I click on Insert Patient button
  #   Then the patient record should be created successfully
  #   When I close the encounter tab without saving the visit
  #   And I search for the created patient visit in Patient Access
  #   Then no visit should be found for the created patient

  # # @UC_01_Test_Case_03 @UI
  # # Scenario: UC_01 Test Case 03 - Baby insured via child relationship to visitor mother insurer
  # #   And I click on Patient Access
  # #   And I click on New visit button
  # #   And I click on add Patient button
  # #   When I fill visitor patient registration form with the following data
  # #     | Field                     | Value      |
  # #     | MRN                       | auto       |
  # #     | First Name                | Mother     |
  # #     | Last Name                 | Insurer    |
  # #     | DOB                       | 11/04/1985 |
  # #     | Passport                  | auto       |
  # #     | No QID Check              | true       |
  # #     | Reason for Absence of QID | 1          |
  # #     | Visitor Sub-Category      | 2          |
  # #     | Visa Type                 | 2          |
  # #     | Nationality               | ALB        |
  # #   And I click on Add Insurance Card button
  # #   Then the Add Insurance modal should be displayed
  # #   And I fill mandatory insurance details in Add Insurance modal
  # #     | Field             | Value                                                     |
  # #     | Member ID         | auto                                                      |
  # #     | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
  # #     | Network Name      | QLM Network                                               |
  # #     | Plan              | QLM Plan                                                  |
  # #     | Policy Number     | auto                                                      |
  # #     | Policy Start Date | 01/03/2026                                                |
  # #     | Policy End Date   | 01/02/2027                                                |
  # #     | Priority          | Primary                                                   |
  # #   And I click on Save button
  # #   And I click on Insert Patient button
  # #   Then I should see Encounter Section
  # #   When I start a new patient visit from Patient Access
  # #   And I click on add Patient button
  # #   When I fill baby visitor patient registration form with the following data
  # #     | Field                     | Value      |
  # #     | MRN                       | auto       |
  # #     | First Name                | Baby       |
  # #     | DOB                       | 01/06/2026 |
  # #     | No QID Check              | true       |
  # #     | Reason for Absence of QID | New Born   |
  # #     | Visitor Sub-Category      | 2          |
  # #     | Visa Type                 | 2          |
  # #     | Nationality               | ALB        |
  # #   And I click on Add Insurance Card button
  # #   Then the Add Insurance modal should be displayed
  # #   And I fill child insurance with relationship Child linked to the created mother patient
  # #   And I click on Save button
  # #   And I click on Insert Patient button
  # #   Then I should see Encounter Section
  # #   And the visit label should be visitor insured or self pay visitor

  # @UC_01_Test_Case_06 @UI
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

  # @UC_01_Test_Case_07 @UI
  # Scenario: Correct missing QID by entering QID after insert blocked without passport
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with no QID and no passport
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | QidFix     |
  #     | Last Name                 | Visitor    |
  #     | DOB                       | 11/04/1966 |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   Then Insert Patient should be blocked without reaching Encounter Section
  #   When I uncheck No QID and enter QID details with the following data
  #     | Field           | Value      |
  #     | National ID     | auto       |
  #     | QID Expiry Date | 21/04/2027 |
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section

  # @UC_01_Test_Case_08 @UI
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

  # @UC_01_Test_Case_09 @UI
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

  # @UC_01_Test_Case_09b @UI
  # Scenario: Visitor patient with insurance card; Member ID updated via Edit Patient
  #   # Step 1 — Create visitor patient with insurance card
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Insured    |
  #     | Last Name                 | Visitor    |
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
  #     | Receiver          | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
  #     | Network Name      | QLM Network                                               |
  #     | Plan              | QLM Plan                                                  |
  #     | Policy Number     | auto                                                      |
  #     | Policy Start Date | 01/03/2026                                                |
  #     | Policy End Date   | 01/02/2027                                                |
  #     | Priority          | Primary                                                   |
  #   And I click on Save button
  #   Then the patient should have 1 insurance card on the create patient form
  #   And the Health Insurance Info section should contain insurance for "QLM"
  #   And I click on Insert Patient button
  #   Then I should see Encounter Section
  #   And the patient should be classified as visitor insured
  #   # Step 2 — Update Member ID on insurance card via Edit Patient
  #   When I click on Edit Patient button
  #   And I click on edit insurance card row 0 in Edit Patient modal
  #   And I update the insurance Member ID in Edit Insurance modal to a new value
  #   And I save the Edit Insurance modal
  #   Then the insurance Member ID in Edit Patient insurance list row 0 should match the registered value
  #   And I save the Edit Patient modal
  #   Then I should see Encounter Section

  # @UC_01_Test_Case_17 @UI
  # Scenario: TRANSIT visitor with insurance stores ADT fields and defaults encounter to Visitor Insured
  #   # UC_01 Test Case 17 — TRANSIT/FAMILY VISIT/HAYYA + Insurance = Yes → Visitor Insured
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Transit    |
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
  #   Then the patient should have 1 insurance card on the create patient form
  #   And the Health Insurance Info section should contain insurance for "QLM"
  #   And I click on Insert Patient button
  #   Then the patient record should be created successfully
  #   And the ADT visitor fields should be stored on the patient
  #   Then I should see Encounter Section
  #   And the patient insurer should be listed on the encounter
  #   And the encounter label should be Visitor Insured

  # @UC_01_Test_Case_15 @UI
  # Scenario: TRANSIT visitor self pay without insurance
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | Transit    |
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
  #   And the encounter label should be Visitor Self Pay

  # @UC_01_Test_Case_16 @UI
  # Scenario: WORK or RESIDENT visa stores ADT fields (Self Pay defaults after visit is saved)
  #   # UC_01 Test Case 16 — WORK/RESIDENT VISA + Insurance = No. The plain "Self Pay" label
  #   # only resolves after the visit is saved (a billing-flow step); this scenario covers the
  #   # reliable Robin UI part: patient created with WORK visa + ADT fields stored + encounter opened.
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | WorkVisa   |
  #     | Last Name                 | Resident   |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | WORK       |
  #     | Nationality               | ALB        |
  #   And I click on Insert Patient button
  #   Then the patient record should be created successfully
  #   And the ADT visitor fields should be stored on the patient
  #   Then I should see Encounter Section
  #   And the visa type "WORK VISA" should be displayed on the encounter page

  @UC_01_Test_Case_10 @UI
  Scenario: Visitor without QID gets insurance in Robin and is tagged MVHI (Visitor Insured); Member ID updated
    # UC_01 Test Case 10 — Patient without QID (reason + passport) → Visitor created,
    # insurance added in Robin → patient becomes Visitor Insured (MVHI), then Member ID updated.
    And I click on Patient Access
    And I click on New visit button
    And I click on add Patient button
    When I fill visitor patient registration form with the following data
      | Field                     | Value      |
      | MRN                       | auto       |
      | First Name                | Mvhi       |
      | Last Name                 | Visitor    |
      | DOB                       | 11/04/1966 |
      | Passport                  | auto       |
      | No QID Check              | true       |
      | Reason for Absence of QID | 1          |
      | Visitor Sub-Category      | 2          |
      | Visa Type                 | 2          |
      | Nationality               | ALB        |
      | Gender                    | Male       |
    # Step 2 — Insurance added in Robin → patient becomes insurance (MVHI) visitor
    And I click on Add Insurance Card button
    Then the Add Insurance modal should be displayed
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
    Then the patient should have 1 insurance card on the create patient form
    And the Health Insurance Info section should contain insurance for "QLM"
    And I click on Insert Patient button
    Then I should see Encounter Section
    And the patient should be classified as visitor insured
    # Step 3 — Update Member ID on the insurance card via Edit Patient
    When I click on Edit Patient button
    And I click on edit insurance card row 0 in Edit Patient modal
    And I update the insurance Member ID in Edit Insurance modal to a new value
    And I save the Edit Insurance modal
    Then the insurance Member ID in Edit Patient insurance list row 0 should match the registered value
    And I save the Edit Patient modal
    Then I should see Encounter Section

  # @UC_01_Test_Case_18 @UI
  # Scenario: TRANSIT visitor self pay
  #   And I click on Patient Access
  #   And I click on New visit button
  #   And I click on add Patient button
  #   When I fill visitor patient registration form with the following data
  #     | Field                     | Value      |
  #     | MRN                       | auto       |
  #     | First Name                | VisaFix    |
  #     | Last Name                 | Visitor    |
  #     | DOB                       | 11/04/1966 |
  #     | Passport                  | auto       |
  #     | No QID Check              | true       |
  #     | Reason for Absence of QID | 1          |
  #     | Visitor Sub-Category      | 2          |
  #     | Visa Type                 | 2          |
  #     | Nationality               | ALB        |
  #   And I click on Insert Patient button
  #   Then the patient record should be created successfully
  #   And the ADT visitor fields should be stored on the patient
  #   Then I should see Encounter Section
  #   And the encounter label should be Visitor Self Pay
  