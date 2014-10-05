package com.ultisource.messaging.service.impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ultisource.messaging.service.QueueMessageService;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {

	
	@Autowired
	private AmqpTemplate template;
	
	/**
	 * Sending message to default exchange
	 */
	public void sendMessage(String exchangeName, String routingKey,Message msg){
		MessageProperties props = new MessageProperties();
		props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
		template.send(exchangeName,routingKey,msg);
	}

	/**
	 * Receiving message from default exchange
	 */
	public String receiveMessage(){
		return (String)template.receiveAndConvert("myqueue");
	}
	
	
}
