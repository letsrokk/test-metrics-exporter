package org.fxclub.qa.cucumber.influxdb;

import cucumber.api.Result;
import cucumber.api.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxclub.qa.influxdb.InfluxDBWriter;
import org.influxdb.dto.Point;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class InfluxDBSummaryFormatter implements ConcurrentEventListener {

    private Logger logger = LogManager.getLogger(InfluxDBSummaryFormatter.class);

    private final Properties influxdbProperties = new Properties();

    private static AtomicInteger totalStarted = new AtomicInteger(0);
    private static AtomicInteger totalEnabled = new AtomicInteger(0);
    private static AtomicInteger totalUndefined = new AtomicInteger(0);

    private final EventHandler<TestCaseStarted> caseStartedHandler = this::handleTestCaseStarted;
    private final EventHandler<TestCaseFinished> caseFinishedHandler = this::handleTestCaseFinished;
    private final EventHandler<TestRunFinished> runFinishedHandler = this::handleTestRunFinished;

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, caseFinishedHandler);
        eventPublisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestCaseStarted(final TestCaseStarted event) {
        totalStarted.incrementAndGet();
    }

    private void handleTestCaseFinished(final TestCaseFinished event) {
        if (event.result.getStatus() == Result.Type.UNDEFINED) {
            totalUndefined.incrementAndGet();
        } else {
            totalEnabled.incrementAndGet();
        }
    }

    private void handleTestRunFinished(final TestRunFinished event) {
        storeSummary();
    }

    private void storeSummary(){
        try {
            File projectDir = new File(System.getProperty("user.dir"));

            influxdbProperties.load(this.getClass().getClassLoader().getResourceAsStream("influxdb-export.properties"));

            String project_name = influxdbProperties.getProperty("project.name", projectDir.getName());
            logger.debug("Project Name: " + project_name);

            String suite_name = influxdbProperties.getProperty("suite.name", projectDir.getName());
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
