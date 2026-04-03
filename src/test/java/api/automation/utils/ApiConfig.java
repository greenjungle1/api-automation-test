package api.automation.utils;

public final class ApiConfig {

    public ApiConfig() {}

    public static final String BASE_URL =
            System.getProperty("base.url", "https://qainterview.pythonanywhere.com");

    public static final String FACTORIAL_ENDPOINT = "/factorial";
    public static final String NUMBER_PARAM = "number";
    public static final String INVALID_PARAM = "num";
    public static final String FIELD_ANSWER = "answer";
}
