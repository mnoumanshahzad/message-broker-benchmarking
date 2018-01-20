package example;

public class Config {

    /**
     * General ActiveMQ Config
     */
    static String CONNECTION_STRING = "tcp://localhost:61616";
    static String CONNECTION_FACTORY = "ConnectionFactory";
    static String QUEUE_NAME = "EC:TEST:QUEUE";
    static String TOPIC_NAME = "EC:TEST:TOPIC";
    static String USERNAME = "admin";
    static String PASSWORD = "admin";

    /**
     * For using Point-to-Point channel, set to 0
     * For using Publish-Subscribe channel, set to 1
     */
    static int CHANNEL_TYPE = 1;

    static int NUM_PRODUCERS = 3;
    static int MESSAGES_PER_PRODUCER = 10;

    static int NUM_CONSUMERS = 10;
    static int CONSUMER_MAX_TIMEOUT = 10000;
}
