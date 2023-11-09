package fr.it_akademy_book.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StyleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Style getStyleSample1() {
        return new Style().id(1L).styleOfText("styleOfText1");
    }

    public static Style getStyleSample2() {
        return new Style().id(2L).styleOfText("styleOfText2");
    }

    public static Style getStyleRandomSampleGenerator() {
        return new Style().id(longCount.incrementAndGet()).styleOfText(UUID.randomUUID().toString());
    }
}
