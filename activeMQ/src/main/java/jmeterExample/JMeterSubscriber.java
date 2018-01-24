package jmeterExample;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;

public class JMeterSubscriber implements Runnable {
    @Override
    public void run() {
        //Initializing JMeter Engine
        StandardJMeterEngine jMeterEngine = new StandardJMeterEngine();

        //Loading JMeter Properties
        JMeterUtils.setJMeterHome("/Users/mshahzad/Downloads/apache-jmeter-3.3");
        JMeterUtils.loadJMeterProperties("/Users/mshahzad/Downloads/apache-jmeter-3.3/bin/jmeter.properties");
        JMeterUtils.initLogging();
        JMeterUtils.initLocale();

        //JMeter Test Plan Data Structure
        HashTree testPlanTree = new HashTree();

        //JMS Subscriber Sampler
        SubscriberSampler jmsSubscriber = new SubscriberSampler();
        jmsSubscriber.setJNDIIntialContextFactory("org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jmsSubscriber.setProviderUrl("tcp://localhost:61616");
        jmsSubscriber.setConnectionFactory("ConnectionFactory");
        jmsSubscriber.setDestination("dynamicQueues/JavaTestPlan");
        jmsSubscriber.setIterations("10");


        //Loop Controller
        LoopController loopController = new LoopController();
        loopController.setLoops(2);
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
        testPlanTree.add("jmsSubscriber", jmsSubscriber);

        jMeterEngine.configure(testPlanTree);
        jMeterEngine.run();
    }
}
