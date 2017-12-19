package org.fxclub.qa.cucumber.influxdb;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        dryRun = true,
        strict = true,
        features = {"src/test/resources/features"},
        plugin = {"org.fxclub.qa.cucumber.influxdb.InfluxDBSummaryFormatter"}
)
public class CucumberTestNGDryRunner extends AbstractTestNGCucumberTests {

}
