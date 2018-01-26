package de.tuberlin.mqbenchmark.monitoring;

import javax.jms.Message;
import java.util.concurrent.LinkedBlockingQueue;

public class ReceivedMessageQueue {

    public static LinkedBlockingQueue<Message> recievedMessageQueue = new LinkedBlockingQueue<>();

}
