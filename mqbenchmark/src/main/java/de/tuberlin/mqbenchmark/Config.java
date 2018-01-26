package de.tuberlin.mqbenchmark;

import java.util.ArrayList;
import java.util.List;

public class Config {
    /**
     * General ActiveMQ Config
     */
    public static final String CONNECTION_STRING = "ssl://b-5292ccd6-7076-448d-a943-aadf9dff4be9-1.mq.eu-west-1.amazonaws.com:61617";
    public static final String USERNAME = "nouman";
    public static final String PASSWORD = "noumanshahzad";

    public static final String DESTINATION = "Test-Topic";

    public static final int NUM_DESTINATIONS = 4;

    //public static final List<String> DESTINATION_LIST;


    /**
     * For using Point-to-Point channel, set to 0
     * For using Publish-Subscribe channel, set to 1
     */
    public static final int CHANNEL_TYPE = 1;

    public static final int NUM_PRODUCERS = 2;
    public static final int NUM_PRODUCERS_PER_DESTINATION = 2;
    public static final int MESSAGES_PER_PRODUCER = 100000;

    public static final int NUM_CONSUMERS = 2;
    public static final int NUM_CONSUMERS_PER_DESTINATION = 2;
    public static final int CONSUMER_MAX_TIMEOUT = 10000;






}
