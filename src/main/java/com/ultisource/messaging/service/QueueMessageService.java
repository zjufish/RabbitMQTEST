package com.ultisource.messaging.service;

import org.springframework.amqp.core.Message;

public interface QueueMessageService {
	
	public void sendMessage(String exchangeName, String routingKey,Message msg);

}
