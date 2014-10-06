package com.ultisource.messaging.service.impl;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ultisource.messaging.service.QueueMessageService;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {

	private static final Logger logger = Logger.getLogger(QueueMessageServiceImpl.class);
	
	@Autowired
	private AmqpTemplate template;
	
	/**
	 * Sending message to default exchange
	 */
	public void sendMessage(String exchangeName, String routingKey,Message msg){
		MessageProperties props = new MessageProperties();
		props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		template.send(exchangeName,routingKey,msg);
		logger.info(" --- Message Published to Exchange with Routing Key " + routingKey  + " Exchange Name " + exchangeName);
	}

	/**
	 * Receiving message from default exchange
	 */
	public String receiveMessage(){
		return (String)template.receiveAndConvert("myqueue");
	}
	
	
}
