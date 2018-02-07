package de.tuberlin.mqbenchmark.monitoring;

import de.tuberlin.mqbenchmark.util.Util;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

import javax.jms.*;
import java.time.Instant;
import java.util.Enumeration;

public class BrokerStatistics {

    public static void main(String[] args) {

        //Util.getTimeMicros();

        long x = 0;

        System.out.println(x++);

//        long time = 1517101681337L;
//        System.out.println(time);
//        System.out.println(((time / 1000) + 1 ) *1000);

        /*try {

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);


            Queue replyTo = session.createTemporaryQueue();
            MessageConsumer consumer = session.createConsumer(replyTo);

            String queueName = "ActiveMQ.Statistics.Broker";
            Queue testQueue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(testQueue);
            Message msg = session.createMessage();
            msg.setJMSReplyTo(replyTo);
            producer.send(msg);

            MapMessage reply = (MapMessage) consumer.receive();
            //assertNotNull(reply);

            for (Enumeration e = reply.getMapNames(); e.hasMoreElements();) {
                reply.getString()
                String name = e.nextElement().toString();
                System.out.println(name + "=" + reply.getObject(name));
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }*/

    }
}
