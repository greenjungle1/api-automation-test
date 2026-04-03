package api.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features   = "src/test/java/api/automation/features",
        glue       = {"api.automation.steps", "api.automation.hooks"},
        tags       = "not @ignore",
        plugin     = {
                "pretty",
                "summary",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class FactorialTestRunner extends AbstractTestNGCucumberTests {
}
