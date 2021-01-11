package com.net.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.stream.FileImageInputStream;

public class Base64Util {

	/**
     * 字符串转图片
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str){
        byte[] b = null;
        Decoder decoder = Base64.getDecoder();
        b = decoder.decode(replaceEnter(base64Str));
        return b;
    }
    
    /**
     * 图片转字符串
     * @param image
     * @return
     */
    public static String encode(byte[] image){
        Encoder decoder = Base64.getEncoder();
        return replaceEnter(new String(decoder.encode(image)));
    }
    
    public static String encode(String uri){
    	Encoder encoder =Base64.getEncoder();
        return replaceEnter(new String(encoder.encode(uri.getBytes())));
    }
    
    /**
     * 
     * @path    图片路径
     * @return
     */
    
    public static byte[] imageTobyte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while((numBytesRead = input.read(buf)) != -1){
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
    
    
    
    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
    
}
