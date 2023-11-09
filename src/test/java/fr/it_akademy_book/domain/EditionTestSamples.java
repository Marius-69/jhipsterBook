package fr.it_akademy_book.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EditionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Edition getEditionSample1() {
        return new Edition().id(1L).dayOfPublication(1L).monthOfPublication("monthOfPublication1");
    }

    public static Edition getEditionSample2() {
        return new Edition().id(2L).dayOfPublication(2L).monthOfPublication("monthOfPublication2");
    }

    public static Edition getEditionRandomSampleGenerator() {
        return new Edition()
            .id(longCount.incrementAndGet())
            .dayOfPublication(longCount.incrementAndGet())
            .monthOfPublication(UUID.randomUUID().toString());
    }
}
