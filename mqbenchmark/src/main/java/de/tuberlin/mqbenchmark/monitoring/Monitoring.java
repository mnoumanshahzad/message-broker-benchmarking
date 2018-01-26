package de.tuberlin.mqbenchmark.monitoring;

import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.JMSException;
import java.util.concurrent.TimeUnit;

public class Monitoring implements Runnable {

    private double enqueueTime = 0;
    private double senderLatency = 0;
    private double messageRetries = 0;

    private double commulativeEnqueueTime = 0;
    private double commulativeSenderLatency = 0;
    private double commulativeMessageRetries = 0;

    /**
     * Average Calculations
     */
    private double averageEnqueuedTime = 0;
    private double averageSenderLatency = 0;
    private double averageReceiverLatency = 0;
    private double averageMessageRetries = 0;

    private long numMessages = 0;

    @Override
    public void run() {

        ActiveMQTextMessage message;

        try {
            while((message = (ActiveMQTextMessage) ReceivedMessageQueue.recievedMessageQueue.poll(5, TimeUnit.SECONDS)) != null) {
                numMessages++;
                System.out.println("Broker In Time: " + message.getBrokerInTime());
                calculateMetrics(message);
                printValues();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        calculateAverage();
        printAverages();
    }

    private void calculateMetrics(ActiveMQTextMessage message) throws JMSException {

        enqueueTime = message.getBrokerOutTime() - message.getBrokerInTime();
        senderLatency = message.getJMSTimestamp() - Long.parseLong(message.getText());
        messageRetries = message.getRedeliveryCounter();

        commulativeEnqueueTime += enqueueTime;
        commulativeSenderLatency += senderLatency;
        commulativeMessageRetries += messageRetries;
    }

    private void calculateAverage() {

        averageEnqueuedTime = commulativeEnqueueTime / numMessages;
        averageSenderLatency = commulativeSenderLatency / numMessages;
        //averageReceiverLatency = (averageReceiverLatency + (message.getBrokerOutTime() - message.getLongProperty("arivalTime"))) / numMessages;
        averageMessageRetries = commulativeMessageRetries / numMessages;
    }

    private void printValues() {
        System.out.println("**************************************************");
        System.out.println("Enqueue Time: " + enqueueTime);
        System.out.println("Sender Latency:" + senderLatency);
        System.out.println("Message Retries:" + messageRetries);
    }

    private void printAverages() {
        System.out.println("**************************************************");
        System.out.println("Average Enqueued Time: " + averageEnqueuedTime);
        System.out.println("Average Sender Latency; " + averageSenderLatency);
        //System.out.println("Average Receiver Latency: " + averageReceiverLatency/1000);
        System.out.println("Average Message Retries: " + averageMessageRetries);
        System.out.println("Total Number Of Messages: " + numMessages);
    }
}
