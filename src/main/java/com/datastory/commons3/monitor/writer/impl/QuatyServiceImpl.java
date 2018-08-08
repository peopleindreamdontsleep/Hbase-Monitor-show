package com.datastory.commons3.monitor.writer.impl;

import com.datastory.commons3.monitor.utils.InfluxDbUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuatyServiceImpl {

    private static InfluxDbUtils influxDB;

    public static void intoDb(String measurement,Map<String, String> tags,Map<String, Object> fields) {
        influxDB = InfluxDbUtils.setUp();
        influxDB.insert(measurement,tags, fields);
    }

}
