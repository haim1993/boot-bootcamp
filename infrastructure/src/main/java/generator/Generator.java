package generator;

import com.mifmif.common.regex.Generex;
import regex.RegexValidator;

public class Generator {
    public static String generateToken() {
        return new Generex(RegexValidator.TOKEN_REGEX).random();
    }

    public static String generateElasticsearchIndexName() {
        return new Generex(RegexValidator.ES_INDEX_NAME_REGEX).random();
    }

    public static String generateName() {
        return new Generex(RegexValidator.NAME_REGEX).random();
    }

    public static String generateMessage(int messageLength) {
        return new Generex("[A-Za-z]{"+messageLength+"}").random();
    }
}
