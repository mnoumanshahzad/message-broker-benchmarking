package jmeterExample;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

import java.io.FileOutputStream;
import java.io.IOException;

public class JMeterExample {

    public static void main(String[] args) {

        //Initializing JMeter Engine
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();

        //Loading JMeter Properties
        JMeterUtils.setJMeterHome("/Users/mshahzad/Downloads/apache-jmeter-3.3");
        JMeterUtils.loadJMeterProperties("/Users/mshahzad/Downloads/apache-jmeter-3.3/bin/jmeter.properties");
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        //JMeter Test Plan Data Structure
        HashTree testPlanTree = new HashTree();

        //JMS Publisher Sampler
        PublisherSampler jmsPublisher = new PublisherSampler();
        jmsPublisher.setJNDIIntialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jmsPublisher.setProviderUrl("tcp://localhost:61616");
        jmsPublisher.setConnectionFactory("ConnectionFactory");
        jmsPublisher.setDestination("dynamicQueues/JavaTestPlan");
        jmsPublisher.setIterations("10");
        jmsPublisher.setTextMessage("Java JMeter Example");
        jmsPublisher.setProperty(TestElement.TEST_CLASS, PublisherSampler.class.getName());

        //JMS Subscriber Sampler
        SubscriberSampler jmsSubscriber = new SubscriberSampler();
        jmsSubscriber.setJNDIIntialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jmsSubscriber.setProviderUrl("tcp://localhost:61616");
        jmsSubscriber.setConnectionFactory("ConnectionFactory");
        jmsSubscriber.setDestination("dynamicQueues/JavaTestPlan");
        jmsSubscriber.setIterations("10");


        //Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(1);
        loopController.setProperty(TestElement.TEST_CLASS, LoopController.class.getName());
        loopController.setFirst(true);
        loopController.initialize();

        //Thread Group
        ThreadGroup threadGroup = new ThreadGroup();
        threadGroup.setName("JMeter Thread Group");
        threadGroup.setNumThreads(8);
        threadGroup.setRampUp(0);
        threadGroup.setSamplerController(loopController);
        threadGroup.setProperty(TestElement.TEST_CLASS,ThreadGroup.class.getName());
        threadGroup.addTestElement(jmsPublisher);

        TestPlan testPlan = new TestPlan("JMeter Test from Java");
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setUserDefinedVariables((Arguments) new ArgumentsPanel().createTestElement());


        testPlanTree.add("testPlan", testPlan);
        HashTree threadGroupHashTree = testPlanTree.add(testPlan,threadGroup);
        threadGroupHashTree.add(jmsPublisher);
        //threadGroupHashTree.add(jmsSubscriber);

        try {
            SaveService.saveTree(testPlanTree, new FileOutputStream("/Users/mshahzad/tub/Enterprise Computing/Assignments/assignment-3/message-broker-benchmarking/activeMQ/target/example.jmx"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();

    }

}