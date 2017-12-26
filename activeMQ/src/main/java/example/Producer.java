package example;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer implements Runnable {

    private static final String CONNECTION_STRING = Config.CONNECTION_STRING;
    private static final String QUEUE_NAME = Config.QUEUE_NAME;
    private static long PRODUCER_ID = 0;
    private static long NUMBER_OF_MESSAGES = Config.MESSAGES_PER_PRODUCER;
    private static long MESSAGE_ID = 0;

    private long currentProducerId;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    /**
     * Default constructor bootstraps the necessary objects
     */
    public Producer() {

        currentProducerId = PRODUCER_ID++;

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

            //Create a producer for the above mentioned queue
            //and specifies not to persist messages sent by the producer
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        catch (JMSException e) { e.printStackTrace(); }
    }

    /**
     * Produces the specified number of messages
     */
    public void run() {

        try {
            for (long i = 0; i < NUMBER_OF_MESSAGES; i++) {
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
     * Generates TextMessages with a unique MESSAGE_ID and current Thread Name and ID
     */
    private Message generateMessage() throws JMSException {

        String text = "Hello from producer: " + currentProducerId + ". Delivering you Message #: " + MESSAGE_ID++;
        TextMessage message = session.createTextMessage(text);
        return message;
    }
}
