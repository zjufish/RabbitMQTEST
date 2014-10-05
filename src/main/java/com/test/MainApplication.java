package com.test;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ultisource.messaging.config.RabbitMQConfiguration;
import com.ultisource.messaging.service.QueueAdminService;
import com.ultisource.messaging.service.QueueMessageService;

public class MainApplication {

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
		QueueAdminService service = context.getBean(QueueAdminService.class);

		QueueMessageService sendReceiveService = context.getBean(QueueMessageService.class);
		MessageProperties props = new MessageProperties();
		props.setExpiration("10000");
		props.getHeaders().put("counter",new Integer("3"));
		Message msg = new Message("test message".getBytes(),props);
		sendReceiveService.sendMessage("main-exchange","main",msg);

	}

}
