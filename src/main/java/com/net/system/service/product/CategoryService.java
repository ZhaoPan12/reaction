package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.net.system.model.Category;

public interface CategoryService {

	int delete(String fCateid);

    int insert(Category category);

    int insertDynamic(Category category);

    int updateDynamic(Category category);

    int update(Category category);

    Category selectByFCateid(String fCateid);

    List<Category> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    
    List<Category> selectAllCategoryTree();
    
    /**
         * 根据父分类获得子分类
	 * @param fParentid
	 * @return
	 */
	List<Category> selectByfparentid(String fParentid);
	
	List<Category> selectAllCategoryTreeAndRoot();
	
	Integer swapSort(String currentId,String swapId);
}
