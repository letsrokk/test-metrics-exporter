package org.fxclub.qa.testng.influxdb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxclub.qa.influxdb.InfluxDBWriter;
import org.influxdb.dto.Point;
import org.testng.IAnnotationTransformer3;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class InfluxDBExportListener implements IAnnotationTransformer3, ISuiteListener {

    private Logger logger = LogManager.getLogger(InfluxDBExportListener.class);

    private final Properties influxdbProperties = new Properties();


    private static AtomicInteger totalStarted = new AtomicInteger(0);
    private static AtomicInteger totalEnabled = new AtomicInteger(0);
    private static AtomicInteger totalUndefined = new AtomicInteger(0);


    @Override
    public void transform(IListenersAnnotation iListenersAnnotation, Class aClass) {

    }

    @Override
    public void transform(IConfigurationAnnotation iConfigurationAnnotation, Class aClass, Constructor constructor, Method method) {

    }

    @Override
    public void transform(IDataProviderAnnotation iDataProviderAnnotation, Method method) {

    }

    @Override
    public void transform(IFactoryAnnotation iFactoryAnnotation, Method method) {

    }

    @Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        totalStarted.incrementAndGet();

        if(iTestAnnotation.getEnabled()) {
            totalEnabled.incrementAndGet();
            iTestAnnotation.setEnabled(false);
        } else {
            totalUndefined.incrementAndGet();
        }
    }

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        try {
            File projectDir = new File(System.getProperty("user.dir"));

            influxdbProperties.load(this.getClass().getClassLoader().getResourceAsStream("influxdb-export.properties"));

            String project_name = influxdbProperties.getProperty("project.name", projectDir.getName());
            logger.debug("Project Name: " + project_name);

            String suite_name = influxdbProperties.getProperty("suite.name", iSuite.getName());
            logger.debug("Test Suite: " + suite_name);

            String influxdb_host = influxdbProperties.getProperty("influxdb.host");
            String influxdb_dbname = influxdbProperties.getProperty("influxdb.dbname");
            logger.debug("InfluxDB Host: " + influxdb_host);
            logger.debug("InfluxDB Name: " + influxdb_dbname);

            Point point1 = Point.measurement("suite_stats")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("project_name", project_name)
                    .tag("suite_name", suite_name)
                    .addField("total", totalStarted.get())
                    .addField("enabled", totalEnabled.get())
                    .addField("pending", totalUndefined.get())
                    .build();
            logger.debug("Data Point: " + point1.toString());

            new InfluxDBWriter(influxdb_host, influxdb_dbname).writeSinglePoint(point1);
        } catch (Exception e) {
            logger.throwing(e);
        }
    }
}
