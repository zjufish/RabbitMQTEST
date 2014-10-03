package com.test;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendReceiveService {

	@Autowired
	private AmqpTemplate template;
	
	/**
	 * Sending message to default exchange
	 */
	public void sendMessage(){
		template.convertAndSend("myqueue", "hello message");
	}

	/**
	 * Receiving message from default exchange
	 */
	public String receiveMessage(){
		return (String)template.receiveAndConvert("myqueue");
	}
	
	
}
