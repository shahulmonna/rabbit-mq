package com.rabbit.mq.amqp;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Date;

/**
 * @author: Syed Shahul
 */
public class Send {
	private final static String QUEUE_NAME = "my3ndqueue";
	public static void main(String[] argv)
		throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(System.getenv("HOST"));
		factory.setVirtualHost(System.getenv("VHOST"));
		factory.setUsername(System.getenv("UNAME"));
		factory.setPassword(System.getenv("PASSCODE"));

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		int i;
		for(i=0;i<1000;i++){
		String message = i+ " Hello World! queue : "+ new Date().toString();
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");
		}
		channel.close();
		connection.close();
	}
}
