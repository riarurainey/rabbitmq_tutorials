package com.example.rabbitmq_tutorials.devstories.fanout_exchange;

public class ConsumerTax extends Consumer {

    public static void main(String[] args) {
        Consumer consumerTax = new ConsumerTax();
        consumerTax.startConsumer(true);

    }
}


