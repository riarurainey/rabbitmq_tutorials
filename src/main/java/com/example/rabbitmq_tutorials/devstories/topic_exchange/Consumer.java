package com.example.rabbitmq_tutorials.devstories.topic_exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final String rootingKey;
    private static final String EXCHANGE_NAME = "topic_logs";
    public Consumer(String rootingKey) {
        this.rootingKey = rootingKey;
    }

    protected void startConsumer() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, rootingKey);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.printf("Received message: %s\n", message);

            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
            System.out.printf("Subscribed to queue: %s\n", queueName);
            System.out.printf("Listening to %s\n", rootingKey);

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}



