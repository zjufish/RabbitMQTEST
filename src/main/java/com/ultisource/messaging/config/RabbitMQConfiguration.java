package com.ultisource.messaging.config;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ultisource.messaging.service.QueueAdminService;
import com.ultisource.messaging.service.QueueMessageService;
import com.ultisource.messaging.service.impl.QueueAdminServiceImpl;
import com.ultisource.messaging.service.impl.QueueMessageServiceImpl;
import com.ultisource.messaging.service.listener.QueueMessageListener;

@Configuration
public class RabbitMQConfiguration {

	private static final String QUEUE_NAMES="main,main-timeout,main-dlx";
	private static final Logger logger = Logger.getLogger(RabbitMQConfiguration.class);
	
	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
		//factory.setUsername("guest");
		//factory.setPassword("guest");
		//factory.setConnectionCacheSize(10);
//		factory.setPublisherConfirms(true);
//		factory.setPublisherReturns(true);
//		factory.setChannelCacheSize(1);
		logger.info(" --- Connection Factory Creationg Successful --- ");
		return factory;
	}
	
	@Bean
	public AmqpAdmin amqpAdmin() {
		RabbitAdmin admin = new RabbitAdmin(connectionFactory());
		return admin;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(){
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setChannelTransacted(true);
		return template;
	}
	
//	@Bean
//	public Queue queue(){
//		Queue queue = new Queue("myqueue");
//		Map<String,Object> props = queue.getArguments();
//		if(props==null){
//			props = new HashMap<String,Object>();
//		}
//		props.put("x-dead-letter-exchange", "dlx-exchange");
//		return queue;
//	}
	
	@Bean
	public QueueMessageService queueMessageService(){
		return new QueueMessageServiceImpl();
	}
	
	/**
	 * Settting up listener on myqueue 'Message Driven POJO' Settings
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames("main");
		container.setMessageListener(new QueueMessageListener(queueMessageService()));
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		logger.info("--- Listener Registered for the main Queue --- ");
		return container;
	}
	
	@Bean
	QueueAdminService queueAdminService(){
		return new QueueAdminServiceImpl();
	}
	
	
	
}
