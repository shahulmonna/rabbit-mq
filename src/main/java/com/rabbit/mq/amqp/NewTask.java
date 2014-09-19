package com.rabbit.mq.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * @author: Syed Shahul
 */
public class NewTask {
	private final static String QUEUE_NAME = "myfirstqueue";
	private static String getMessage(String[] strings){
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0) return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

	public static void main(String[] argv)
		throws java.io.IOException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(System.getenv("HOST"));
		factory.setVirtualHost(System.getenv("VHOST"));
		factory.setUsername(System.getenv("UNAME"));
		factory.setPassword(System.getenv("PASSCODE"));

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//String message = "Hello World! queue"+ new Date().toString();
		//channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		String message = getMessage(argv);

		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}
}
