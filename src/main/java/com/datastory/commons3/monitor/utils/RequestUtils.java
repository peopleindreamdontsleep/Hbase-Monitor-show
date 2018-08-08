package com.datastory.commons3.monitor.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 请求的工具类
 */
public class RequestUtils {

    private static final int timeoutSecond = 20;

    /**
     * 得到HttpClient
     * @return
     */
    public static HttpClient getHttpClient(){
        HttpParams mHttpParams=new BasicHttpParams();
        //设置网络链接超时
        //即:Set the timeout in milliseconds until a connection is established.
        HttpConnectionParams.setConnectionTimeout(mHttpParams, timeoutSecond*1000);
        //设置socket响应超时
        //即:in milliseconds which is the timeout for waiting for data.
        HttpConnectionParams.setSoTimeout(mHttpParams, timeoutSecond*1000);
        //设置socket缓存大小
        HttpConnectionParams.setSocketBufferSize(mHttpParams, 8*1024);
        //设置是否可以重定向
        HttpClientParams.setRedirecting(mHttpParams, true);

        HttpClient httpClient=new DefaultHttpClient(mHttpParams);
        return httpClient;
    }

    /**
     * 根据url获取json内容信息
     * @param url
     * @return
     */
    public static String getJsonByUrl(String url){
        HttpClient httpClient = getHttpClient();
        StringBuilder urlStringBuilder = new StringBuilder(url);
        StringBuilder entityStringBuilder = new StringBuilder();
        //利用URL生成一个HttpGet请求
        HttpGet httpGet = new HttpGet(urlStringBuilder.toString());
        BufferedReader bufferedReader = null;


        String body = null;
        HttpResponse httpResponse = null;
        try {
            //HttpClient发出一个HttpGet请求
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //得到httpResponse的状态响应码
        int statusCode=httpResponse.getStatusLine().getStatusCode();
        if (statusCode== HttpStatus.SC_OK) {
            //得到httpResponse的实体数据
            HttpEntity httpEntity=httpResponse.getEntity();
            if (httpEntity!=null) {
                try {
                    bufferedReader=new BufferedReader
                            (new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8*1024);
                    String line=null;
                    while ((line=bufferedReader.readLine())!=null) {
                        entityStringBuilder.append(line+"/n");
                    }
                    //利用从HttpEntity中得到的String生成JsonObject
                    body = entityStringBuilder.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return body;
    }

    /*public static void main(String[] args){
        String url = "http://datatub1:16030/jmx";
        String jsonRet = getJsonByUrl(url).replaceAll("/n","");
        JvmMetrics jvmMetrics = getJvmMetrics(jsonRet);
    }*/
}
