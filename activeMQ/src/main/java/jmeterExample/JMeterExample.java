package jmeterExample;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import org.apache.jmeter.threads.ThreadGroup;

public class JMeterExample {

    public static void main(String[] args) {

        //Initializing JMeter Engine
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();

        //Loading JMeter Properties
        JMeterUtils.loadJMeterProperties("src/main/resources/jmeter.properties");

        //JMeter Test Plan Data Structure
        HashTree testPlanTree = new HashTree();

        //JMS Publisher Sampler
        PublisherSampler jmsPublisher = new PublisherSampler();
        jmsPublisher.setJNDIIntialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jmsPublisher.setProviderUrl("tcp://localhost:61616");
        jmsPublisher.setConnectionFactory("ConnectionFactory");
        jmsPublisher.setDestination("JavaTestPlan");
        jmsPublisher.setIterations("10");
        jmsPublisher.setTextMessage("Java JMeter Example");

        //JMS Subscriber Sampler
        SubscriberSampler jmsSubscriber = new SubscriberSampler();
        jmsSubscriber.setJNDIIntialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jmsSubscriber.setProviderUrl("tcp://localhost:61616");
        jmsSubscriber.setConnectionFactory("ConnectionFactory");
        jmsSubscriber.setDestination("JavaTestPlan");
        jmsSubscriber.setIterations("10");


        //Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(2);
        loopController.addTestElement(jmsPublisher);
        loopController.addTestElement(jmsSubscriber);
        loopController.setFirst(true);
        loopController.initialize();

        //Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setNumThreads(5);
        threadGroup.setRampUp(0);
        threadGroup.setSamplerController(loopController);

        TestPlan testPlan = new TestPlan("JMeter Test from Java");

        testPlanTree.add("testPlan", testPlan);
        testPlanTree.add("loopController", loopController);
        testPlanTree.add("threadGroup", threadGroup);
        testPlanTree.add("jmsPublisher", jmsPublisher);
        testPlanTree.add("jmsSubscriber", jmsSubscriber);

        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();

    }

}
