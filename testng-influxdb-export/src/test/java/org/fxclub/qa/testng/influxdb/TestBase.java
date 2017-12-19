package org.fxclub.qa.testng.influxdb;

import org.testng.annotations.BeforeSuite;

public class TestBase {

    @BeforeSuite
    public void beforeSuiteTestBase(){
        System.out.println("EXECUTED: beforeSuiteTestBase");
    }

}
