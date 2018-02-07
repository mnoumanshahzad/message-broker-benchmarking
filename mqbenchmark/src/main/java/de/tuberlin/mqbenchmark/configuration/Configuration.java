package de.tuberlin.mqbenchmark.configuration;

import de.tuberlin.mqbenchmark.exceptions.PropertyNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration {

    private Properties properties;

    /**
     * General ActiveMQ Configuration
     */
    private String CONNECTION_URL;
    private String USERNAME;
    private String PASSWORD;

    /**
     * ActiveMQ Destination Configuration
     */
    private int NUM_DESTINATIONS;
    private List<String> DESTINATION_LIST;

    // 0 --> Point-to-Point and 1 --> Publish-Subscribe
    private int CHANNEL_TYPE;

    /**
     * ActiveMQ Producer Configuration
     */
    private int NUM_PRODUCERS_PER_DESTINATION;
    private int MESSAGES_PER_PRODUCER;

    /**
     * ActiveMQ Consumer Configuration
     */
    private int NUM_CONSUMERS_PER_DESTINATION;
    private int CONSUMER_MAX_TIMEOUT;

    private int MESSAGE_SIZE;

    public Configuration() {

        properties = new Properties();
        InputStream is = null;

        try {

            is = new FileInputStream("src/main/resources/benchmark.properties");
            properties.load(is);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration(String filePath) {

        properties = new Properties();
        InputStream is = null;

        try {

            is = new FileInputStream(filePath);
            properties.load(is);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getConnetionUrl() {

        if (CONNECTION_URL == null) {
            if ((CONNECTION_URL = properties.getProperty("connection.url")) == null) {
                throw new PropertyNotFoundException("configuration.url property not found");
            }
        }
        return CONNECTION_URL;
    }

    public void setConnetionUrl(String connetionUrl) {
        this.CONNECTION_URL = connetionUrl;
    }

    public String getUsername() {

        if (USERNAME == null) {
            if ((USERNAME = properties.getProperty("connection.username")) == null) {
                throw new PropertyNotFoundException("connection.username property not found");
            }
        }
        return USERNAME;
    }

    public void setUsername(String username) {
        this.USERNAME = username;
    }

    public String getPassword() {

        if (PASSWORD == null) {
            if ((PASSWORD = properties.getProperty("connection.password")) == null) {
                throw new PropertyNotFoundException("connection.password property not found");
            }
        }
        return PASSWORD;
    }

    public void setPassword(String password) {
        this.PASSWORD = password;
    }

    public int getNumberOfDestinations() {

        if (NUM_DESTINATIONS < 1) {
            String destinationCount;
            if ((destinationCount = properties.getProperty("destination.count")) == null) {
                throw new PropertyNotFoundException("destination.count property not found");
            }
            else {
                NUM_DESTINATIONS = Integer.parseInt(destinationCount);
            }
        }
        return NUM_DESTINATIONS;
    }

    public void setNumberOfDestinations(int numDestinations) {

        if (numDestinations < 1) {
            throw new IllegalArgumentException("Value can not be less than 1");
        }
        else {
            NUM_DESTINATIONS = numDestinations;
        }
    }

    public List<String> getDestinationList() {

        DESTINATION_LIST = new ArrayList<>();

        for (int i = 0; i < getNumberOfDestinations(); i++) {
            DESTINATION_LIST.add("Destination-" + i);
        }
        return DESTINATION_LIST;
    }

    public void setDestinationList(List<String> destinationList) {
        this.DESTINATION_LIST = destinationList;
    }

    public int getChannelType() {

        if (CHANNEL_TYPE < 1) {
            String channelType;
            if ((channelType = properties.getProperty("destination.channel-type")) == null) {
                throw new PropertyNotFoundException("destination.channel-type property not found");
            }
            else {
                CHANNEL_TYPE = Integer.parseInt(channelType);
                if (CHANNEL_TYPE != 0 && CHANNEL_TYPE != 1) {
                    throw new IllegalArgumentException("Invalid value for destination.channel-type");
                }
            }
        }
        return CHANNEL_TYPE;
    }

    public void setChannelType(int channelType) {
        this.CHANNEL_TYPE = channelType;
    }

    public int getProducersPerDestination() {

        if (NUM_PRODUCERS_PER_DESTINATION < 1) {
            String producersPerDestination;
            if ((producersPerDestination = properties.getProperty("producer.count-per-destination")) == null) {
                throw new PropertyNotFoundException("producer.count-per-destination property not found");
            } else {
                NUM_PRODUCERS_PER_DESTINATION = Integer.parseInt(producersPerDestination);
                if (NUM_PRODUCERS_PER_DESTINATION < 1) {
                    throw new IllegalArgumentException("Invalid value for producer.count-per-destination");
                }
            }
        }
        return NUM_PRODUCERS_PER_DESTINATION;
    }

    public void setNumberOfProducersPerDestination(int numProducersPerDestination) {

        if (numProducersPerDestination < 1) {
            throw new IllegalArgumentException("Invalid value for numProducersPerDestination");
        }
        this.NUM_PRODUCERS_PER_DESTINATION = numProducersPerDestination;
    }

    public int getNumberOfMessagesPerProducer() {

        if (MESSAGES_PER_PRODUCER < 1) {
            String messagesPerProducer;
            if ((messagesPerProducer = properties.getProperty("producer.message-count-per-producer")) == null) {
                throw new PropertyNotFoundException("producer.message-count-per-producer property not found");
            } else {
                MESSAGES_PER_PRODUCER = Integer.parseInt(messagesPerProducer);
                if (MESSAGES_PER_PRODUCER < 1) {
                    throw new IllegalArgumentException("Invalid value for producer.message-count-per-producer");
                }
            }
        }
        return MESSAGES_PER_PRODUCER;
    }

    public void setNumberOfMessagesPerProducer(int numMessagesPerProducer) {

        if (numMessagesPerProducer < 1) {
            throw new IllegalArgumentException("Invalid value for numMessagesPerProducer");
        }
        this.MESSAGES_PER_PRODUCER = numMessagesPerProducer;
    }

    public int getNumberOfConsumersPerDestination() {

        if (NUM_CONSUMERS_PER_DESTINATION < 1) {
            String numConsumersPerDestination;
            if ((numConsumersPerDestination = properties.getProperty("consumer.count-per-destination")) == null) {
                throw new PropertyNotFoundException("consumer.count-per-destination property not found");
            } else {
                NUM_CONSUMERS_PER_DESTINATION = Integer.parseInt(numConsumersPerDestination);
                if (NUM_CONSUMERS_PER_DESTINATION < 1) {
                    throw new IllegalArgumentException("Invalid value for consumer.count-per-destination");
                }
            }
        }
        return NUM_CONSUMERS_PER_DESTINATION;
    }

    public void setNumberOfConsumersPerDestination(int numConsumersPerDestination) {

        if (numConsumersPerDestination < 1) {
            throw new IllegalArgumentException("Invalid value for numConsumersPerDestination");
        }
        this.NUM_CONSUMERS_PER_DESTINATION = numConsumersPerDestination;
    }

    public int getConsumerMaxTimeout() {

        if (CONSUMER_MAX_TIMEOUT < 1) {
            String consumerMaxTimeout;
            if ((consumerMaxTimeout = properties.getProperty("consumer.max-timeout")) == null) {
                throw new PropertyNotFoundException("consumer.max-timeout property not found");
            } else {
                CONSUMER_MAX_TIMEOUT = Integer.parseInt(consumerMaxTimeout);
                if (CONSUMER_MAX_TIMEOUT < 1) {
                    throw new IllegalArgumentException("Invalid value for consumer.max-timeout");
                }
            }
        }
        return CONSUMER_MAX_TIMEOUT;
    }

    public void setConsumerMaxTimeout(int consumerMaxTimeout) {

        if (consumerMaxTimeout < 1) {
            throw new IllegalArgumentException("Invalid value for consumerMaxTimeout");
        }
        this.CONSUMER_MAX_TIMEOUT = consumerMaxTimeout;
    }

    public int getMessageSize() {

        if (MESSAGE_SIZE < 1) {
            String messageSize;
            if (((messageSize) = properties.getProperty("message-size")) == null) {
                throw new PropertyNotFoundException("message-size property not found");
            } else {
                MESSAGE_SIZE = Integer.parseInt(messageSize.substring(0, messageSize.length() -1));
                if (MESSAGE_SIZE < 1) {
                    throw new IllegalArgumentException("Invalid value for message-size");
                }
            }
        }
        return MESSAGE_SIZE;
    }

    //public void setMessageSize(String messageSize ) {}
}
