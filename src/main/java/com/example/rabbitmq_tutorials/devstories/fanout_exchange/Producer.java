package com.example.rabbitmq_tutorials.devstories.fanout_exchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String EXCHANGE_NAME = "notifier";

    public static void main(String[] args) {
        Random random = new Random();

        while (true) {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

                int money = random.nextInt(10000);
                String message = String.format("Payment received for the amount of %d", money);

                byte[] body = message.getBytes();
                channel.basicPublish(EXCHANGE_NAME, "", null, body);

                System.out.printf("Payment received for amount of {%d}.\nNotifying by '%s' Exchange", money, EXCHANGE_NAME);
                Thread.sleep(3000);

            } catch (IOException | TimeoutException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
