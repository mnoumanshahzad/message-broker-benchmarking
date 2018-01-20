package example;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.protocol.jms.sampler.JMSSampler;

import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestElementTraverser;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;



import javax.jms.*;

public class JMeterTestFromCode {

    public static void start(){
        // Engine
        StandardJMeterEngine jm = new StandardJMeterEngine();
        // jmeter.properties
        JMeterUtils.loadJMeterProperties("example/jmeter.properties");


        HashTree hashTree = new HashTree();

//        // HTTP Sampler
//        HTTPSampler httpSampler = new HTTPSampler();
//        httpSampler.setDomain("www.google.com");
//        httpSampler.setPort(80);
//        httpSampler.setPath("/");
//        httpSampler.setMethod("GET");

        // Loop Controller
        TestElement loopCtrl = new LoopController();
        ((LoopController)loopCtrl).setLoops(4);
//        ((LoopController)loopCtrl).addTestElement(httpSampler);
        ((LoopController)loopCtrl).setFirst(true);

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(5);
        threadGroup.setRampUp(0);

        SubscriberSampler subs = new SubscriberSampler();
        subs.setName("Sample Subscriber");
        subs.setProviderUrl(Config.CONNECTION_STRING);
        subs.setConnectionFactory(Config.CONNECTION_FACTORY);
        subs.setUsername(Config.USERNAME);
        subs.setPassword(Config.PASSWORD);


        PublisherSampler publisher = new PublisherSampler();
        publisher.setName("Sample Publisher");
        publisher.setProviderUrl(Config.CONNECTION_STRING);
        publisher.setConnectionFactory(Config.CONNECTION_FACTORY);
        publisher.setUsername(Config.USERNAME);
        publisher.setPassword(Config.PASSWORD);


        JMSSampler p2PSampler = new JMSSampler();
        p2PSampler.setName("JMS point to point");
        p2PSampler.setQueueConnectionFactory(Config.CONNECTION_FACTORY);
//        Argument arg[2] = new Argument();
//
//        arg[0].setName("queue.Q.REQ");
//        arg[0].setValue("Example test A");
//        arg[1].setName("queue.Q.RPL");
//        arg[1].setValue("Example test B");
//        p2PSampler.setJNDIProperties(arg);




//        TopicSubscriber subscriber = new TopicSubscriber() {
//            @Override
//            public Topic getTopic() throws JMSException {
//                return null;
//            }
//
//            @Override
//            public boolean getNoLocal() throws JMSException {
//                return false;
//            }
//
//            @Override
//            public String getMessageSelector() throws JMSException {
//                return null;
//            }
//
//            @Override
//            public MessageListener getMessageListener() throws JMSException {
//                return null;
//            }
//
//            @Override
//            public void setMessageListener(MessageListener messageListener) throws JMSException {
//
//            }
//
//            @Override
//            public Message receive() throws JMSException {
//                return null;
//            }
//
//            @Override
//            public Message receive(long l) throws JMSException {
//                return null;
//            }
//
//            @Override
//            public Message receiveNoWait() throws JMSException {
//                return null;
//            }
//
//            @Override
//            public void close() throws JMSException {
//
//            }
//        };


//        threadGroup.setSamplerController((LoopController)loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");
        testPlan.addThreadGroup(threadGroup);
//        testPlan.
//      testPlan.add
        hashTree.add("testPlan", testPlan);
        hashTree.add("loopCtrl", loopCtrl);
        hashTree.add("threadGroup", threadGroup);
//        hashTree.add("httpSampler", httpSampler);

        jm.configure(hashTree);

        jm.run();
    }
}