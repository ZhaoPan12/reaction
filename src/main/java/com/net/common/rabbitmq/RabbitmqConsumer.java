package com.net.common.rabbitmq;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.OrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.net.system.mapper.order.OrderMapper;
import com.net.system.mapper.order.OrderProductMapper;
import com.net.system.mapper.product.ProductMapper;
import com.net.system.mapper.product.ProductSkuMapper;
import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

@Component
public class RabbitmqConsumer {

	private Logger logger = LoggerFactory.getLogger(RabbitmqConsumer.class);

	@Resource
	private OrderMapper orderMapper;

	@Resource
	private OrderProductMapper orderProductMapper;

	@Resource
	private ProductMapper productMapper;

	@Resource
	private ProductSkuMapper productSkuMapper;

	/**
	 * 订单支付延时
	 * 
	 * @param content
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	//@RabbitListener(queues = RabbitConfig.ORDER_PAYMENT)
	public void orderPayment(String content, Message message, Channel channel) throws IOException {
		System.out.println("案件时限开始");
		Order order = orderMapper.selectById(content);
		if (order != null && order.getStatus() == 1 && order.getPaymentStatus() == 1) {
			// 取消订单
			Order updateOrder = new Order();
			updateOrder.setId(order.getId());
			updateOrder.setStatus(3);
			updateOrder.setCloseTime(new Date());
			updateOrder.setUpdateTime(new Date());
			orderMapper.updateDynamic(updateOrder);

			// 归还库存
			List<OrderProduct> orderProducts = orderProductMapper.findOrderProduct(order.getId());
			for (OrderProduct orderProduct : orderProducts) {
				productSkuMapper.returnStock(orderProduct);
				productMapper.returnStock(orderProduct);
			}
		}

		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

}
