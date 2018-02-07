package de.tuberlin.mqbenchmark.monitoring;

import de.tuberlin.mqbenchmark.message.MyActiveMQTextMessage;

import javax.jms.Message;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceivedMessageQueue {

    public static LinkedBlockingQueue<Message> recievedMessageQueue = new LinkedBlockingQueue<>();

    public static LinkedBlockingQueue<MyActiveMQTextMessage> recievedMessages = new LinkedBlockingQueue<>();

}
