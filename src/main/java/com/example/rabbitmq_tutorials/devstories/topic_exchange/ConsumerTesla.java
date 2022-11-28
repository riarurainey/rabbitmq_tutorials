package com.example.rabbitmq_tutorials.devstories.topic_exchange;

public class ConsumerTesla extends Consumer {
    public ConsumerTesla(String routingKey) {
        super(routingKey);
    }

    public static void main(String[] args) {
        Consumer consumerTesla = new ConsumerTesla("Tesla.#");
        consumerTesla.startConsumer();

    }
}
