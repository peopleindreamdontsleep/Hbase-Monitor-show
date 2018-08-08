package com.datastory.commons3.monitor.writer.impl;

import com.datastory.commons3.monitor.writer.Writer;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 写本地文件的Writer
 */
public class LocalFileWriter implements Writer {

    static Logger logger = Logger.getLogger(LocalFileWriter.class);

    private String path;
    private String encoding;

    public LocalFileWriter(){}

    public LocalFileWriter(String path, String encoding) {
        this.path = path;
        this.encoding = encoding;
    }

    /**
     * 将数据写到本地存储系统中去
     *
     * @param targetMap
     */
    public void write(String host, Map<String, Object> targetMap) {
        File file = new File(this.path);
        file = file.getAbsoluteFile();
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(path), true), encoding));
            writer.write("机器[" + host + "]的hbase指标信息如下:");
            writer.newLine();
            for (String targetKey : targetMap.keySet()) {
                writer.write(targetKey + ":" + targetMap.get(targetKey));
                writer.newLine();
            }
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 将数据写到influxdb中去
     *
     * @param targetMap
     */
    public void writeToInflux(String host, Map<String, Object> targetMap) {

        try {
            String hostName = host.split("//")[1].split(":")[0];
            Map<String, String> tags = new HashMap<>();
            Map<String, Object> fields = new HashMap<>();
            for (String targetKey : targetMap.keySet()) {
                if(host.contains("16010")&&!(targetKey.contains("Master"))){
                    continue;
                }
                Object wirteValue = targetMap.get(targetKey);
                if(wirteValue != null){
                    String[] splitDesc = targetKey.split("\\.");
                    String tagDesc = splitDesc[0];
                    String tagName = splitDesc[1];
                    tags.put("TAG_NAME",tagName);
                    tags.put("TAG_DESC",tagDesc);
                    tags.put("TAG_HOSTNAME",hostName);
                    fields.put("TAG_VALUE",targetMap.get(targetKey));
                    fields.put("TIMAMPEST", new Date().getTime());
                    logger.info("write to influxdb "+hostName+"\t"+tagName+"\t"+tagDesc+"\t"+targetMap.get(targetKey));
                    QuatyServiceImpl.intoDb(tagName,tags,fields);
                    tags.clear();
                    fields.clear();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
