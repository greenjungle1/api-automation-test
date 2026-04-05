package api.automation.steps;

import api.automation.utils.SpecBuilder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import api.automation.api.FactorialApiClient;
import api.automation.utils.ApiConfig;

import java.math.BigInteger;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


public class FactorialStepDefinitions {

    private final FactorialApiClient apiClient = new FactorialApiClient();
    private Response response;

    @Given("the Factorial API is available")
    public void theFactorialApiIsAvailable() {
        Response healthCheck = given()
                .baseUri(ApiConfig.BASE_URL)
                .formParam(ApiConfig.NUMBER_PARAM, 1)
                .post(ApiConfig.FACTORIAL_ENDPOINT);

        assertThat(healthCheck.statusCode())
                .as("Health check failed — POST /factorial at [%s] is not reachable.",
                        ApiConfig.BASE_URL)
                .isEqualTo(200);

        System.out.println("  Health check passed — POST /factorial is reachable");
    }

    @When("I POST the factorial of {int}")
    public void iPostTheFactorialOf(int number) {
        response = apiClient.postFactorial(number);
    }

    @When("I POST the factorial with value {string}")
    public void iPostTheFactorialWithValue(String value) {
        response = apiClient.postWithStringValue(value);
    }

    @When("I POST the factorial with invalid value {string}")
    public void iPostTheFactorialWithInvalidValue(String value) {
        String resolvedValue;
        switch (value) {
            case "[empty]": resolvedValue = "";  break;
            case "[space]": resolvedValue = " "; break;
            default:        resolvedValue = value;
        }
        response = apiClient.postWithStringValue(resolvedValue);
    }

    @When("I POST to the factorial endpoint with no parameter")
    public void iPostToTheFactorialEndpointWithNoParameter() {
        response = apiClient.postWithNoParam();
    }

    @When("I GET the factorial of {int}")
    public void iGetTheFactorialOf(int number) {
        response = apiClient.getFactorial();
    }

    @When("I send a {string} request to the factorial endpoint")
    public void iSendARequestToTheFactorialEndpoint(String method) {
        switch (method.toUpperCase()) {
            case "GET":    response = apiClient.getFactorial();    break;
            case "PUT":    response = apiClient.putFactorial();    break;
            case "DELETE": response = apiClient.deleteFactorial(); break;
            default:
                throw new IllegalArgumentException(
                    "Unsupported method in test: '" + method + "'. "
                    + "Use GET, PUT or DELETE.");
        }
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatus) {
        assertThat(response.statusCode())
                .as("HTTP status code")
                .isEqualTo(expectedStatus);
    }

    @Then("the response status code should not be 500")
    public void theResponseStatusCodeShouldNotBe500() {
        assertThat(response.statusCode())
                .as("BUG-006: Server must never return HTTP 500. "
                        + "Large inputs must be handled gracefully — "
                        + "either return 200 with the result or 400 with a clear error message.")
                .isNotEqualTo(500);
    }

    @And("the response content-type should be JSON")
    public void theResponseContentTypeShouldBeJson() {
        assertThat(response.contentType())
                .as("Content-Type header must be application/json")
                .containsIgnoringCase("application/json");
    }

    @And("the response body should not be empty")
    public void theResponseBodyShouldNotBeEmpty() {
        assertThat(response.body().asString())
                .as("response body must not be empty")
                .isNotBlank();
    }

    @And("the answer field should be present")
    public void theAnswerFieldShouldBePresent() {
        Object answer = response.jsonPath().get(ApiConfig.FIELD_ANSWER);
        assertThat(answer)
                .as("'answer' field must exist and not be null in the JSON response")
                .isNotNull();
    }

    @And("the answer field should be a non-negative number")
    public void theAnswerFieldShouldBeANonNegativeNumber() {
        long value = response.jsonPath().getLong(ApiConfig.FIELD_ANSWER);
        assertThat(value)
                .as("factorial result must be >= 1")
                .isGreaterThanOrEqualTo(1L);
    }

    @And("the answer field should equal {string}")
    public void theAnswerFieldShouldEqualString(String expected) {
        String actual = response.jsonPath().getString(ApiConfig.FIELD_ANSWER);
        assertThat(actual)
                .as("answer field string value")
                .isEqualTo(expected);
    }

    @And("the response should only contain the answer field")
    public void theResponseShouldOnlyContainTheAnswerField() {
        Map<String, Object> body = response.jsonPath().getMap("$");
        assertThat(body.keySet())
                .as("Response body should only contain the 'answer' field. "
                        + "Unexpected extra fields found: " + body.keySet())
                .containsExactly(ApiConfig.FIELD_ANSWER);
    }

    @And("the answer field should contain an error message")
    public void theAnswerFieldShouldContainAnErrorMessage() {
        String answer = response.jsonPath().getString(ApiConfig.FIELD_ANSWER);
        assertThat(answer)
                .as("error message in 'answer' field")
                .isEqualTo("Please enter a positive integer")
                .isNotNull()
                .isNotBlank();
    }

    @And("the answer field should equal {long}")
    public void theAnswerFieldShouldEqualLong(long expected) {
        long actual = response.jsonPath().getLong(ApiConfig.FIELD_ANSWER);
        assertThat(actual)
                .as("answer field (numeric factorial result)")
                .isEqualTo(expected);
    }


    @And("the answer field should be a large valid integer")
    public void theAnswerFieldShouldBeALargeValidInteger() {
        String raw = response.jsonPath().getString(ApiConfig.FIELD_ANSWER);

        assertThat(raw)
                .as("answer field must not be null — server overflowed silently")
                .isNotNull();

        assertThat(raw)
                .as("answer field must not be 'Infinity' — server must not use float arithmetic")
                .isNotEqualToIgnoringCase("Infinity");

    }

    @When("I POST the factorial with parameter name {string} and value {string}")
    public void iPostTheFactorialWithParameterNameAndValue(String paramName, String value) {
        response = apiClient.postWithParamName(paramName, value);
    }
}
