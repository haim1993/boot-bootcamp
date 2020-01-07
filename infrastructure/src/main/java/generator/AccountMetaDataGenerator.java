package generator;

import com.mifmif.common.regex.Generex;
import regex.RegexValidator;

public class AccountMetaDataGenerator {
    public static String generateToken() {
        return new Generex(RegexValidator.TOKEN_REGEX).random();
    }

    public static String generateElasticsearchIndexName() {
        return new Generex(RegexValidator.ES_INDEX_NAME_REGEX).random();
    }
}
