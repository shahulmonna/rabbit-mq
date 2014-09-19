package com.rabbit.mq.amqp;

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
/**
 * @author: Syed Shahul
 */
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv)
		throws java.io.IOException,
		       java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(System.getenv("HOST"));
		factory.setVirtualHost(System.getenv("VHOST"));
		factory.setUsername(System.getenv("UNAME"));
		factory.setPassword(System.getenv("PASSCODE"));

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();
		//A binding is a relationship between an exchange and a queue.
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(queueName, true, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Received '" + message + "'");
		}
	}
}
