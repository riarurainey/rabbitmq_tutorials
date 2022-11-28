package com.example.rabbitmq_tutorials.devstories.direct_exchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.submit(startProducer(12000, "error"));
        service.submit(startProducer(10000, "info"));
        service.submit(startProducer(8000, "warning"));
    }

    static Runnable startProducer(int timeToSleep, String routingKey) {

        return () -> {
            int counter = 0;
            while (true) {
                ConnectionFactory factory = new ConnectionFactory();

                try (Connection connection = factory.newConnection();
                     Channel channel = connection.createChannel()) {

                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                    counter++;
                    String message = String.format("Message type [%s] №%d From Publisher", routingKey, counter);
                    byte[] body = message.getBytes();
                    channel.basicPublish(EXCHANGE_NAME, routingKey, null, body);

                    System.out.printf("Message type [%s] №%d was send to Direct Exchange\n", routingKey, counter);
                    Thread.sleep(timeToSleep);

                } catch (IOException | TimeoutException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

