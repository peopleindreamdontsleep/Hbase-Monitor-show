package com.datastory.commons3.monitor.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json字符串的工具类
 */
public class JsonUtils {

    /**
     * 将字符串转换成一个json格式的对象
     * @param jsonRet
     * @return
     */
    public static JSONObject getJsonObject(String jsonRet){
        if(StringUtils.isNullStr(jsonRet)){
            return null;
        }
        jsonRet = jsonRet.replaceAll("/n","");

        //原生的jsonobject不支持重复的key，继承，重写
        JSONObject jsonObject = new JSONObject(jsonRet);
        return jsonObject;
    }

    /**
     * 解析json，得到对应key的value
     * @param jsonObject
     * @param key
     * @return
     */
    public static Object getValue(JSONObject jsonObject, String key){
        //获取第一级别的key信息
        String[] keySplits = key.split("\\" + MonitorConf.JSON_NODE_SEGMENTATION);
        if(keySplits == null || keySplits.length == 0){
            return null;
        }
        //表示已经到了最末端，可以直接取数据了
        if(keySplits.length == 1){
            //使用正则进行解析
            if(key.contains("*")){
                Map<String,Object> objectMap = jsonObject.toMap();
                //对数据进行聚合
                long sum = 0;
                for(String objectKey : objectMap.keySet()){
                    Pattern p = Pattern.compile(key);
                    Matcher m = p.matcher(objectKey);
                    if(m.find()){
                        Object obj = objectMap.get(objectKey);
                        if(obj instanceof Integer){
                            sum += ((Integer) obj).intValue();
                        }
                    }
                }
                return sum;
            }else{
                return jsonObject.has(key) ? jsonObject.get(key) : null;
            }
        }
        //只读取一级
        String keySplit = keySplits[0];
        //取后面的字符串
        String nextSplit = key.substring(keySplit.length() + MonitorConf.JSON_NODE_SEGMENTATION.length(),key.length());

        if (jsonObject.get(keySplit) instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) jsonObject.get(keySplit);
            //下一级别一定是包含属性的，能定位到某一个具体的Object
            keySplits = nextSplit.split("\\" + MonitorConf.JSON_NODE_SEGMENTATION);//name[songhao].job
            //只读取一级
            keySplit = keySplits[0];//name[songhao]

            if(keySplit.contains("[") && keySplit.contains("]")){
                //获取[]里面的属性值
                String propValue = keySplit.substring(keySplit.indexOf("[") + 1,keySplit.indexOf("]"));
                keySplit = keySplit.substring(0,keySplit.indexOf("["));

                int size = jsonArray.length();
                for(int i = 0; i < size; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.getString(keySplit);
                    //判断节点是否是我们需要的
                    if (name.equals(propValue)) {
                        //取后面的
                        nextSplit = nextSplit.substring(nextSplit.indexOf(MonitorConf.JSON_NODE_SEGMENTATION) + 1,nextSplit.length());
                        //拿到了一个具体的JSONObject
                        return getValue(object,nextSplit);
                    }
                }
            }
        } else {
            jsonObject = (JSONObject) jsonObject.get(keySplit);
            return getValue(jsonObject,nextSplit);
        }
        return null;
    }
}
