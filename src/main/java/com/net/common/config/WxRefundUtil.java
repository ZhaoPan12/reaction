package com.net.common.config;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ClassPathResource;
  
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class WxRefundUtil {

	private static byte[] certData;
	
	
	
	/****
	   * @throws Exception
	   */
	  static {
	    try {
	      //从微信商户平台下载的安全证书存放的目录
	      String certPath = "D:\\config\\zzyg_cert.p12";
	      File file = new File(certPath);
	      InputStream certStream = new FileInputStream(file);
	      WxRefundUtil.certData = IOUtils.toByteArray(certStream);
	      certStream.read(WxRefundUtil.certData);
	      certStream.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	
	/**
	   * 开始退款操作
	   *
	   * @param mchId 商户ID
	   * @param url  请求URL
	   * @param data 退款参数
	   * @return
	   * @throws Exception
	   */
	  public static String doRefund(String mchId, String url, String data) throws Exception {
	    /**
	     * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
	     */
	    KeyStore keyStore = KeyStore.getInstance("PKCS12");
	    //这里自行实现我是使用数据库配置将证书上传到了服务器可以使用 FileInputStream读取本地文件
	    //ByteArrayInputStream inputStream = FileUtil.getInputStream("https://############################.p12");
	    ByteArrayInputStream inputStream = new ByteArrayInputStream(WxRefundUtil.certData);
	    try {
	      //这里写密码..默认是你的MCHID
	      keyStore.load(inputStream, mchId.toCharArray());
	    } finally {
	      inputStream.close();
	    }
	    SSLContext sslcontext = SSLContexts.custom()
	        //这里也是写密码的
	        .loadKeyMaterial(keyStore, mchId.toCharArray())
	        .build();
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	        sslcontext,
	        SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	    CloseableHttpClient httpclient = HttpClients.custom()
	        .setSSLSocketFactory(sslsf)
	        .build();
	    try {
	      HttpPost httpost = new HttpPost(url);
	      httpost.setEntity(new StringEntity(data, "UTF-8"));
	      CloseableHttpResponse response = httpclient.execute(httpost);
	      try {
	        HttpEntity entity = response.getEntity();
	        //接受到返回信息
	        String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
	        EntityUtils.consume(entity);
	        return jsonStr;
	      } finally {
	        response.close();
	      }
	    } finally {
	      httpclient.close();
	    }
	  }
}
