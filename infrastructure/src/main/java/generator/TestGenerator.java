package generator;

import com.mifmif.common.regex.Generex;
import regex.RegexValidator;
public class TestGenerator {
    public static String generateName() {
        return new Generex(RegexValidator.NAME_REGEX).random();
    }

    public static String generateMessage(int messageLength) {
        return new Generex("[A-Za-z]{"+messageLength+"}").random();
    }
}
