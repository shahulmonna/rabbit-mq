package com.rabbit.mq.amqp;
import com.google.common.collect.Lists;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.util.Date;

/**
 * @author: Syed Shahul
 */
public class EmitLogDirect {
	private static final String EXCHANGE_NAME = "direct_logs";

	private static String getMessage(String[] strings){
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String getSeverity(String[] strings){
			return "info";
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

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

	//	String severity = getSeverity(argv);
		String message = " Hello World! queue : "+ new Date().toString();
		for(String severity: Lists.newArrayList("info", "warning", "error")){
			message.concat(" "+severity);
		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());

		System.out.println(" [x] Sent '" + severity + "':'" + message );
		}

		channel.close();
		connection.close();
	}
}
