package generator;

import java.util.Random;

public class Generator {

    public final static int TOKEN_LENGTH = 32;
    public final static String LOGZIO_PREFIX = "logz-";

    public static String generateToken() {
        // Does not include 'l' or 'I'
        String characters = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        return generateString(new Random(), characters, TOKEN_LENGTH);
    }

    public static String generateElasticsearchIndexName() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        return LOGZIO_PREFIX + generateString(new Random(), characters, TOKEN_LENGTH);
    }


    /**
     * Generate a random string.
     *
     * @param random the random number generator.
     * @param characters the characters for generating string.
     * @param length the length of the generated string.
     * @return
     */
    private static String generateString(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }
}
