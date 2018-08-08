package com.datastory.commons3.monitor.executor.impl;

import com.datastory.commons3.monitor.executor.MonitorExecutor;
import com.datastory.commons3.monitor.utils.JsonUtils;
import com.datastory.commons3.monitor.utils.RequestUtils;
import com.datastory.commons3.monitor.writer.impl.LocalFileWriter;
import com.datastory.commons3.monitor.utils.StringUtils;
import com.datastory.commons3.monitor.writer.Writer;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对hbase的指标进行监控
 */
public class HbaseMonitorExecutor implements MonitorExecutor {

    //配置key和指标的映射关系
    Map<String, String> keyMappings = new LinkedHashMap<String, String>();

    public HbaseMonitorExecutor() throws IOException {
        //jvm的指标信息
        InputStream in = getClass().getClassLoader().getResourceAsStream("hbase.jmx.paths");

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in));
        String str = null;
        while (true) {
            str = reader.readLine();
            if(str!=null){
                String[] arr = str.trim().split("\t");
                keyMappings.put(arr[0], arr[1]);
            }
            else
                break;
        }
    }

    /**
     * 具体的业务执行实现方法
     *
     * @param requestUrls
     */
    public void executor(List<String> requestUrls) {
        //Writer writer = new LocalFileWriter("result.txt","utf-8");
        Writer writer = new LocalFileWriter();
        for (String requestUrl : requestUrls) {
            //根据url获取请求的信息
            String jsonRet = RequestUtils.getJsonByUrl(requestUrl);
            if (StringUtils.isNullStr(jsonRet)) {
                continue;
            }
            //0.96的"tag.Context"有两个，直接解析会报错

            //将请求到的数据转换为json对象
           // System.out.println(jsonRet);
            JSONObject object = JsonUtils.getJsonObject(jsonRet);
            //对json的数据进行解析，得到对应的vlaue
            Map<String, Object> targetMap = parseJsonObject(object, keyMappings);
            writer.writeToInflux(requestUrl, targetMap);
            //writer.write(requestUrl,targetMap);
        }
    }

    /**
     * 解析json对象，获取相应的值
     *
     * @param object
     * @param keyMappings
     * @return
     */
    private Map<String, Object> parseJsonObject(JSONObject object, Map<String, String> keyMappings) {
        Map<String, Object> targetMap = new LinkedHashMap<String, Object>();
        for (String key : keyMappings.keySet()) {
            //获取json标签的值
            Object value = JsonUtils.getValue(object, key);
            //将新填值之后的信息放入到集合中去
            targetMap.put(keyMappings.get(key), value);
        }
        return targetMap;
    }

}
