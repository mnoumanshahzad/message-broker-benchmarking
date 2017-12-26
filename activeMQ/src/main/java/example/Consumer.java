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

            //Specifies the destination.
            //A queue in this case
            //Can be a pub-sub topic as well
            destination = session.createQueue(QUEUE_NAME);

            //Creates a consumer for the above mentioned queue
            consumer = session.createConsumer(destination);

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
                System.out.println(message.getText() + ". Received by example.Consumer: " + currentConsumerId);
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
}
