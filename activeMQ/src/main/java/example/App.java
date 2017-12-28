package example;

import java.util.ArrayList;
import java.util.List;

/**
 * An example to demonstrate the two different operating modes for ActiveMQ.
 *
 *   1. Point-to-Point
 *          "Sender" produces the messages and sends to an ActiveMQ "queue"
 *          "Receiver" reads a message from a broker
 *          A message can be read by "exactly one" receiver
 *
 *   2. Publish-Subscribe
 *          "Producer" produces the messages and sends to an ActiveMQ "topic"
 *          "Consumer" subscribe to a "topic"
 *          A message is broadcasted to "all" the consumers that subscribed to
 *          the topic
 *
 *   Use the CHANNEL_TYPE parameter in the {@link Config} class to choose
 *   between the two options
 */
public class App {

    private static List<Runnable> parallelTasks = new ArrayList<>();

    public static void main(String[] args) {

        runParallelTasks();
    }

    /**
     * Runs all threads in parallel
     */
    private static void runParallelTasks() {

        createConsumers(Config.NUM_CONSUMERS);
        createProducers(Config.NUM_PRODUCERS);

        parallelTasks
                .parallelStream()
                .forEach(Runnable::run);
    }

    /**
     * @param numProducers  Number of Producer threads to create
     */
    private static void createProducers (int numProducers) {

        for (int i = 0; i < numProducers; i++) {
            parallelTasks.add(new Producer());
        }
    }

    /**
     *
     * @param numConsumers  Number of Consumer threads to create
     */
    private static void createConsumers (int numConsumers) {

        for (int i = 0; i<numConsumers; i++) {
            parallelTasks.add(new Consumer());
        }
    }
}
