package com.net.common.rabbitmq;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.net.common.util.DateUtils;



@Component
public class RabbitmqPublish {

	@Autowired
	RabbitTemplate rabbitTemplate;

	/**
	 * 发送延时信息
	 * 
	 * @param map1  内容
	 * @param topic topic
	 * @param delay 延时时间，秒
	 */
	public void sendTimeoutMsg(String p1,String p2,String payloadjson, int delay) {

		try {
			
			  rabbitTemplate.convertAndSend(p1,p2, payloadjson, message -> {
		            message.getMessageProperties().setDelay(delay * 1000);
		            System.out.println("发送时间:" + DateUtils.getDate());
		            return message;
		        });
		} catch (Exception e) {
			System.out.println("失败");
			e.printStackTrace();
		}

	}

}
