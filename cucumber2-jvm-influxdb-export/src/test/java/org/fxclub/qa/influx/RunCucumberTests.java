package org.fxclub.qa.influx;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        dryRun = true,
        strict = true,
        features = "src/test/resources/features/",
        glue = "org.fxclub.qa.influx",
        plugin = {"cucumber.runtime.formatter.InfluxDBSummaryFormatter"}
)
public class RunCucumberTests extends AbstractTestNGCucumberTests {

}
