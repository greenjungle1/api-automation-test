# Factorial API Automation Test

REST-Assured + Cucumber BDD + TestNG API test suite for:
**https://qainterview.pythonanywhere.com**

GitHub Actions runs tests on every push and publishes the report to:
**https://greenjungle1.github.io/api-automation-test/**

---

## Endpoint Discovered

Found by inspecting the page source HTML

```javascript
type: 'POST'
url:  '/factorial'
data: { 'number': number }   // form body — not query string
factorial.answer             // response field is "answer"
```

| Property     | Value                                              |
|--------------|----------------------------------------------------|
| Method       | `POST`                                             |
| URL          | `https://qainterview.pythonanywhere.com/factorial` |
| Content-Type | `application/x-www-form-urlencoded`                |
| Parameter    | `number` (integer)                                 |
| Success      | `{ "answer": 120 }`                                |
| Auth         | None                                               |

---

## Quick Start

**Prerequisites:** Java 11+, Maven 3.8+, TestNG 

```bash
git clone https://github.com/greenjungle1/api-automation-test.git
cd api-automation-test
mvn test
```

---

## Running Tests

```bash
mvn test                                          # all scenarios
mvn test -Dcucumber.filter.tags="@sanity"         # sanity only
mvn test -Dcucumber.filter.tags="@regression"     # regression
mvn test -Dcucumber.filter.tags="@negative"       # negative paths
mvn test -Dcucumber.filter.tags="@contract"       # contract checks
```

Reports generated at `target/cucumber-reports/cucumber.html`

---

## Test Coverage

| Tag | What it covers                                                                 |
|-----|--------------------------------------------------------------------------------|
| `@sanity` | Core happy path — is the API alive and returning correct results?              |
| `@regression` | Full coverage — all passing scenarios                                          |
| `@negative` | Invalid inputs — strings, decimals, empty, whitespace, negative numbers        |
| `@contract` | Response structure, content-type, error messages, HTTP methods, parameter name |
| `@bug` | Known defects — expected to fail until fixed                                   |

---

## Testing Approach

### Exploration
1. Loaded the app in a browser and clicked Calculate with various inputs
2. Opened DevTools → Network tab to observe the XHR call
3. Inspected the raw page HTML source — discovered the endpoint is `POST /factorial` with form body `number=value` and response field `answer`
4. Tested boundary values, edge cases and invalid inputs manually
5. Reviewed the public GitHub repo `qxf2/qa-interview-web-application` which confirms the app is seeded with bugs

### Contract Analysis
The HTML validation logic:
```javascript
if (number != parseInt(number, 10)) {
    // reject — show "Please enter an integer"
} else {
    // call the API
}
```
This uses JavaScript's loose `!=` comparison which coerces types — meaning `+5`, `-5`, `05` all pass validation and reach the API.



### ⚠️ Open Finding — Undocumented Input Limit

| Input | HTTP Status | Result                |
|-------|-------------|-----------------------|
| `991` | 200 ✅ | Correct large integer |
| `992` | 500 ❌ | Server crash          |

The API has no documented maximum input value. HTTP 500 is never acceptable.

### Note on GET /factorial

`GET /factorial` currently returns HTTP 200 because the server serves the **HTML page** for any
GET request to that path — it is not rejecting the method at all.
This is the same HTML page we use during exploration to discover the endpoint contract.
However the correct behaviour should be `405 Method Not Allowed` since the endpoint
only accepts `POST` — derived from `type: 'POST'` in the page source.

---

## Assumptions

1. `POST` with form body — confirmed by page source
2. Response field is `answer` — confirmed by `factorial.answer` in page JS
3. `0! = 1` is mathematically correct and expected
4. No authentication required
5. `+5` is accepted by the contract — `parseInt("+5", 10)` returns `5` and `"+5" != 5` is `false` due to loose comparison

---

## What I Would Add Given More Time

- **Performance testing** — I dont have much of the experience in Performance test but give time i would add couple of scenarios
- **WireMock** — mock server to stub the `/factorial` endpoint so tests run offline without depending on the live site.
- **Allure report** — richer HTML with history and trends
