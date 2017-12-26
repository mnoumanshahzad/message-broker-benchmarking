package example;

import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        createProducers(Config.NUM_PRODUCERS);
        createConsumers(Config.NUM_CONSUMERS);
    }

    /**
     * @param numProducers  Number of example.Producer threads to create
     */
    private static void createProducers (int numProducers) {

        List<Producer> producerThreads = new ArrayList<Producer>();
        for (int i = 0; i < numProducers; i++) {
            producerThreads.add(new Producer());
        }

        producerThreads
                .stream()
                .parallel()
                .forEach(Producer::run);
    }

    /**
     *
     * @param numConsumers  Number of example.Consumer threads to create
     */
    private static void createConsumers (int numConsumers) {

        List<Consumer> consumerThreads = new ArrayList<Consumer>();
        for (int i = 0; i<numConsumers; i++) {
            consumerThreads.add(new Consumer());
        }

        consumerThreads
                .stream()
                .parallel()
                .forEach(Consumer::run);
    }
}
