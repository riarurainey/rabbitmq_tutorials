package com.example.rabbitmq_tutorials.devstories.topic_exchange;

public class ConsumerEcological extends Consumer {
    public ConsumerEcological(String routingKey) {
        super(routingKey);
    }

    public static void main(String[] args) {
        Consumer consumerEcological = new ConsumerEcological("Tesla.#");
        consumerEcological.startConsumer();

    }
}
