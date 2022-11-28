package com.example.rabbitmq_tutorials.devstories.default_exchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Publisher {
    private static final String QUEUE_NAME = "dev_queue";

    public static void main(String[] args) {
        int counter = 0;

        while (true) {
            ConnectionFactory factory = new ConnectionFactory();

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                counter++;
                String message = String.format("Message №%d From Publisher", counter);
                byte[] body = message.getBytes();
                channel.basicPublish("", QUEUE_NAME, null, body);

                System.out.printf("Message №%d was send to Default Exchange\n", counter);
                Thread.sleep(3000);

            } catch (IOException | TimeoutException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
