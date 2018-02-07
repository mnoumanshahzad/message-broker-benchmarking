package de.tuberlin.mqbenchmark.producer;

import de.tuberlin.mqbenchmark.configuration.Configuration;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Producer implements Runnable {

    private static Configuration config;

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Topic topic;
    private MessageProducer producer;

    /**
     * Constructor bootstraps the necessary objects
     */
    public Producer(String destinationName) {

        try {
            //Creates an ActiveMQ connection factory
            connectionFactory = new ActiveMQConnectionFactory(config.getConnetionUrl());
            connectionFactory.setUserName(config.getUsername());
            connectionFactory.setPassword(config.getPassword());

            //Creates a new connection
            connection = connectionFactory.createConnection();
            connection.start();

            //Creates a new session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create a producer for the appropriate message channel
            producer = createProducer(destinationName);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        catch (JMSException e) { e.printStackTrace(); }
    }

    /**
     * Produces the specified number of messages
     */
    @Override
    public void run() {

        try {
            for (long i = 0; i < config.getNumberOfMessagesPerProducer(); i++) {
                producer.send(generateMessage());
            }

            close();
        } catch (JMSException e) { e.printStackTrace(); }
    }

    /**
     * Closes the session and connection objects
     */
    private void close() throws JMSException {

        session.close();
        connection.close();
    }

    /**
     * Sets the appropriate channel type as defined in the Configuration
     */
    private MessageProducer createProducer(String destinationName) throws JMSException {

        if (config.getChannelType() == 0) {
            destination = session.createQueue(destinationName);
            return session.createProducer(destination);
        }
        else if (config.getChannelType() == 1) {
            topic = session.createTopic(destinationName);
            return session.createProducer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }

    /**
     * Generates TextMessages with a unique MESSAGE_ID
     */
    private Message generateMessage() throws JMSException {

        StringBuilder message = new StringBuilder();

        int messageSize = config.getMessageSize();

        int randomPayloadSize = (messageSize*1024) - (Long.toString(System.currentTimeMillis()) + ",").length();

        char[] randomPayload = new char[randomPayloadSize];

        String time = Long.toString(System.currentTimeMillis()) + ",";

        message.append(time).append(randomPayload);

        TextMessage textMessage = session.createTextMessage(message.toString());
        return textMessage;
    }

    public static List<Runnable> generateProducers(Configuration configuration) {

        config = configuration;

        List<Runnable> producerList = new ArrayList<>();

        for (String destinationName : config.getDestinationList()) {
            for (int i = 0; i < config.getProducersPerDestination(); i++) {
                producerList.add(new Producer(destinationName));
            }
        }

        return producerList;
    }
}
