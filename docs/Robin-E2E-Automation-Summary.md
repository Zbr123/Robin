# Robin E2E Automation — Summary Report

**Project:** Robin (RobinAutomation)  
**Scenario:** Create a new patient, book an encounter, add services, and complete billing  
**Feature file:** `resources/Features/login.feature`  
**Environment:** https://auto-robin-qtr.santechture.com/ROBIN  
**Credentials:** QFacilityAdmin / QFacilityAdmin123  
**Last known run:** BUILD SUCCESS (~71s, headed Chrome)

---

## 1. What is automated (active steps)

| # | Step | What it does |
|---|------|----------------|
| 1 | Given I am on the login page | Opens ROBIN URL, waits for login screen |
| 2 | When I enter username and password | Uses QFacilityAdmin / QFacilityAdmin123 |
| 3 | And I click on signin button | Signs in, waits for Patient Access |
| 4 | And I click on Patient Access | Opens patient journey screen |
| 5 | And I click on New visit button | Opens Patient and Encounter |
| 6 | And I click on add Patient button | Opens Create Patient |
| 7 | And I fill data in patient form | Random MRN, names, National ID; fixed DOB 12/03/2015; QID 21/04/2026; Nationality American; Gender Male |
| 8 | And I click on Add Insurance Card button | Opens insurance modal |
| 9 | And I fill mandatory insurance details in Add Insurance modal | Data table (see Section 4); auto = random Member ID & Policy Number |
| 10 | And I click on Save button | Saves insurance card, waits for Insert Patient |
| 11 | And I click on Insert Patient button | Inserts patient (stale-element retry) |
| 12 | Then I should see Encounter Section | Asserts Encounter section visible |
| 13 | When I fill mandatory encounter details | Data table (see Section 5); Class & Status filled last |
| 14 | And I click on Diagnosis and Interventions | Opens diagnosis/services tab |
| 15 | And I fill mandatory add service details | Activity Type, Code autocomplete, Date |
| 16 | And I click on Add Service button | Clicks AccumedHaadActivityListForm:addServiceBTN |
| 17 | Then I should see service as added with service code "B07A" | Parameterized assertion on activity list |
| 18 | When I click on button with text "Insert Visit" | Parameterized button click by visible text |
| 19 | Then I should see success message "Visit has been created successfully." | Parameterized growl title assertion |

**Coverage:** Login → new patient → insurance → encounter → add service → insert visit → success message.

---

## 2. Major fixes implemented

### Performance & stability
- Removed long fixed Thread.sleep waits; replaced with WebDriverWait / condition waits
- Chrome: implicit wait 0; unique profile per run (avoids profile lock)
- Insert Patient: stale-element retry loop

### Insurance modal
- Receiver / Network / Plan use PrimeFaces selectOneMenu (label + panel)
- Wait for dependent dropdowns after Receiver/Network AJAX
- Stale panel handling when listing dropdown options

### Encounter section
- Correct widget IDs: InvoiceForm:medicalServiceID, InvoiceForm:eDTriageCode
- Encounter Class / Status: encounterForm:icd, encounterForm:encounterStatusValue
- Selection waits until value sticks (panel close + label/hidden select confirm + retries)
- Encounter Class & Status filled last (after Location, Department, Clinician, etc.)
- Attending Clinician: table-style autocomplete matching (data-label, row text, cells)
- Dismiss autocomplete overlays between fields

### Add service (Diagnosis and Interventions)
- Activity Type via selectOneMenu
- Code autocomplete with B07A | … pipe-separated matching
- Date field with stale-input retry
- Activity code: wait until enabled after Activity Type; retry on not interactable

### New parameterized steps
- When I click on button with text {string} — any button by visible text (e.g. Insert Visit)
- Then I should see service as added with service code {string}
- Then I should see success message {string}
- Existing When I click on Update visit button kept (id-based Update Visit); commented in feature

### Patient form
- QID expiry via direct date entry (21/04/2026) when calendar was flaky
- Nationality: American via selectOneMenu panel

---

## 3. What is not automated yet (commented in feature)

| Step | Status | Notes |
|------|--------|--------|
| When I click on Update visit button | Commented | Step exists (actionsFormInvoice:j_idt2194) |
| And I click on Mark As Ready To Bill button | Commented | Step + locator exist in LoginPage |
| When I navigate to Billing-OP Receipts page | Commented | Billing → OP Receipts navigation implemented |
| And I click on created visit | Commented | Uses generated patient first/last name from run |
| And I select payment type cash | Commented | Implemented |
| And I type 10000 amount in Payment Amount field | Commented | Implemented |
| And I select payment type Cheque | Commented | Implemented |
| And I type 0 amount in Payment Amount field | Commented | Implemented |
| And I click on Generate Receipt Button | Commented | Implemented |
| Then I should see Receipt generated success message | Commented | Asserts Receipt generated Successfully + OK click |

Billing block: step definitions and page methods exist in code; only disabled in the feature file.

---

## 4. Test data — Insurance modal

| Field | Value |
|--------|--------|
| Member ID | auto (automation: random 4 digits) |
| Receiver | PHI/116849/000006 \| QLM Life & Medical Insurance Company |
| Network Name | QLM Network |
| Plan | QLM Plan |
| Policy Number | auto (automation: random 4 digits) |
| Policy Start Date | 01/03/2026 |
| Policy End Date | 01/02/2027 |
| Priority | Primary |

---

## 5. Test data — Encounter

| Field | Value |
|--------|--------|
| Location | AW Laboratory Department |
| Department | AW Laboratory |
| Attending Clinician | Mahmoud Metwally Ameen Mohamed Hegazy |
| Medical Service | Aids and Appliances |
| ED Triage Code | 1 - Resuscitation Immediate |
| Encounter Class | Outpatient |
| Encounter Status | In-Progress |

Fill order in automation: Location → Department → Attending → Medical Service → ED Triage → Class → Status (last two).

---

## 6. Test data — Add service

| Field | Value |
|--------|--------|
| Activity Type | IP_AR_DRG |
| Code | B07A \| Peripheral and Cranial Nerve and Other Nervous System Procedures W CC |
| Date | 21/06/2026 00:00 |

Expected after Add Service: service code B07A visible in activity list.

---

## 7. Notes in feature file (not in scenario)

- Missing Finance Account
- Visitor Non Insured Income Account
- Optional alternate: Activity Type ER_URG, Code URG007
- Manual note: 51310384983 (likely National ID)

---

## 8. Scenario progress

```
DONE (~70%):
  Login → Patient → Insurance → Encounter → Services → Insert Visit → Success

REMAINING (~30%):
  Mark As Ready To Bill → Billing OP Receipts → Payments → Generate Receipt
```

---

## 9. Key files & run command

| File | Role |
|------|------|
| resources/Features/login.feature | Scenario + data tables |
| src/test/java/stepDefinitions/LoginSteps.java | Cucumber glue |
| src/main/java/Pages/LoginPage.java | Locators & UI actions |
| src/main/java/utils/DriverManager.java | Chrome setup |

**Run:** `mvn test -Dheaded=true`

---

## 10. Suggested next steps

1. Uncomment billing steps (feature lines 45–53) and run headed.
2. Parameterize Mark As Ready To Bill success message if text differs.
3. Resolve Finance Account / Visitor Non Insured Income Account if required before billing.
4. Add Update Visit to flow if app requires Insert then Update before billing.
5. Optional: second scenario for ER_URG / URG007.

---

*Generated: May 2026 — Robin automation project*
