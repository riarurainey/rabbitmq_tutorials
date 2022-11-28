package com.example.rabbitmq_tutorials.devstories.topic_exchange;

public class ConsumerRed extends Consumer {
    public ConsumerRed(String routingKey) {
        super(routingKey);
    }

    public static void main(String[] args) {
        Consumer consumerRed = new ConsumerRed("*.red.#");
        consumerRed.startConsumer();

    }
}
