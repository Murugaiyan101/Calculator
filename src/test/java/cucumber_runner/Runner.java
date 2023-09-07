package cucumber_runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources\\WeatherStations.feature",
        glue = "steps",
        plugin = {"json:target/cucumber-report.json",
                "html:target/cucumber-report.html"})
public class Runner {


}
