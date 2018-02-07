package de.tuberlin.mqbenchmark.util;

import java.time.Instant;

public class Util {

    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getTimeMicros() {

        long time = (Instant.now().toEpochMilli() * 1000) + (Instant.now().getNano() / 1000);


        System.out.println(Instant.now().getEpochSecond());
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(Instant.now());
        long nano = Instant.now().getNano();
        long nano1 = nano / (100*1000);
        System.out.println(nano);
        System.out.println(nano1);
        System.out.println(time);
        //System.out.println(time.substring(20,26));
        return 0;
    }
}
