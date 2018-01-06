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
    private Topic topic;
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

            //Create a producer for the appropriate message channel
            long creationStartTime = System.currentTimeMillis();
            producer = createProducer();
            long creationEndTime =System.currentTimeMillis();
            System.out.println("Producer Creation Delay "+ (creationEndTime-creationStartTime));
//            Stats.producerCreationDelay.add(creationEndTime-creationStartTime);
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
     * Sets the appropriate channel type as defined in the Config
     */
    private MessageProducer createProducer() throws JMSException {

        if (Config.CHANNEL_TYPE == 0) {
            destination = session.createQueue(Config.QUEUE_NAME);
            return session.createProducer(destination);
        }
        else if (Config.CHANNEL_TYPE == 1) {
            topic = session.createTopic(Config.TOPIC_NAME);
            return session.createProducer(topic);
        }
        else throw new RuntimeException("Invalid Channel Type");
    }

    /**
     * Generates TextMessages with a unique MESSAGE_ID
     */
    private Message generateMessage() throws JMSException {

//        String text = "Hello from producer: " + currentProducerId + ". Delivering you Message #: " + MESSAGE_ID++;
//        String text = Long.toString(System.currentTimeMillis());
        String text = Long.toString(System.currentTimeMillis());

        TextMessage message = session.createTextMessage(text);
        return message;
    }
}
