package com.ultisource.messaging.service.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class ProgMessageListener implements ChannelAwareMessageListener {

	public void onMessage(Message message, Channel channel) throws Exception {
		
		System.out.println(" -- received programmatic listener in message --- " + message.getBody());
		
		
	}

	
	
}
