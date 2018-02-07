package de.tuberlin.mqbenchmark.monitoring;

import de.tuberlin.mqbenchmark.message.MyActiveMQTextMessage;

import javax.jms.JMSException;
import java.util.Arrays;
import java.util.HashMap;

public class Experimental implements Runnable {

    @Override
    public void run() {

        MyActiveMQTextMessage[] sortedMessages = new MyActiveMQTextMessage[ReceivedMessageQueue.recievedMessages.size()];

        sortedMessages = ReceivedMessageQueue.recievedMessages.toArray(sortedMessages);

        Arrays.sort(sortedMessages);

        long startTime = sortedMessages[0].getMessage().getBrokerInTime();

        long startSecond = startTime / 1000;

        long normalizedSecond = (startSecond + 1) * 1000;

        HashMap<Long, Long> messagePerSecond = new HashMap<Long, Long>();

        //System.out.println("Queue size: " + ReceivedMessageQueue.recievedMessages.size());
        //System.out.println("Remaining Capacity" + ReceivedMessageQueue.recievedMessages.remainingCapacity());
        //System.out.println("" + ReceivedMessageQueue.recievedMessages.\);
        /*try {
            System.out.println("MessageSize: " + sortedMessages[0].getMessage().getText().length());
        } catch (JMSException e) {
            e.printStackTrace();
        }*/

        //System.out.println("Sorted Messages Size: " + sortedMessages.length);

        long count = 0;

        for (MyActiveMQTextMessage message : sortedMessages) {

            if (message.getMessage().getBrokerInTime() < normalizedSecond) {
                messagePerSecond.put(startSecond, ++count);
            } else {
                startSecond += 1;
                normalizedSecond = (startSecond + 1) * 1000;
                count = 0;
                messagePerSecond.put(startSecond, ++count);
            }
        }

        //System.out.println("MessagePerSecond Size: " + messagePerSecond.size());

        System.out.println("Throughput...!");
        for (long key : messagePerSecond.keySet()) {

            System.out.println("Time: " + key + ". Messages: " + messagePerSecond.get(key));
        }

    }
}
