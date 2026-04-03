package api.automation.hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import api.automation.utils.SpecBuilder;

public class Hooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("\n==================================================");
        System.out.println("  SCENARIO : " + scenario.getName());
        System.out.println("  Tags     : " + scenario.getSourceTagNames());
        System.out.println("==================================================");
        SpecBuilder.configure();
    }

    @After
    public void afterScenario(Scenario scenario) {
        System.out.println("\n==================================================");
        System.out.println("  FINISHED : " + scenario.getName());
        System.out.println("  Status   : " + scenario.getStatus());
        System.out.println("==================================================\n");
    }
}
