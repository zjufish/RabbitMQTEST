package com.test;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SimpleMessageListener implements MessageListener {

	public void onMessage(Message msg) {
		
		System.out.println("Recevied " + msg);
		
	}

	
	
}
