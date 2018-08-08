package com.datastory.commons3.monitor.writer;

import java.util.Map;

/**
 * 写存储系统的接口
 */
public interface Writer {

    //write to local file
    public void write(String host, Map<String, Object> targetMap);

    //write to influxdb
    public void writeToInflux(String host, Map<String, Object> targetMap);

}
