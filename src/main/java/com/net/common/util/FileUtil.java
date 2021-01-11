package com.net.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.net.system.model.vo.BASE64DecodedMultipartFile;

public class FileUtil {

	public static MultipartFile base64ToMultipart(String base64) {
	    String[] baseStrs = base64.split(",");

		Decoder decoder = Base64.getDecoder();  
		byte[] b = new byte[0];
		b = decoder.decode(baseStrs[1]);

		for(int i = 0; i < b.length; ++i) {
		    if (b[i] < 0) {
		        b[i] += 256;
		    }
		}

		return new BASE64DecodedMultipartFile(b, baseStrs[0]);
	}
	

    
    /**删除文件
     * @param path 目录
     * @param filename 文件名
     */
    public static boolean delFile (String path,String filename) {
      File file=new File(path + "/" + filename);
      if(file.exists() && file.isFile()) {
    	  return file.delete();
      }
      	return false;
    }
    
    /**
     * 本地图片转换Base64的方法
     *
     * @param imgPath     
     */
    public static String GetImageStr(String imgFilePath) {  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            InputStream in = new FileInputStream(imgFilePath);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 对字节数组Base64编码  
        Encoder encoder = Base64.getEncoder();
        return new String(encoder.encode(data));// 返回Base64编码过的字节数组字符串  
    }  
    
    /**
     * 通过图片的url获取图片的base64字符串
     * @param imgUrl    图片url
     * @return    返回图片base64的字符串
     */
    public static String image2Base64(String imgUrl) {  

        URL url = null;  

        InputStream is = null;   

        ByteArrayOutputStream outStream = null;  

        HttpURLConnection httpUrl = null;  

        try{  

            url = new URL(imgUrl);  

            httpUrl = (HttpURLConnection) url.openConnection();  

            httpUrl.connect();  

            httpUrl.getInputStream();  

            is = httpUrl.getInputStream();            

              

            outStream = new ByteArrayOutputStream();  

            //创建一个Buffer字符串  

            byte[] buffer = new byte[1024];  

            //每次读取的字符串长度，如果为-1，代表全部读取完毕  

            int len = 0;  

            //使用一个输入流从buffer里把数据读取出来  

            while( (len=is.read(buffer)) != -1 ){  

                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  

                outStream.write(buffer, 0, len);  

            }  

            // 对字节数组Base64编码  

            return Base64Util.encode(outStream.toByteArray());  

        }catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            if(is != null){  
                try {  
                    is.close();  
                } catch (IOException e) {
                    e.printStackTrace();  

                }  
            }  
            if(outStream != null){  
                try {  
                    outStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if(httpUrl != null) {  
                httpUrl.disconnect();  
            }  
        }  
        return imgUrl;  

    }  
    
    public String uploadImage(MultipartFile file,String IMG_PATH_PREFIX,String photoPath) throws IllegalStateException, IOException {
        String filename = UUID.randomUUID().toString().replaceAll("-", "");
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String filenames = filename + ext;
        String tagFilePath = IMG_PATH_PREFIX + filenames;// 单文件
        File dest = new File(tagFilePath);
        if (!dest.getParentFile().exists()) {
         dest.getParentFile().mkdirs();
        }
        // 执行流写入
        file.transferTo(dest);
		return photoPath+filenames;
    }
    
    public boolean deleteImage(String imageUrl,String IMG_PATH_PREFIX,String photoPath) {
    	System.out.println(imageUrl);
    	 String str2=imageUrl.substring(photoPath.length(), imageUrl.length());
    	 System.out.println(str2);
    	 FileUtil.delFile(IMG_PATH_PREFIX, str2);
    	 return true;
    }
}
