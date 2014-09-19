package com.rabbit.mq.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author: Syed Shahul
 */
public class Worker {
	private final static String QUEUE_NAME = "my3ndqueue";
	private static void doWork(String task) throws InterruptedException {
		for (char ch: task.toCharArray()) {
			if (ch == '.') Thread.sleep(1000);
		}
	}

	public static void main(String[] argv)
		throws java.io.IOException,
		       InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(System.getenv("HOST"));
		factory.setVirtualHost(System.getenv("VHOST"));
		factory.setUsername(System.getenv("UNAME"));
		factory.setPassword(System.getenv("PASSCODE"));
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		boolean autoAck = false;

		channel.basicConsume(QUEUE_NAME, autoAck, consumer);

	/*	while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			System.out.println(" [x] Received '" + message + "'");
		}*/
		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println(" [x] Received '" + message + "'");
			doWork(message);
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			System.out.println(" [x] Done");
		}
	}
}
