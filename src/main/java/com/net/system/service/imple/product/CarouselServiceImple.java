package com.net.system.service.imple.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.net.common.util.FileUtil;
import com.net.system.mapper.product.CarouselMapper;
import com.net.system.model.Carousel;
import com.net.system.service.product.CarouselService;
@Service
public class CarouselServiceImple implements CarouselService{
	@Resource
	private CarouselMapper  carouselMapper;

	@Value("${photo.resourceLocations}")
	private   String IMG_PATH_PREFIX;
    
    @Value("${photo.photoPath}")
   	private   String photoPath;
    
	@Override
	public int delete(String id) {
		Carousel carousel=carouselMapper.selectById(id);
		if(carousel.getImageUrl()!=null&&!carousel.getImageUrl().equals("")) {
		  String str2=carousel.getImageUrl().substring(photoPath.length(), carousel.getImageUrl().length());
		  FileUtil.delFile(IMG_PATH_PREFIX, str2);
		}
		// TODO Auto-generated method stub
		return carouselMapper.delete(id);
	}

	@Override
	public int insert(Carousel carousel) {
		String files=carousel.getFiles();
		carousel.setId(UUID.randomUUID().toString().replaceAll("-",""));
		//将base64编码的图片转成MultipartFile类型
    	MultipartFile file=FileUtil.base64ToMultipart(files);
    	try {
			String imgUrl=this.uploadImage(file);
			carousel.setImageUrl(imgUrl);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	carousel.setCreateTime(new Date());
		// TODO Auto-generated method stub
		return carouselMapper.insert(carousel);
	}

	@Override
	public int insertDynamic(Carousel carousel) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateDynamic(Carousel carousel) {
		carousel.setUpdateTime(new Date());
		// TODO Auto-generated method stub
		return carouselMapper.updateDynamic(carousel);
	}

	@Override
	public int update(Carousel carousel) {
		String files=carousel.getFiles();
		if(files!=null&&!files.equals("")) {
			try {
				String str2=carousel.getImageUrl().substring(photoPath.length(), carousel.getImageUrl().length());
		       	FileUtil.delFile(IMG_PATH_PREFIX, str2);
		       //将base64编码的图片转成MultipartFile类型
		    	MultipartFile file=FileUtil.base64ToMultipart(files);
				String imgUrl = this.uploadImage(file);
				carousel.setImageUrl(imgUrl);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		carousel.setUpdateTime(new Date());
		// TODO Auto-generated method stub
		return carouselMapper.update(carousel);
	}

	@Override
	public Carousel selectById(String id) {
		// TODO Auto-generated method stub
		return carouselMapper.selectById(id);
	}

	@Override
	public List<Carousel> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return carouselMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return carouselMapper.findPageWithCount(map);
	}

	 public   String uploadImage(MultipartFile file) throws IllegalStateException, IOException {
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
}
