package com.net.common.util;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
 
/**
 * @date: 2018/11/28 17:17
 * @author: YINLELE
 * @description: http请求的工具
 */
@SuppressWarnings("deprecation")
public class HttpUtil {
    public static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
 
    /**
     * @param url
     * @param formparams (BasicNameValuePair)
     * @return
     */
    public static String postForm(String url, List<NameValuePair> formparams) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        httppost.setConfig(requestConfig);
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String result = EntityUtils.toString(entity, "UTF-8");
                    logger.info("--------------------------------------");
                    logger.info("postForm====> Response content: " + result);
                    logger.info("--------------------------------------");
                    return result;
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }
 
    /**
     * postSoapXml
     *
     * @param url
     * @param str
     * @return
     */
    public static String postSoapXml(String url, String str) {
        HttpPost httppost = new HttpPost(url);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            httppost.setConfig(requestConfig);
            httppost.addHeader("content-type", "application/soap+xml; charset=utf-8");
            StringEntity myEntity = new StringEntity(str, "UTF-8");
            httppost.setEntity(myEntity);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                logger.info("postSoapXml====> Response :result"+result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }
 
    /**
     * post/json
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doPostJson(String url, String jsonStr) {
        String result = null;
        HttpPost httppost = new HttpPost(url);
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httppost.setConfig(requestConfig);
            httppost.addHeader("content-type", "application/json");
            StringEntity myEntity = new StringEntity(jsonStr, "UTF-8");
            httppost.setEntity(myEntity);
            HttpResponse response = httpclient.execute(httppost);
            // 返回的状态码
            System.out.println(response.getStatusLine().getStatusCode());
 
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                logger.info("--------------------------------------");
                logger.info("doPostJson====> Response content: " + result);
                logger.info("--------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return result;
    }
 
    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
 
        HttpGet httpget = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
        httpget.setConfig(requestConfig);
        try {
            System.out.println("executing request " + httpget.getURI());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                logger.info("--------------------------------------");
                logger.info(response.getStatusLine()+"");
                if (entity != null) {
                    result = EntityUtils.toString(entity, "UTF-8");
                    logger.info("--------------------------------------");
                    logger.info("get====> Response content: " + result);
                    logger.info("--------------------------------------");
                }
                logger.info("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpget.releaseConnection();
        }
        return result;
    }
 
 
    /**
     * httpssl请求
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String doSSLPost(String url, String jsonStr) {
        HttpClient httpclient = null;
        HttpPost httppost = new HttpPost(url);
        String result = "";
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
            SSLContext ctx = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(ctx, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(requestConfig).build();
            httppost.addHeader("content-type", "application/json");
            StringEntity myEntity = new StringEntity(jsonStr, "UTF-8");
            httppost.setEntity(myEntity);
            HttpResponse response = httpclient.execute(httppost);
            // 返回的状态码
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                logger.info("--------------------------------------");
                logger.info("doSSLPost====> Response content: " + result);
                logger.info("--------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return result;
    }
}


