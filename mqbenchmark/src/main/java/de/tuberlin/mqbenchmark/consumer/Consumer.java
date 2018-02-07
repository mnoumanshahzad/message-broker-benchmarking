package de.tuberlin.mqbenchmark.consumer;

import de.tuberlin.mqbenchmark.configuration.Configuration;
import de.tuberlin.mqbenchmark.message.MyActiveMQTextMessage;
import de.tuberlin.mqbenchmark.monitoring.ReceivedMessageQueue;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

public class Consumer implements Runnable {

    private static Configuration config;

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Topic topic;
    private MessageConsumer consumer;

    /**
     * Constructor bootstraps the necessary objects
     */
    public Consumer(String destinationName) {

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

            //Creates a consumer for the appropriate message channel
            consumer = createConsumer(destinationName);

        } catch (JMSException e) { e.printStackTrace(); }


    }

    /**
     * Consumes messages from a broker until no more new messages appear
     * for 5 seconds
     * Prints the consumed message on the console
     */
    @Override
    public void run() {

        try{

            ActiveMQTextMessage message;

            //MyActiveMQTextMessage message_;

            /*while((message = nextMessage()) != null) {
                ReceivedMessageQueue.recievedMessageQueue.put(message);
            }*/

            while ((message = (ActiveMQTextMessage) consumer.receive(config.getConsumerMaxTimeout())) != null) {
                ReceivedMessageQueue.recievedMessages.put(
                        new MyActiveMQTextMessage(message,
                                Long.parseLong(message.getText().split(",")[0]),
                                System.currentTimeMillis()));
            }
            close();

        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the session and connection objects
     */
    private void close() throws JMSException{

        session.close();
        connection.close();
    }

    /**
     * Sets the appropriate channel type as defined in the Configuration
     */
    private MessageConsumer createConsumer(String destinationName) throws  JMSException{

        if (config.getChannelType() == 0) {
            destination = session.createQueue(destinationName);
            return session.createConsumer(destination);
        }
        else if (config.getChannelType() == 1) {
            topic = session.createTopic(destinationName);
            return session.createConsumer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }

    private Message nextMessage() throws JMSException {
        Message message = consumer.receive(config.getConsumerMaxTimeout());

        if (message != null) {
            return message;
        }
        else return null;
    }

    public static List<Runnable> generateConsumers(Configuration configuration) {

        config = configuration;

        List<Runnable> consumerList = new ArrayList<>();

        for (String destinationName: config.getDestinationList()) {
            for (int i = 0; i < config.getNumberOfConsumersPerDestination(); i++) {
                consumerList.add(new Consumer(destinationName));
            }
        }
        return consumerList;
    }
}
