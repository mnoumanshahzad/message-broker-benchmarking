package example;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.jms.client.Publisher;
import org.apache.jmeter.protocol.jms.client.ClientPool;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.protocol.jms.sampler.JMSSampler;

import org.apache.jmeter.*;
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
        JMeterUtils.loadJMeterProperties("/media/taha/D0A65441A65429EC/masters/3rdSemester/EnterpriseComputing/message-broker-benchmarking/activeMQ/src/main/java/example/jmeter.properties");




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




        SubscriberSampler subs = new SubscriberSampler();
        subs.setName("Sample Subscriber");
        subs.setProviderUrl(Config.CONNECTION_STRING);
        subs.setConnectionFactory(Config.CONNECTION_FACTORY);
        subs.setUsername(Config.USERNAME);
        subs.setPassword(Config.PASSWORD);

        loopCtrl.addTestElement(subs);

        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(5);
        threadGroup.setRampUp(0);
        threadGroup.addTestElement(loopCtrl);


        PublisherSampler publisher = new PublisherSampler();
        publisher.setName("Sample Publisher");
        publisher.setProviderUrl(Config.CONNECTION_STRING);
        publisher.setConnectionFactory(Config.CONNECTION_FACTORY);
        publisher.setUsername(Config.USERNAME);
        publisher.setPassword(Config.PASSWORD);
        publisher.setTextMessage("Hello message from Publisher");


        JMSSampler p2PSampler = new JMSSampler();
        p2PSampler.setName("JMS point to point");
        p2PSampler.setQueueConnectionFactory(Config.CONNECTION_FACTORY);

        Arguments args = new Arguments();
        args.addArgument("queue.Q.REQ","Example test A");
        args.addArgument("queue.Q.REQ","Example test B");
//        ThreadGroup

        p2PSampler.setJNDIProperties(args);
        p2PSampler.setUseReqMsgIdAsCorrelId(true);
        p2PSampler.setUseResMsgIdAsCorrelId(true);
        p2PSampler.setTimeout("2000");
        p2PSampler.setExpiration("0");
        p2PSampler.setPriority("4");
        p2PSampler.setContent("testing point to point");
        p2PSampler.setInitialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        p2PSampler.setContextProvider(Config.CONNECTION_STRING);
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


//        Publisher pub =  new Publisher();
//        pub.publish("Hello from ", String destinationName, Map<String,Object> properties, int        deliveryMode, int priority, long expiration);
//
//        ClientPool clientPool = new ClientPool();
//        threadGroup.setSamplerController((LoopController)loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");
//        testPlan.addThreadGroup(threadGroup);
//        testPlan.
//      testPlan.add



        HashTree hashTree = new HashTree();
        hashTree.add("testPlan", testPlan);
//        hashTree.add("loopCtrl", loopCtrl);
//        hashTree.add("threadGroup", threadGroup);
//        hashTree.add("publisher", publisher);
//        hashTree.add("subs", subs);
////        hashTree.add("httpSampler", httpSampler);
//        hashTree.add("P2Psampler", p2PSampler);


        jm.configure(hashTree);
        System.out.println("starts running test");

//        try {
            jm.run();
//        }catch(Exception e){
//            System.out.println(e);
//        }
    }
}