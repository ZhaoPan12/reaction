package com.net.common.aop;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @desc 请求数据解密
 */
@RestControllerAdvice(basePackages = "com.net.system.controller")
public class MyRequestBodyAdvice implements RequestBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(MyRequestBodyAdvice.class);

    private AesEncryptUtil aesEncrypt=AesEncryptUtil.getAesEncryptUtil();;
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
    	 
        return methodParameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            boolean encode = false;
            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
                //获取注解配置的包含和去除字段
                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
                //入参是否需要解密
                encode = serializedField.inDecode();
            }
            if (encode) {
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密");
                HttpInputMessage httpInputMessage= new MyHttpInputMessage(inputMessage);
                return httpInputMessage;
            }else{
                return inputMessage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常："+e.getMessage());
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

 class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
        	System.out.println(IOUtils.toString(inputMessage.getBody(), "UTF-8"));
        	String mes=aesEncrypt.desEncrypt(IOUtils.toString(inputMessage.getBody(), "UTF-8"));
        	
            this.headers = inputMessage.getHeaders();
            this.body = IOUtils.toInputStream(mes, "UTF-8");
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

        public String easpString(String requestData){
            LoggerFactory.getLogger("接收加密数据").info(requestData);
            if(requestData != null && !requestData.equals("")){
                if(!requestData.startsWith("{\"requestData\":")){
                    throw new RuntimeException("参数【requestData】缺失异常！");
                }else{
                    int closeLen = requestData.length()-2;
                    int openLen = "{\"requestData\":".length()+1;
                    return StringUtils.substring(requestData,openLen,closeLen);
                }
            }
            return "";
        }
    }
}

