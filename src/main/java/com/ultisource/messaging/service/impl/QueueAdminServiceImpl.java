package com.ultisource.messaging.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.ultisource.messaging.service.QueueAdminService;

@Component
public class QueueAdminServiceImpl implements QueueAdminService {

	@Autowired
	private ConnectionFactory connectionFactory;
	
	private Channel channel;
	
	private static final String EXCHANGE = "-exchange";
	private static final String TIMEOUT_EXCHANGE = "-timout-exchange";
	private static final String DLX_EXCHANGE = "-dlx-exchange";
	
	private static final String EXCHANGE_TYPE_DIRECT = "direct";
	
	private static final String DLX_QUEUE = "-dlx";
	private static final String TIMEOUT_QUEUE = "-timeout";
	
	private static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";
	private static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
	
	public void createQueue(String queueName) throws Exception {
		
		Connection connection = connectionFactory.createConnection();
		channel = connection.createChannel(true);
		
		final String exchangeName = queueName + EXCHANGE;
		final String dlxExchangeName = queueName + DLX_EXCHANGE;
		final String timeoutExchangeName = queueName + TIMEOUT_EXCHANGE;
		
		final String dlxQueueName = queueName + DLX_QUEUE;
		final String timeoutQueueName = queueName + TIMEOUT_QUEUE;
		
		createExchange(exchangeName);
		createExchange(timeoutExchangeName);
		createExchange(dlxExchangeName);
		
		createAndBindQueue(exchangeName,queueName,dlxExchangeName, dlxQueueName);
		createAndBindQueue(dlxExchangeName, dlxQueueName);
		createAndBindQueue(timeoutExchangeName,timeoutQueueName,exchangeName,queueName);
		
	}
	
	private Map<String,Object> getDLXProps(String dlxExchangeName,String dlxRoutingKey){
		Map<String,Object> props = new HashMap<String,Object>();
		props.put(X_DEAD_LETTER_EXCHANGE,dlxExchangeName);
		props.put(X_DEAD_LETTER_ROUTING_KEY,dlxRoutingKey);
		return props;
	}
	
	private void createExchange(String exchangeName) throws IOException {
		if(channel!=null){
			channel.exchangeDeclare(exchangeName, EXCHANGE_TYPE_DIRECT,true);
		}
	}
	
	private void createAndBindQueue(String exchangeName,String queueName) throws IOException {
		channel.queueDeclare(queueName, true, false, false, new HashMap<String,Object>());
		channel.queueBind(queueName, exchangeName, queueName);
	}
	
	private void createAndBindQueue(String exchangeName,String queueName,String dlxExchangeName,String dlxRoutingKey) throws IOException {
		Map<String,Object> properties = getDLXProps(dlxExchangeName, dlxRoutingKey);
		channel.queueDeclare(queueName, true, false, false, properties);
		channel.queueBind(queueName, exchangeName, queueName);
	}

}
