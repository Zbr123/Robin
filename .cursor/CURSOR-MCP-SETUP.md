# Cursor MCP & CLI setup for ROBIN automation

## Current status

| Tool | Purpose | Config |
|------|---------|--------|
| **Playwright MCP** | Browser automation from chat (locators, flows, debug) | `.cursor/mcp.json` |
| **Browser MCP** | Built-in Cursor browser tab (`browser_navigate`, `browser_snapshot`) | Enabled in Cursor by default |
| **Maven CLI** | Run Selenium/Cucumber tests | Terminal / Agent shell |

Node.js is installed at `C:\Program Files\nodejs\` (v22+).

---

## 1. Connect Playwright MCP (one-time)

1. Open this project in **Cursor** (`Robin` or `Robin Playwright` folder).
2. Press **Ctrl + Shift + J** → **Tools & MCP**.
3. Confirm **Enable MCP servers** is ON.
4. You should see **playwright** from `.cursor/mcp.json`.
5. Toggle it **Off → On** if it shows disconnected.
6. Green dot + tool list = connected.

### If playwright does not connect

- **Output panel**: `Ctrl + Shift + U` → dropdown **MCP** → read errors.
- **spawn ENOENT**: `mcp.json` already uses full path to `npx.cmd`.
- **Still failing**: run in terminal:
  ```powershell
  npx -y @playwright/mcp@latest --help
  ```
  Then restart Cursor.

### Optional: global MCP (all projects)

Create `C:\Users\co-ventechpc\.cursor\mcp.json` with the same `playwright` block if you want it in every workspace.

---

## 2. Use MCP to automate faster

### Playwright MCP (in Agent chat)

Ask things like:
- “Open ROBIN login, snapshot the page, and list form field IDs.”
- “Navigate to Create Patient and find the gender dropdown locator.”
- “Fill visitor registration and tell me the Patient Category label text.”

The agent can call Playwright MCP tools instead of guessing locators.

### Browser MCP (built-in)

Useful for quick checks:
1. `browser_navigate` → ROBIN URL
2. `browser_snapshot` → accessibility tree + refs
3. `browser_click` / `browser_fill` on refs

Good for validating a step before adding it to Java/TypeScript.

---

## 3. CLI shortcuts (no MCP)

### Selenium (`Robin`)

```powershell
cd "C:\Users\co-ventechpc\Desktop\Robin"

mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC15 and @UI"
mvn test "-Dtest=PatientRegistrationTestRunner" "-Dheaded=true" "-Dcucumber.filter.tags=@TC18 and @UI"
mvn test "-Dtest=AuditTestRunner" "-Dheaded=true"
mvn test "-Dtest=BillingTestRunner" "-Dheaded=true"
```

### Playwright BDD (`Robin Playwright`)

```powershell
cd "C:\Users\co-ventechpc\Desktop\Robin Playwright"

npm run test:headed
npm run test:tc18
npx cucumber-js --tags "@TC15 and @UI"
```

---

## 4. Recommended workflow

1. **Explore** with Browser MCP or Playwright MCP → confirm locators on live ROBIN QTR.
2. **Add scenario** in `resources/Features/*.feature`.
3. **Run** with Maven or `npm run` headed.
4. **Fix** using failure screenshots in `target/surefire-reports` or `reports/`.

---

## 5. Security note

Do not commit real passwords. Tests use QTR credentials from code/config; for production-like envs use env vars:

```powershell
$env:ROBIN_USERNAME = "your-user"
$env:ROBIN_PASSWORD = "your-pass"
```
