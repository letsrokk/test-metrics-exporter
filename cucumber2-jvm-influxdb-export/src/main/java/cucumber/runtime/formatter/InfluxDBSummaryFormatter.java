package cucumber.runtime.formatter;

import cucumber.api.Result;
import cucumber.api.event.*;
import cucumber.api.formatter.Formatter;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class InfluxDBSummaryFormatter implements Formatter{

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
        File projectDir = new File(System.getProperty("user.dir"));
        File summary = new File(projectDir.getAbsolutePath() + "/target/influxdb_summary.txt");
        try {
            influxdbProperties.load(this.getClass().getClassLoader().getResourceAsStream("influxdb-export.properties"));

            String suite_name = influxdbProperties.getProperty("suite.name", projectDir.getName());

            String influxdb_host = influxdbProperties.getProperty("influxdb.host");
            String influxdb_dbname = influxdbProperties.getProperty("influxdb.dbname");

            InfluxDB influxDB = InfluxDBFactory.connect("http://"+influxdb_host+":8086");
            influxDB.createDatabase(influxdb_dbname);

            BatchPoints batchPoints = BatchPoints
                    .database(influxdb_dbname)
                    .tag("suite_name", suite_name)
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .build();

            Point point1 = Point.measurement("suite_stats")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("total", totalStarted.get())
                    .addField("enabled", totalEnabled.get())
                    .addField("pending", totalUndefined.get())
                    .build();

            batchPoints.point(point1);
            influxDB.write(batchPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
