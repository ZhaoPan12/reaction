package com.net.system.service.imple.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.net.common.util.FileUtil;
import com.net.system.mapper.product.CategoryMapper;
import com.net.system.model.Category;
import com.net.system.model.Dept;
import com.net.system.service.product.CategoryService;
@Service
public class CategoryServiceImple implements CategoryService {

	@Resource
	private CategoryMapper categoryMapper;
	
	@Value("${photo.resourceLocations}")
	private   String IMG_PATH_PREFIX;
    
    @Value("${photo.photoPath}")
   	private   String photoPath;
	
	private FileUtil fileUtil=new FileUtil();
	 /**
	  * 删除当前分类及子分类.
     */
	@Override
	public int delete(String fCateid) {
		List<String> childIDList = categoryMapper.selectChildrenID(fCateid);
        for (String childId : childIDList) {
        	delete(childId);
        }
		// TODO Auto-generated method stub
		return categoryMapper.delete(fCateid);
	}

	@Override
	public int insert(Category category) {
		category.setfCreatetime(new Date());
		category.setfCateid(String.valueOf(getCategoryId()));
		category.setfStatus(1);
		Integer orderNum=getOrderMax(category.getfParentid());
		if(orderNum!=null) {
			category.setfSortorder(orderNum+1);
		}else {
			category.setfSortorder(1);
		}
		if(category.getFileimg()!=null) {
			FileUtil file=new FileUtil();
			try {
				MultipartFile files=FileUtil.base64ToMultipart(category.getFileimg());
				String url=file.uploadImage(files,IMG_PATH_PREFIX,photoPath);
				category.setImageUrl(url);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		return categoryMapper.insert(category);
	}

	@Override
	public int insertDynamic(Category category) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateDynamic(Category category) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Category category) {
		category.setfUpdatetime(new Date());
		
		if(category.getFileimg()!=null) {
			try {
				if(!category.getFileimg().startsWith("http")) {
					MultipartFile files=FileUtil.base64ToMultipart(category.getFileimg());
					category.setImageUrl(fileUtil.uploadImage(files,IMG_PATH_PREFIX,photoPath));
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String str2=category.getOriginalUrl().substring(photoPath.length(), category.getOriginalUrl().length());
		FileUtil.delFile(IMG_PATH_PREFIX, str2);
		// TODO Auto-generated method stub
		return categoryMapper.update(category);
	}

	@Override
	public Category selectByFCateid(String fCateid) {
		// TODO Auto-generated method stub
		return categoryMapper.selectByFCateid(fCateid);
	}

	@Override
	public List<Category> findPageWithResult(Map<String, Object> map) {
		Integer page=(Integer) map.get("page");
		Integer rows=(Integer) map.get("rows");
		//PageHelper.startPage(page, rows);
		// TODO Auto-generated method stub
		return categoryMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> selectAllCategoryTree() {
		// TODO Auto-generated method stub
		return categoryMapper.selectAllCategoryTree();
	}

	@Override
	public List<Category> selectByfparentid(String fParentid) {
		// TODO Auto-generated method stub
		return categoryMapper.selectByfparentid(fParentid);
	}

	@Override
	public List<Category> selectAllCategoryTreeAndRoot() {
		List<Category> list=categoryMapper.selectAllCategoryTree();
		Category root = new Category();
	        root.setfCateid("0");
	        root.setfName("商品分类");
	        root.setChildren(list);
	        List<Category> rootList = new ArrayList<>();
	        rootList.add(root);
		return rootList;
	}

	@Override
	public Integer swapSort(String currentId, String swapId) {
		// TODO Auto-generated method stub
		return categoryMapper.swapSort(currentId, swapId);
	}
	
	public Integer getCategoryId() {
		String str=categoryMapper.getCategoryId();
		if(str==null) {
			str="0";
		}
		return Integer.parseInt(str)+1;
	}

	public Integer getOrderMax(String fParentid) {
		return categoryMapper.getOrderMax(fParentid);
		
	}
}
