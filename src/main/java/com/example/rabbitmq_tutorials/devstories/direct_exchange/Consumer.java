package com.example.rabbitmq_tutorials.devstories.direct_exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(4);
        service.submit(startConsumer("info"));
        service.submit(startConsumer("warning"));
        service.submit(startConsumer("error"));
    }

    static Runnable startConsumer(String rootingKey) {
        return () -> {
            try {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");

                Connection connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                String queueName = channel.queueDeclare().getQueue();
                channel.queueBind(queueName, EXCHANGE_NAME, rootingKey);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.printf("Received message: %s\n", message);

                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
                System.out.printf("Subscribed to queue: %s\n", queueName);

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        };
    }
}
