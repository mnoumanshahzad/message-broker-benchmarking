package example;

public class Config {

    static String CONNECTION_STRING = "tcp://localhost:61616";
    static String QUEUE_NAME = "EC:TEST:QUEUE";

    static int NUM_PRODUCERS = 100;
    static int MESSAGES_PER_PRODUCER = 10;

    static int NUM_CONSUMERS = 10;
    static int CONSUMER_MAX_TIMEOUT = 5000;
}
