package de.tuberlin.mqbenchmark;

import de.tuberlin.mqbenchmark.configuration.Configuration;
import de.tuberlin.mqbenchmark.consumer.Consumer;
import de.tuberlin.mqbenchmark.monitoring.Experimental;
import de.tuberlin.mqbenchmark.producer.Producer;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.ArrayList;
import java.util.List;

public class Benchmark {

    private Configuration config;

    private int warmups;
    private int runs;

    private String configFilePath;
    private String mode;

    public Benchmark(String[] args) {

        ArgumentParser parser = ArgumentParsers
                .newFor("MQ-Benchmark")
                .build();

        parser.addArgument("--config")
                .required(false)
                .help("Configuration file path <filepath>");

        parser.addArgument("--mode")
                .choices("producer", "consumer")
                .required(true)
                .help("Benchmark mode <producer | consumer>");

        Namespace ns = null;

        try {

            ns = parser.parseArgs(args);
            String temp;

            if ((temp = ns.getString("config")) != null) {
                configFilePath = temp;
                config = new Configuration(configFilePath);
            } else {
                config = new Configuration();
            }

            mode = ns.getString("mode");

        } catch (ArgumentParserException e) {
            e.printStackTrace();
        }

    }

    public void execute() {

        if (mode.toLowerCase().equals("producer")) {
            Producer.generateProducers(config).stream().forEach(Runnable::run);
        }
        else if (mode.toLowerCase().equals("consumer")) {
            Consumer.generateConsumers(config).stream().forEach(Runnable::run);
            (new Experimental()).run();
        }
    }
}
