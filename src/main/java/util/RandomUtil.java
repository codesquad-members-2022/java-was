package util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final int MAX_RANGE = 10;

    private static RandomUtil instance = new RandomUtil();

    private RandomUtil() {}

    public static RandomUtil getInstance() {
        if (instance == null){
            return new RandomUtil();
        }
        return instance;
    }

    public boolean isEvenNumber() {
        return getInt() % 2 == 0;
    }

    private strictfp int getInt() {
        return random.nextInt(MAX_RANGE);
    }
}
