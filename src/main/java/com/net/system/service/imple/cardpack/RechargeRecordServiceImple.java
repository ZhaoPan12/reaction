package com.net.system.service.imple.cardpack;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.common.config.WxSPConfig;
import com.net.common.util.BeanUtil;
import com.net.common.util.MapToUtil;
import com.net.common.util.ResultBean;
import com.net.common.util.Signature;
import com.net.common.util.WechatUtil;
import com.net.common.util.WxPayHttp;
import com.net.common.util.XmlUtil;
import com.net.system.mapper.cardpack.RechargeRecordMapper;
import com.net.system.mapper.sysmange.UserMapper;
import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;
import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.mall.RechargeRecord;
import com.net.system.model.vo.WxParam;
import com.net.system.service.cardpack.RechargeRecordService;

import cn.hutool.core.lang.UUID;
@Service
public class RechargeRecordServiceImple implements RechargeRecordService {
	
	@Resource
	private RechargeRecordMapper rechargeRecordMapper;
	@Resource
	private UserMapper userMapper;
	
	private static final String NOTIFY_URL="http://card.tzcitycard.com/reaction/canteen/wx/notifyResultCoupon";

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(RechargeRecord rechargeRecord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(RechargeRecord rechargeRecord) {
		// TODO Auto-generated method stub
		return rechargeRecordMapper.insertDynamic(rechargeRecord);
	}

	@Override
	public int updateDynamic(RechargeRecord rechargeRecord) {
		// TODO Auto-generated method stub
		return rechargeRecordMapper.updateDynamic(rechargeRecord);
	}

	@Override
	public int update(RechargeRecord rechargeRecord) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RechargeRecord selectByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return rechargeRecordMapper.selectByOrderNo(orderNo);
	}

	@Override
	public List<RechargeRecord> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return rechargeRecordMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultBean RechargePay(Map<String, Object> map, HttpServletRequest request) {
		try {
			//获取订单id
			String orderId=(String) map.get("orderId");
			String accKind=(String) map.get("accKind");
			RechargeRecord record=rechargeRecordMapper.selectByOrderNo(orderId);
			String userId=(String) map.get("userId");
			String money=(String) map.get("money");
			if(record==null) {
				record=new RechargeRecord();
				record.setCreatetime(new Date());
				record.setUserid(Integer.parseInt(userId));
				//获取订单号
				record.setOrderno(getOrderIdByTime());
				record.setId(UUID.randomUUID().toString().replace("-",""));
				record.setStatus(1);
				//充值金额
				BigDecimal total=new BigDecimal(money);
				record.setRechargeName(total);
				record.setPaytype(1);
				record.setType(2);
				record.setAcckind(accKind);
				rechargeRecordMapper.insertDynamic(record);
				// 将支付进行封装
				WxParam wxParam = new WxParam();
				wxParam.setWxParam(wxParam, record.getOrderno(), record.getRechargeName().doubleValue(), request, NOTIFY_URL, userMapper.getUserOpenId(userId));
				Map<String, String> wxParamTOMap = BeanUtil.convertBeanToMap(wxParam);
				String sign = Signature.getSign(wxParamTOMap, WxSPConfig.MCH_KEY);
				wxParamTOMap.put("sign", sign);
				String xmlResponse = WxPayHttp.doPostPayUnifiedOrder(WxSPConfig.UNIFIED_ORDER_URL,
						XmlUtil.createRequestXml(wxParamTOMap));
				Map<String, String> wxParamVO = WechatUtil.parseXmlResponse(xmlResponse);
				wxParamVO.put("serialNumber", record.getOrderno());
				return ResultBean.success(wxParamVO);
			}else {
				//微信支付
				WxParam wxParam = new WxParam();
				wxParam.setWxParam(wxParam, record.getOrderno(), record.getRechargeName().doubleValue(), request, NOTIFY_URL, userMapper.getUserOpenId(userId));
				Map<String, String> wxParamTOMap = BeanUtil.convertBeanToMap(wxParam);
				String sign = Signature.getSign(wxParamTOMap, WxSPConfig.MCH_KEY);
				wxParamTOMap.put("sign", sign);
				String xmlResponse = WxPayHttp.doPostPayUnifiedOrder(WxSPConfig.UNIFIED_ORDER_URL,
						XmlUtil.createRequestXml(wxParamTOMap));
				Map<String, String> wxParamVO = WechatUtil.parseXmlResponse(xmlResponse);
				wxParamVO.put("serialNumber", record.getOrderno());
				return ResultBean.success(wxParamVO);
			}
		}catch(Exception e) {
			return ResultBean.error("错误");
		}
	}

    public static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }

	@Override
	public ResultBean updateOrderStatus(RechargeRecord rechargeRecord) {
		return ResultBean.success(rechargeRecordMapper.updateOrderStatus(rechargeRecord));
	}
}
