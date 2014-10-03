package com.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
		SendReceiveService sendReceiveService = context.getBean(SendReceiveService.class);
		sendReceiveService.sendMessage();
//		String msg = sendReceiveService.receiveMessage();
		
//		System.out.println(" --- Received --- " + msg);
		
	}

}
