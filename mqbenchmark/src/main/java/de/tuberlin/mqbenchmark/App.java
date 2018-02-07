package de.tuberlin.mqbenchmark;

import de.tuberlin.mqbenchmark.configuration.Configuration;
import de.tuberlin.mqbenchmark.consumer.Consumer;
import de.tuberlin.mqbenchmark.monitoring.Experimental;
import de.tuberlin.mqbenchmark.monitoring.Monitoring;
import de.tuberlin.mqbenchmark.producer.Producer;

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {

        Benchmark benchmark = new Benchmark(args);
        benchmark.execute();

        //System.out.println(Long.toString(System.currentTimeMillis()).length());

        //runTasks();
        //(new Monitoring()).run();
        //(new Experimental()).run();

    }

    private static void runTasks() {

        Configuration configuration = new Configuration();

        List<Runnable> tasks = new ArrayList<>();

        tasks.addAll(Producer.generateProducers(configuration));
        tasks.addAll(Consumer.generateConsumers(configuration));

        tasks.stream().forEach(Runnable::run);
    }
}
