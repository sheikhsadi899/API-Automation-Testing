package utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class RandomUtils {
    private final int LETTERS_CAPITAL_MIN_ASCII = 65; // 'A'
    private final int LETTERS_CAPITAL_MAX_ASCII = 90; // 'Z'
    private final int LETTERS_LOWER_MIN_ASCII = 97; // 'a'
    private final int LETTERS_LOWER_MAX_ASCII = 122; // 'z'
    private final int DEFAULT_STRING_LENGTH = 10;

    public String getRandomAlphabeticString() {
        return getRandomAlphabeticString(DEFAULT_STRING_LENGTH);
    }

    public String getRandomAlphabeticString(int length) {
        return ThreadLocalRandom.current()
                .ints(LETTERS_CAPITAL_MIN_ASCII, LETTERS_LOWER_MAX_ASCII + 1)
                .filter(i -> (i <= LETTERS_CAPITAL_MAX_ASCII || i >= LETTERS_LOWER_MIN_ASCII))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public long getRandomLong(long minInclusive, long maxExclusive) {
        return ThreadLocalRandom.current().nextLong(minInclusive, maxExclusive);
    }

    public long getRandomLong() {
        return getRandomLong(0, Long.MAX_VALUE);
    }
}
