package de.tuberlin.mqbenchmark.producer;

import de.tuberlin.mqbenchmark.Config;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer implements Runnable {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Topic topic;
    private MessageProducer producer;

    /**
     * Default constructor bootstraps the necessary objects
     */
    public Producer() {

        try {
            //Creates an ActiveMQ connection factory
            connectionFactory = new ActiveMQConnectionFactory(Config.CONNECTION_STRING);
            connectionFactory.setUserName(Config.USERNAME);
            connectionFactory.setPassword(Config.PASSWORD);

            //Creates a new connection
            connection = connectionFactory.createConnection();
            connection.start();

            //Creates a new session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Create a producer for the appropriate message channel
            producer = createProducer();
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
            for (long i = 0; i < Config.MESSAGES_PER_PRODUCER; i++) {
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
     * Sets the appropriate channel type as defined in the Config
     */
    private MessageProducer createProducer() throws JMSException {

        if (Config.CHANNEL_TYPE == 0) {
            destination = session.createQueue(Config.DESTINATION);
            return session.createProducer(destination);
        }
        else if (Config.CHANNEL_TYPE == 1) {
            topic = session.createTopic(Config.DESTINATION);
            return session.createProducer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }

    /**
     * Generates TextMessages with a unique MESSAGE_ID
     */
    private Message generateMessage() throws JMSException {

        String text = Long.toString(System.currentTimeMillis());
        TextMessage message = session.createTextMessage(text);
        return message;
    }
}
