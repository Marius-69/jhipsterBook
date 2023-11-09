package fr.it_akademy_book.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StyleDeuxTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StyleDeux getStyleDeuxSample1() {
        return new StyleDeux().id(1L).styleOfTextDeux("styleOfTextDeux1");
    }

    public static StyleDeux getStyleDeuxSample2() {
        return new StyleDeux().id(2L).styleOfTextDeux("styleOfTextDeux2");
    }

    public static StyleDeux getStyleDeuxRandomSampleGenerator() {
        return new StyleDeux().id(longCount.incrementAndGet()).styleOfTextDeux(UUID.randomUUID().toString());
    }
}
