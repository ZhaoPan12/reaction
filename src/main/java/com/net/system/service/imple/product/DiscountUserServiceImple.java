package com.net.system.service.imple.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.common.util.DateUtils;
import com.net.common.util.ResultBean;
import com.net.system.mapper.product.CategoryMapper;
import com.net.system.mapper.product.DiscountMapper;
import com.net.system.mapper.product.DiscountUserMapper;
import com.net.system.mapper.product.ProductMapper;
import com.net.system.model.Category;
import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.service.product.DiscountUserService;

import cn.hutool.core.lang.UUID;

@Service
public class DiscountUserServiceImple implements DiscountUserService {

	@Autowired
	private DiscountUserMapper discountUserMapper;

	@Autowired
	private DiscountMapper discountMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultBean receive(DiscountUser discountUser) {
		// 判断该用户是否已经领取过了
		// 取出该用户领取该优惠券的数量
		Integer sum = discountUserMapper.selectDiscountUserById(discountUser);
		Discount discount = discountMapper.selectById(discountUser.getDiscountid());
		// 如果超过领取数量
		if (sum >= discount.getLimitedcollar()) {
			return ResultBean.error("已超过领取数量");
		}
		Integer total = discountUserMapper.selectDiscountUserByDiscountId(discountUser.getDiscountid());
		if (discount.getQuantity() != 0 && total >= discount.getQuantity()) {
			return ResultBean.error("此优惠券已被抢完了");
		}
		discountUser.setCollectiontime(new Date());
		// 如果为领取后多少天到期，则过期时间为当前领取时间往后推xx天
		if (discount.getValidperiod() == 1) {
			discountUser.setInvalidtime((DateUtils.getRearDate(discount.getDay())));
		} else {
			discountUser.setInvalidtime(discount.getEndtime());
		}
		discountUser.setId(UUID.randomUUID().toString().replace("-", ""));
		// 领取成功后优惠券的状态修改为1 （1 可使用 2 已使用 3 已过期）
		discountUser.setStatus(1);
		discountUserMapper.insertDynamic(discountUser);
		return ResultBean.success("领取成功");
	}

	@Override
	public int insertDynamic(DiscountUser discountUser) {
		// TODO Auto-generated method stub
		return discountUserMapper.insertDynamic(discountUser);
	}

	@Override
	public int updateDynamic(DiscountUser discountUser) {
		// TODO Auto-generated method stub
		return discountUserMapper.updateDynamic(discountUser);
	}

	@Override
	public int update(DiscountUser discountUser) {
		// TODO Auto-generated method stub
		return discountUserMapper.update(discountUser);
	}

	@Override
	public DiscountUser selectById(String id) {
		// TODO Auto-generated method stub
		return discountUserMapper.selectById(id);
	}

	@Override
	public List<DiscountUser> findPageWithResult(Map<String, Object> map) {
		String page = (String) map.get("page");
		String rows = (String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		return discountUserMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public ResultBean insertDiscount(DiscountUser discountUser) {
        // TODO
        return ResultBean.success(discountUserMapper.insertDynamic(discountUser));
    }

    
    @Override
	public List<Map<String, Object>> filterCoupon(List<OrderProduct> orderProducts, String userId, double total) {
		// 查询可使用优惠劵

		List<Map<String, Object>> couponList = discountUserMapper.filterCoupon(userId);
		List<Map<String, Object>> resCouponList = new ArrayList<>();

		for (Map<String, Object> coupon : couponList) {
			Integer couponType = Integer.parseInt(coupon.get("type").toString());
			Double minconsume = Double.parseDouble(coupon.get("minconsume").toString());

			// 1全场通用
			if (couponType == 1) {
				// 判断是否满足满减添加
				if (total > minconsume) {
					resCouponList.add(coupon);
				}

				// 2指定分类
			} else if (couponType == 2) {
				String categoryid = coupon.get("categoryid").toString();
				double categoryPrice = 0;
				for (OrderProduct p : orderProducts) {
					// 查询商品详情
					Product product = productMapper.selectById(p.getProId());

					// 查询分类
					Category category = categoryMapper.selectByFCateid(categoryid);
					if (category != null) {

						// 查询子列表
						List<Category> categoryList = categoryMapper.selectByfparentid(category.getfCateid());
						for (Category sub : categoryList) {
							if (product.getClassify().equals(sub.getfCateid())) {
								categoryPrice += p.getTotalPrice().doubleValue();
								break;
							}
						}
					} else {
						if (product.getClassify().equals("categoryid")) {
							categoryPrice += p.getTotalPrice().doubleValue();
						}
					}
				}

				if (categoryPrice > total) {
					resCouponList.add(coupon);
				}

				// 3商品
			} else if (couponType == 3) {
				String productid = coupon.get("productid").toString();
				for (OrderProduct p : orderProducts) {
					if (productid.equals(p.getProId()) && p.getTotalPrice().doubleValue() > total) {
						resCouponList.add(coupon);
					}
				}
			}

		}

		return resCouponList;
	}

	

}
