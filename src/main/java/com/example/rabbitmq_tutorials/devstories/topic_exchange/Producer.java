package com.example.rabbitmq_tutorials.devstories.topic_exchange;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final List<String> cars = List.of("BMV", "Audi", "Tesla", "Mercedes");
    private static final List<String> colors = List.of("red", "white", "black");
    private static final String EXCHANGE_NAME = "topic_logs";

    
    public static void main(String[] args) {
        int counter = 0;

        while (true) {
            ConnectionFactory factory = new ConnectionFactory();
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

                counter++;
                String routingKey = counter % 4 == 0
                        ? "Tesla.red.fast.ecological" : counter % 5 == 0
                        ? "Mercedes.exclusive.expensive.ecological" :
                        generateRoutingKey();

                String message = String.format("Message type [%s] №%d From Publisher", routingKey, counter);
                byte[] body = message.getBytes();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, body);

                System.out.printf("Message type [%s] №%d was send to Topic Exchange\n", routingKey, counter);
                Thread.sleep(3000);

            } catch (IOException | TimeoutException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String generateRoutingKey() {
        return cars.get(getRandomInt(0, 3)) + "." + colors.get(getRandomInt(0, 2));

    }

    private static int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);

    }
}
