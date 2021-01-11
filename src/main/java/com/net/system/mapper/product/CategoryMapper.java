package com.net.system.mapper.product;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.net.system.model.Category;
@Mapper
public interface CategoryMapper {
    int delete(String fCateid);

    int insert(Category category);

    int insertDynamic(Category category);

    int updateDynamic(Category category);

    int update(Category category);

    Category selectByFCateid(String fCateid);

    List<Category> findPageWithResult(Map<String,Object> map);

    Integer findPageWithCount(Map<String,Object> map);
    /**
     * 查询所以商品分类
     * @return
     */
    List<Category> selectAllCategoryTree();
    /**
         * 查询某分类下所有子分类
     * @param fCateid
     * @return
     */
    List<String> selectChildrenID(String fCateid);
    /**
         * 根据父分类获得子分类
     * @param fParentid
     * @return
     */
    List<Category> selectByfparentid(String fParentid);
    
    Integer swapSort(@Param("currentId") String currentId, @Param("swapId") String swapId);
    
    String getCategoryId();
    
    Integer getOrderMax(String fParentid);
}
