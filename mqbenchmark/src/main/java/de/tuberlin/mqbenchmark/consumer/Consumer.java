package de.tuberlin.mqbenchmark.consumer;

import de.tuberlin.mqbenchmark.Config;
import de.tuberlin.mqbenchmark.monitoring.ReceivedMessageQueue;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer implements Runnable {

    private ActiveMQConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Topic topic;
    private MessageConsumer consumer;

    /**
     * Default constructor bootstraps the necessary objects
     */
    public Consumer() {

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

            //Creates a consumer for the appropriate message channel
            consumer = createConsumer();

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

            Message message;

            while((message = nextMessage()) != null) {
                //Thread.sleep(2);
                ReceivedMessageQueue.recievedMessageQueue.put(message);
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
     * Sets the appropriate channel type as defined in the Config
     */
    private MessageConsumer createConsumer() throws  JMSException{

        if (Config.CHANNEL_TYPE == 0) {
            destination = session.createQueue(Config.DESTINATION);
            return session.createConsumer(destination);
        }
        else if (Config.CHANNEL_TYPE == 1) {
            topic = session.createTopic(Config.DESTINATION);
            return session.createConsumer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }

    private Message nextMessage() throws JMSException {

        long currentTime = System.currentTimeMillis();
        Message message = consumer.receive(Config.CONSUMER_MAX_TIMEOUT);

        if (message != null) {
            //message.setLongProperty("arivalTime", currentTime);
            return message;
        }
        else return null;
    }
}
