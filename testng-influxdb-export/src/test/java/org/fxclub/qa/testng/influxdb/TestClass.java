package org.fxclub.qa.testng.influxdb;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestClass {

    @Test
    public void testMethod1(){

    }

    @Test(enabled = false)
    public void testMethod2(){

    }

    @DataProvider(name = "dataProvider")
    public Iterator<Object[]> dataProvider() {
        List<Object[]> cases = Arrays.asList(
                new Object[]{"asd","asd"},
                new Object[]{"dsa","das"}
        );
        return cases.iterator();
    }

    @Test(dataProvider = "dataProvider")
    public void testMethodWithDataProvider(String param, String param2){

    }

}
