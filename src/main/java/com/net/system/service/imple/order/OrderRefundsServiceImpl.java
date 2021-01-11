package com.net.system.service.imple.order;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.common.config.WxRefundUtil;
import com.net.common.config.WxSPConfig;
import com.net.common.util.ResultBean;
import com.net.common.util.Signature;
import com.net.common.util.XmlUtil;
import com.net.system.mapper.order.OrderMapper;
import com.net.system.mapper.order.OrderRefundsMapper;
import com.net.system.mapper.product.FileMapper;
import com.net.system.mapper.sysmange.BusinessAddressMapper;
import com.net.system.model.BusinessAddress;
import com.net.system.model.Files;
import com.net.system.model.Order;
import com.net.system.model.OrderRefunds;
import com.net.system.model.vo.OrderRefundsVo;
import com.net.system.service.order.OrderRefundsService;

import cn.hutool.core.lang.UUID;

@Service
public class OrderRefundsServiceImpl implements OrderRefundsService {

	@Resource
	private OrderRefundsMapper orderRefundsMapper;

	@Resource
	private FileMapper fileMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private BusinessAddressMapper businessAddressMapper;

	@Value("${photo.resourceLocations}")
	private String IMG_PATH_PREFIX;

	@Value("${photo.photoPath}")
	private String photoPath;

	@Override
	public int applyRefunds(OrderRefundsVo orderRefundsVo) {
		if (orderRefundsVo.getId() != null && !orderRefundsVo.getId().equals("")) {
			OrderRefundsVo orderRefunds = orderRefundsMapper.selectById(orderRefundsVo.getId());
			// 删除申请的售后
			orderRefundsMapper.delete(orderRefunds.getId());

			// 删除上传的图片
			for (Files f : orderRefunds.getFiles()) {
				fileMapper.delete(f.getId());
			}
			deleteFile(orderRefunds.getFiles());
		}

		orderRefundsVo.setId(UUID.randomUUID().toString().replace("-", ""));
		orderRefundsVo.setAddTime(new Date());
		orderRefundsVo.setStatus(1);

		// 修改订单状态
		Order order = new Order();
		order.setId(orderRefundsVo.getOrderId());
		order.setRefundStatus(1);
		orderMapper.updateDynamic(order);

		// 添加图片
		for (Files f : orderRefundsVo.getFiles()) {
			f.setId(UUID.randomUUID().toString().replace("-", ""));
			f.setProductid(orderRefundsVo.getId());
			f.setImagename(f.getImageurl().substring(f.getImageurl().lastIndexOf("/") + 1, f.getImageurl().length()));
			fileMapper.insertDynamic(f);
		}

		return orderRefundsMapper.insertDynamic(orderRefundsVo);
	}

	@Override
	public int cancelRefunds(String id) {
		OrderRefundsVo orderRefundsVo = orderRefundsMapper.selectById(id);
		// 删除申请的售后
		orderRefundsMapper.delete(orderRefundsVo.getId());

		// 删除上传的图片
		for (Files f : orderRefundsVo.getFiles()) {
			fileMapper.delete(f.getId());
		}
		deleteFile(orderRefundsVo.getFiles());

		// 修改订单状态
		Order order = new Order();
		order.setId(orderRefundsVo.getOrderId());
		order.setRefundStatus(0);
		orderMapper.updateDynamic(order);

		return 1;
	}

	public void deleteFile(List<Files> files) {
		for (Files fileInfo : files) {
			File file = new File(IMG_PATH_PREFIX + fileInfo.getImagename());
			file.delete();
		}
	}

	@Override
	public int updateStatus(OrderRefunds orderRefunds) {

		if (orderRefunds.getStatus() != null && orderRefunds.getStatus() == 5) {
			// 发起退款操作
			if(refund(orderRefunds.getOrderId())) {
				orderRefunds.setRefundTime(new Date());
			}
		}

		return orderRefundsMapper.updateDynamic(orderRefunds);
	}

	@Override
	public List<Map<String, Object>> findPageWithResult(Map<String, Object> map) {
		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());
		PageHelper.startPage(page, limit);
		return orderRefundsMapper.findPageWithResult(map);

	}

	@Override
	public BusinessAddress findBusinessAddress() {
		// TODO Auto-generated method stub
		return businessAddressMapper.selectInfo();
	}


	private boolean refund(String orderId) {
		try {

			Order order = orderMapper.selectById(orderId);
			if (order != null) {

				// 商户订单号 支付时的订单号
				String payId = order.getSerialNumber();

				// 商户退款单号
				String refundId = getOrderIdByTime();

				// 订单金额也是退款金额
				Integer money = Integer.valueOf(String.valueOf(Math.round(order.getActual().doubleValue() * 100)));

				SortedMap<Object, Object> params = new TreeMap<>();
				params.put("appid", WxSPConfig.APP_ID_PUBLIC);
				params.put("mch_id", WxSPConfig.MCH_ID);
				params.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
//				    //商户订单号和微信订单号二选一
				params.put("out_trade_no", payId);
				params.put("out_refund_no", refundId);
				params.put("total_fee", String.valueOf(money));
				params.put("refund_fee", String.valueOf(money));
				params.put("refund_desc", "订单退款");

				// 生成签名
				String sign = Signature.createSign("UTF-8", params, WxSPConfig.MCH_KEY);
				params.put("sign", sign);
				String requestXML = XmlUtil.getRequestXml(params);
				String responseXml = WxRefundUtil.doRefund(WxSPConfig.MCH_ID,
						"https://api.mch.weixin.qq.com/secapi/pay/refund", requestXML);
				System.out.println(responseXml);
				Map<String, String> returnMsg = XmlUtil.xmlToMap(responseXml);
				System.out.println(returnMsg.toString());

				// 退款成功
				if (returnMsg.get("result_code").toString().equals("SUCCESS")) {

					// 修改订单状态为退款成功
					Order updateOrder = new Order();
					updateOrder.setId(order.getId());
					updateOrder.setStatus(7);
					updateOrder.setEndTime(new Date());
					orderMapper.updateDynamic(updateOrder);
					return true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getOrderIdByTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate = sdf.format(new Date());
		String result = "";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			result += random.nextInt(10);
		}
		return newDate + result;
	}
}
