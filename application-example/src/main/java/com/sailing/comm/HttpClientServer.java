package com.sailing.comm;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 专门提供通过ApacheHttpClient的方式进行访问远端的http请求地址，并获取返回值对象,
 * 目前主要涉及到GET与POST请求模式
 */
@Slf4j
public class HttpClientServer {



    /**
     * HTTP请求前缀
     */
    public static final String HTTPHEAD = "http://";

    /**
     * 常量冒号
     */
    public static final String STRMH = ":";

    /**
     * 根据入参url以及参数执行对应的GET请求，并将请求结果进行返回
     * @param url
     * @param param
     * @return
     */
    private static final String execHttpGet(String url, Object param, String authorization) throws Exception{
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse resp = null;
        String responseBody = "";

        try {
            httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type","application/json");
            if(!StringUtils.isBlank(authorization)){
                httpGet.setHeader("Authorization",authorization);
            }
            log.debug("执行请求的URI为 : " + httpGet.getURI());
            resp = httpclient.execute(httpGet);
            HttpEntity entity = resp.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (resp != null){
                    resp.close();
                }
                if (httpclient != null){
                    httpclient.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
        return responseBody;
    }

    /**
     * 根据入参url执行对应的GET请求，并将请求结果进行返回
     * @param url
     * @param authorization
     * @return
     * @throws Exception
     */
    public static final String execHttpGet(String url, String authorization) throws Exception{
        return execHttpGet(url,"",authorization);
    }

    /**
     * 根据入参url执行对应的GET请求，并将请求结果进行返回
     * @param url
     * @return
     */
    public static final String execHttpGet(String url) throws Exception{
        return execHttpGet(url, "");
    }

    /**
     * 根据入参url以及参数执行对应的POST请求，并将请求结果进行返回
     * @param url
     * @param formparams
     * List<NameValuePair> formparams = new ArrayList<NameValuePair>();
     * formparams.add(new BasicNameValuePair("参数名", ticket.getValue()));
     * @return
     * @throws Exception
     */
    public static final String execHttpPost(String url, List<NameValuePair> formparams) throws Exception{
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse resp = null;
        String responseBody = "";

        try {
            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            log.debug("执行请求的URI为 : " + httppost.getURI());
            resp = httpclient.execute(httppost);
            HttpEntity entity = resp.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (resp != null){
                resp.close();
            }
            if (httpclient != null){
                httpclient.close();
            }
        }
        return responseBody;
    }

    /**
     * 根据入参url以及参数执行对应的POST请求，并将请求结果进行返回
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static final String execHttpPost(String url, Map map) throws Exception{
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse resp = null;
        String responseBody = "";

        try {
            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);

            StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(map),"utf-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httppost.setEntity(stringEntity);
            log.debug("执行请求的URI为 : " + httppost.getURI());
            resp = httpclient.execute(httppost);
            HttpEntity entity = resp.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (resp != null){
                resp.close();
            }
            if (httpclient != null){
                httpclient.close();
            }
        }
        return responseBody;
    }

    /**
     * 根据入参url以及参数执行对应的POST请求，并将请求结果进行返回
     * @param url
     * @param requestBody JSONObject.toJSONString(requestBody)
     * @return
     * @throws Exception
     */
    public static final String execHttpPost(String url, String requestBody) throws Exception{
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse resp = null;
        String responseBody = "";

        try {
            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);

            StringEntity stringEntity = new StringEntity(requestBody,"utf-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httppost.setEntity(stringEntity);
            resp = httpclient.execute(httppost);
            HttpEntity entity = resp.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (resp != null){
                resp.close();
            }
            if (httpclient != null){
                httpclient.close();
            }
        }
        return responseBody;
    }
}
