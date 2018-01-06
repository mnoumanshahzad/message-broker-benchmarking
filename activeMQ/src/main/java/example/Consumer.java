package example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer implements Runnable {

    private static final String CONNECTION_STRING = Config.CONNECTION_STRING;
    private static final String QUEUE_NAME = Config.QUEUE_NAME;
    private static long CONSUMER_ID = 0;

    private long currentConsumerId;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private Topic topic;
    private MessageConsumer consumer;

    /**
     * Default constructor bootstraps the necessary objects
     */
    public Consumer() {

        currentConsumerId = CONSUMER_ID++;

        try {
            //Creates an ActiveMQ connection factory
            connectionFactory = new ActiveMQConnectionFactory(CONNECTION_STRING);

            //Creates a new connection
            connection = connectionFactory.createConnection();
            connection.start();

            //Creates a new session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //Creates a consumer for the appropriate message channel
            long creationStartTime = System.currentTimeMillis();
            consumer = createConsumer();
            long creationEndTime = System.currentTimeMillis();
            System.out.println("Consumer Creation Delay "+ (creationEndTime-creationStartTime));
//            Stats.consumerCreationDelay.add(creationEndTime-creationStartTime);

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

            TextMessage message;

            while((message = (TextMessage) consumer.receive(Config.CONSUMER_MAX_TIMEOUT)) != null) {
                System.out.println("Message : " + message.getText() + ". Received by Consumer: " + currentConsumerId
                        + " at " + System.currentTimeMillis()+ " Delivery Delay : " + (System.currentTimeMillis() - Long.parseLong(message.getText())));
            }

            close();

        }catch (JMSException e) { e.printStackTrace(); }

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
            destination = session.createQueue(Config.QUEUE_NAME);
            return session.createConsumer(destination);
        }
        else if (Config.CHANNEL_TYPE == 1) {
            topic = session.createTopic(Config.TOPIC_NAME);
            return session.createConsumer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }
}
