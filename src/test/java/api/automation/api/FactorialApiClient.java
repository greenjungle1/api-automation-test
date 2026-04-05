package api.automation.api;

import io.restassured.response.Response;
import api.automation.utils.ApiConfig;
import api.automation.utils.SpecBuilder;

import static io.restassured.RestAssured.given;

public class FactorialApiClient {

    public Response postFactorial(int number) {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .formParam(ApiConfig.NUMBER_PARAM, number)
                .post(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response postWithStringValue(String value) {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .formParam(ApiConfig.NUMBER_PARAM, value)
                .post(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response postWithNoParam() {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .post(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response postWithInvalidParam(String value) {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .formParam(ApiConfig.INVALID_PARAM, value)
                .post(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response postWithParamName(String paramName, String value) {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .formParam(paramName, value)
                .post(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response getFactorial() {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .get(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response putFactorial() {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .put(ApiConfig.FACTORIAL_ENDPOINT);
    }

    public Response deleteFactorial() {
        return given()
                .spec(SpecBuilder.buildRequestSpec())
                .delete(ApiConfig.FACTORIAL_ENDPOINT);
    }
}
