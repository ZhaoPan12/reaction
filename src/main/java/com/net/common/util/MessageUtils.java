package com.net.common.util;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;
import com.net.system.model.vo.TextMessage;
import com.thoughtworks.xstream.XStream;


public class MessageUtils {
	
	private static final String GET_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?";
	private static final String APPID="wxe783b74792f35b75";
	private static final String APPSECRET="b7f343165603b2b32e3beb84219956a6";

	
	
	public static String get(String url) {
		try {
			URL urlObject=new URL(url);
			URLConnection connection=urlObject.openConnection();
			InputStream is=connection.getInputStream();
			byte[] b=new byte[1024];
			int len;
			StringBuffer sb=new StringBuffer();
			while((len=is.read(b))!=-1) {
				sb.append(new String(b,0,len));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
		
	}
	
	public static void main(String[] agrs) {
		Map<String,List<Map<String,Object>>> map=new HashMap<String,List<Map<String,Object>>>();
		List<Map<String,Object>> maps=new ArrayList<Map<String,Object>>();
		Map<String,Object> mapp=new HashMap<String, Object>();
		mapp.put("type", "view");
		mapp.put("name","旅游1");
		mapp.put("url", "https://www.zyyg0523.cn/tzsmt/one.html");
		maps.add(mapp);
		mapp.put("type", "view");
		mapp.put("name","旅游2");
		mapp.put("url", "https://www.zyyg0523.cn/tzsmt/nkgm.html");
		maps.add(mapp);
		map.put("button", maps);
		System.out.println(JSONObject.toJSON(map));
		String url=GET_TOKEN_URL+"grant_type=client_credential&appid="+APPID+"&secret="+APPSECRET;
		String param=HttpUtil.get(url);
		JSONObject json=JSONObject.parseObject(param);
		System.out.print("token："+json.get("access_token"));
		String vuem="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+json.get("access_token");
		String iii="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+json.get("access_token")+"&openid=o3uSv4gROyDGR6j8pExDn7xeaV8Q&lang=zh_CN";
		String params=HttpUtil.doPostJson(vuem, JSONObject.toJSON(map).toString());
		
		//String params=HttpUtil.doPostJson(vuem, "");
		System.out.println("菜单返回："+params);
	}
    /**
     * xml数据转map
     *
     * @param request
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();

        InputStream inputStream = request.getInputStream();
        Document document = reader.read(inputStream);

        Element root = document.getRootElement();
        List<Element> list = root.elements();

        for (Element element : list) {
            map.put(element.getName(), element.getText());
        }

        inputStream.close();
        return map;
    }

    /**
     * 将文本消息对象转换成xml
     *
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage) {
        XStream xStream = new XStream();
        // 将xml的根元素替换成xml
        xStream.alias("xml", textMessage.getClass());
        return xStream.toXML(textMessage);
    }
}