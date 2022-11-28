package com.example.rabbitmq_tutorials.devstories.fanout_exchange;

public class ConsumerBankCharger extends Consumer {

    public static void main(String[] args) {
        Consumer consumerBankCharger = new ConsumerBankCharger();
        consumerBankCharger.startConsumer(false);

    }
}
