package org.fxclub.qa.cucumber.influxdb;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
        dryRun = true,
        glue = {"org.fxclub.qa.cucumber.influxdb"},
        features = {"src/test/resources/features"},
        plugin = {"org.fxclub.qa.cucumber.influxdb.InfluxDBSummaryFormatter", "null_summary"}
)
public class DryRunExportTest extends AbstractTestNGCucumberTests {

}
