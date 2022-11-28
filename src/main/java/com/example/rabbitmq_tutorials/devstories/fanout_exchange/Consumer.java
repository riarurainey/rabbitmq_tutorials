package com.example.rabbitmq_tutorials.devstories.fanout_exchange;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String EXCHANGE_NAME = "notifier";
    private double totalHold = 0;
    private double payment;

    protected void startConsumer(boolean isTax) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                payment = getPayment(message);
                totalHold = addTax(isTax);
                System.out.printf("Payment received for the amount of {%s}\n", payment);
                System.out.printf("{%,.2f} held from this person\n", totalHold);

            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
            System.out.printf("Subscribed to the queue {%s}\n", queueName);
            System.out.println("Listening . . .");

        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private double addTax(boolean isTax) {
        if (isTax) {
            return totalHold += payment * 0.01;
        } else {
            return totalHold += payment;

        }
    }

    private double getPayment(String message) {
        String[] array = message.split(" ");
        return Double.parseDouble(array[array.length - 1]);
    }
}
