package de.tuberlin.mqbenchmark;

import de.tuberlin.mqbenchmark.consumer.Consumer;
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

        runTasks();
        (new Monitoring()).run();
        /*System.out.println("EpochMilli: " + Instant.now().toEpochMilli());
        System.out.println("Nano:       " + (Instant.now().getNano()));
        System.out.println("Nano:       " + (((long)Instant.now().getNano()) / 1000));
        System.out.println("InstantNow: " + Instant.now());*/
        //System.out.println("Nano:    " + System.nanoTime());

    }

    private static void runTasks() {
        List<Runnable> tasks = new ArrayList<>();
        //tasks.add(new Producer());
        tasks.add(new Producer());
        tasks.add(new Producer());
        tasks.add(new Producer());
        tasks.add(new Producer());
        //tasks.add(new Consumer());
        tasks.add(new Consumer());
        tasks.add(new Consumer());

        tasks.stream().forEach(Runnable::run);
    }
}
