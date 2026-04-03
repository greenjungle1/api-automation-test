# Factorial API Automation Test

REST-Assured + Cucumber BDD + TestNG test suite for the Factorial Calculator at https://qainterview.pythonanywhere.com


## Endpoint Discovered

Found by inspecting the page source HTML:

```javascript
type: 'POST'
url:  '/factorial'
data: { 'number': number }        // form body — not query string
response field: factorial.answer  // JSON key is "answer"
```

| Property       | Value                                              |
|----------------|----------------------------------------------------|
| Method         | `POST`                                             |
| URL            | `https://qainterview.pythonanywhere.com/factorial` |
| Content-Type   | `application/x-www-form-urlencoded`                |
| Parameter      | `number` (integer)                                 |
| Success        | `{ "answer": 120 }` HTTP 200                       |
| Auth           | None                                               |

---

## Quick Start

**Prerequisites:** Java 11+, Maven 3.8+

```bash
git clone https://github.com/<your-username>/fractional-api-automation-test.git
cd fractional-api-automation-test
mvn test
```

---

## Running Tests

```bash
mvn test                                            # all scenarios
mvn test -Dcucumber.filter.tags="@sanity"           # sanity only
mvn test -Dcucumber.filter.tags="@regression"       # regression
mvn test -Dcucumber.filter.tags="@negative"         # negative paths
mvn test -Dcucumber.filter.tags="@contract"         # contract checks
mvn test -Dcucumber.filter.tags="@bug"              # known bugs (expected to fail)
mvn test -Dbase.url=http://localhost:6464           # local instance
```

Reports generated at `target/cucumber-reports/cucumber.html`

---

## Test Coverage

| Tag | What it covers |
|-----|----------------|
| `@sanity` | `0`, `1`, `5` — is the API alive and correct? |
| `@regression` | Large numbers — `555`, `991` |
| `@negative` | Invalid inputs — strings, decimals, empty, whitespace |
| `@contract` | Response structure — fields, content-type, error messages, HTTP methods |
| `@bug` | Known defects — expected to fail until fixed |

---

## Findings

### Bugs

| ID | Summary | Severity |
|----|---------|----------|
| BUG-001 | Negative integers return a result instead of an error | High |
| BUG-002 | Validation errors return HTTP 200 instead of 400 | Medium |
| BUG-004 | Missing parameter returns no clear error | Low |
| BUG-005 | GET / PUT / DELETE not rejected with 405 | Low |

### ⚠️ Open Finding — Undocumented Input Limit

During exploration, the server was found to crash for large inputs:

| Input | HTTP Status | Result |
|-------|-------------|--------|
| `991` | 200 ✅ | Correct large integer |
| `992` | 500 ❌ | Server crash |

The API contract does not define a maximum input value. HTTP 500 is never acceptable — the server must either compute the result or return a graceful `400` with a clear message.

> **Open question to API owner:** Is there an intended maximum? If so, document it and enforce it with a 400 response instead of a 500 crash.

Tests assert `the response status code should not be 500` rather than `should be 200` — because either a correct result (200) or a graceful rejection (400) would be acceptable.

---

## Assumptions

1. `POST` with form body — confirmed by page source
2. Response field is `answer` — confirmed by `factorial.answer` in page JS
3. `0! = 1` is expected — mathematically correct
4. `@bug` scenarios fail intentionally — they document known defects
5. No authentication required
6. Tests run against the live site — internet access needed

---

## What I Would Add Given More Time

- Response time assertion — `< 3000ms` SLA check
- Concurrency test — 10 parallel requests for thread safety
- Security probes — SQL injection, script injection via `number` field
- WireMock stub — run tests offline in CI
- Allure report — richer HTML with history trending
- GitHub Actions — run on every push, publish reports
