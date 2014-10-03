package com.test;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
		//factory.setUsername("guest");
		//factory.setPassword("guest");
		//factory.setConnectionCacheSize(10);
		factory.setPublisherConfirms(true);
		factory.setPublisherReturns(true);
		factory.setChannelCacheSize(1);
		return factory;
	}
	
	@Bean
	public AmqpAdmin amqpAdmin() {
		RabbitAdmin admin = new RabbitAdmin(connectionFactory());
		return admin;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(){
		return new RabbitTemplate(connectionFactory());
	}
	
	@Bean
	public Queue queue(){
		Queue queue = new Queue("myqueue");
		System.out.println(queue.isAutoDelete());
		System.out.println(queue.isDurable());
		
		return queue;
	}
	
	@Bean
	public SendReceiveService sendReceiveService(){
		return new SendReceiveService();
	}
	
	/**
	 * Settting up listener on myqueue 'Message Driven POJO' Settings
	 * @return
	 */
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(){
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames("myqueue");
		container.setMessageListener(new SimpleMessageListener());
		return container;
	}
	
}
