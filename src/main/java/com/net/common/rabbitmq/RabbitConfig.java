package com.net.common.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description //TODO
 * @Date $ $
 * @Author huangwb
 **/

//@Configuration
public class RabbitConfig {

	// 订单延时
	public static final String ORDER_PAYMENT= "order_payment";

	 /**
     * 延时的exchange
     */
    public static final String DELAYED_EXCHANGE_XDELAY_ORDER_PAYMENT = "exchange.xdelay.delayed-order_payment";
    public static final String DELAY_ROUTING_KEY_XDELAY_ORDER_PAYMENT = "routingkey.xdelay.delay-order_payment";


	//@Bean
	public Queue prize_stock_recoveryQueue() {
		return new Queue(ORDER_PAYMENT, true);
	}

	

	// 创建交换机
	//@Bean
	CustomExchange  exchange1() {
		Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_XDELAY_ORDER_PAYMENT, "x-delayed-message", true, false, args);
		
	}
	
	

	//@Bean
	Binding bindingExchangeMessage1() {
		return BindingBuilder.bind(prize_stock_recoveryQueue()).to(exchange1()).with(DELAY_ROUTING_KEY_XDELAY_ORDER_PAYMENT).noargs();
	}


}