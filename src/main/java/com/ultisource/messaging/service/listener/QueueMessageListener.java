package com.ultisource.messaging.service.listener;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;
import com.ultisource.messaging.service.QueueMessageService;

public class QueueMessageListener implements ChannelAwareMessageListener {
	
	private static final Logger logger = Logger.getLogger(QueueMessageListener.class);
	
	private QueueMessageService messageService;
	
	public QueueMessageListener(QueueMessageService messageService){
		this.messageService = messageService;
	}
	
	private static final String COUNTER = "counter";

	public void onMessage(Message msg, Channel channel) throws Exception {
		String rountingKey = msg.getMessageProperties().getReceivedRoutingKey();
		logger.info(" --- Received --- " + msg);
		logger.info(" --- Exchange --- " + msg.getMessageProperties().getReceivedExchange());
		logger.info(" --- Routing Key --- " + rountingKey);
		
		Integer counter = decrementCounter(msg);
		if(counter!=null){
			processMsg(counter, channel, msg);
		}
		
		// TODO Processing of the Message
		
		// in case of exception reject
		
	}
	
	private Integer decrementCounter(Message msg){
		Map<String,Object> headerMap = msg.getMessageProperties().getHeaders();
		Object objCounter = headerMap.get(COUNTER);
		Integer counter = null;
		if(objCounter!=null && objCounter instanceof Integer){
			counter = (Integer)objCounter;
			logger.info( " --- No. of Counts ---" + counter--);
			headerMap.put(COUNTER, counter);
		}
		return counter;
			
	}
	
		private void processMsg(int counter, Channel channel, Message msg) throws IOException {
			if(counter<0) {
				// Publishing to DLQ
			channel.basicReject(msg.getMessageProperties().getDeliveryTag(), false);
			}
			
			else {
				
				// TODO set the message expiration based on the counter value and exponential
				msg.getMessageProperties().setExpiration("10000");
				messageService.sendMessage("main-timout-exchange", "main-timeout",msg);
				// Publish to TIME-OUT QUEUE
				channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
			}
		}

	
	
	
}
