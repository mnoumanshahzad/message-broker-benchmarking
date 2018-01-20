package example;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
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


        HashTree hashTree = new HashTree();

//        // HTTP Sampler
//        HTTPSampler httpSampler = new HTTPSampler();
//        httpSampler.setDomain("www.google.com");
//        httpSampler.setPort(80);
//        httpSampler.setPath("/");
//        httpSampler.setMethod("GET");

        // Loop Controller
        TestElement loopCtrl = new LoopController();
        ((LoopController)loopCtrl).setLoops(1);
//        ((LoopController)loopCtrl).addTestElement(httpSampler);
        ((LoopController)loopCtrl).setFirst(true);

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(5);
        threadGroup.setRampUp(0);

//        threadGroup.;
        

        SubscriberSampler subs = new SubscriberSampler();
        subs.setName("Sample Subscriber");
        subs.setProviderUrl();
        subs.setUsername();
        subs.setPassword();

        TopicSubscriber subscriber = new TopicSubscriber() {
            @Override
            public Topic getTopic() throws JMSException {
                return null;
            }

            @Override
            public boolean getNoLocal() throws JMSException {
                return false;
            }

            @Override
            public String getMessageSelector() throws JMSException {
                return null;
            }

            @Override
            public MessageListener getMessageListener() throws JMSException {
                return null;
            }

            @Override
            public void setMessageListener(MessageListener messageListener) throws JMSException {

            }

            @Override
            public Message receive() throws JMSException {
                return null;
            }

            @Override
            public Message receive(long l) throws JMSException {
                return null;
            }

            @Override
            public Message receiveNoWait() throws JMSException {
                return null;
            }

            @Override
            public void close() throws JMSException {

            }
        };




//        threadGroup.setSamplerController((LoopController)loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("MY TEST PLAN");



//        testPlan.add
        hashTree.add("testPlan", testPlan);
        hashTree.add("loopCtrl", loopCtrl);
        hashTree.add("threadGroup", threadGroup);
        hashTree.add("httpSampler", httpSampler);

        jm.configure(hashTree);

        jm.run();
    }
}