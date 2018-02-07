package de.tuberlin.mqbenchmark.message;

import org.apache.activemq.command.ActiveMQTextMessage;

public class MyActiveMQTextMessage implements Comparable<MyActiveMQTextMessage> {

    ActiveMQTextMessage message;

    protected long sendTime;

    protected long receiveTime;

    public MyActiveMQTextMessage(ActiveMQTextMessage message, long sendTime, long receiveTime) {
        this.sendTime = sendTime;
        this.message = message;
        this.receiveTime = receiveTime;
    }

    public void setSendTime(long timestamp) { this.sendTime = timestamp; }

    public void setReceiveTime(long timestamp) {
        this.receiveTime = timestamp;
    }

    public ActiveMQTextMessage getMessage() {
        return message;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    @Override
    public int compareTo(MyActiveMQTextMessage that) {

        return (this.message.getBrokerInTime() > that.message.getBrokerInTime() ? 1 :
                (this.message.getBrokerInTime()==that.message.getBrokerInTime() ? 0 : 1));

    }
}
