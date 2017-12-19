package org.fxclub.qa.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;

public class InfluxDBWriter {

    private String host;
    private String dbname;

    public InfluxDBWriter(String host, String dbname){
        this.host = host;
        this.dbname = dbname;
    }

    public void writeSinglePoint(Point point){
        InfluxDB influxDB = InfluxDBFactory.connect("http://"+host+":8086");
        influxDB.createDatabase(dbname);

        BatchPoints batchPoints = BatchPoints
                .database(dbname)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();

        batchPoints.point(point);
        influxDB.write(batchPoints);
        influxDB.close();
    }

}
