package com.datastory.commons3.monitor.utils;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 监控配置信息
 */
public class MonitorConf {

    static Logger logger = Logger.getLogger(MonitorConf.class);

    //url的配置文件
    //public static String URL_PATH = "E:\\wordspacesec\\HbaseMonitor-master\\src\\main\\resources\\mon-config.xml";
    //public static String URL_KEY = "hbase.jmx.urls";

    //需要请求的url集合信息
    public static List<String> requestUrls = new ArrayList<String>();

    //json节点之间的分隔符
    public static final String JSON_NODE_SEGMENTATION = ".";

    /**
     * 获取请求的url集合
     */
    public List<String> getRequestUrls(String urlConf){

        SAXReader reader = null;
        try{
            File xmlfile = new File(urlConf);
            reader = new SAXReader();
            Document doc = reader.read(xmlfile);
            Element root = doc.getRootElement();
            List<Element> childElements = root.elements();
            for (Element child : childElements) {//循环输出全部book的相关信息
                List<Element> books = child.elements();
                for (Element book : books) {
                    String text = book.getText();//获取当前元素值
                    if(text.startsWith("http")){
                        String[] urlArray = text.split(" ");
                        for (String url:urlArray) {
                            requestUrls.add(url);
                        }
                    }
                }
            }
//            for (Iterator<Element> i = root.elementIterator(URL_KEY); i.hasNext();) {
//                String url = i.next().getData().toString();
//                System.out.println("url:"+url);
//                requestUrls.add(url);
//            }
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
        return requestUrls;
    }
}
