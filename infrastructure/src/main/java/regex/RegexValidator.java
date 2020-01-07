package regex;

import java.util.regex.Pattern;

public class RegexValidator {

    public final static String NAME_REGEX = "[A-Za-z]\\w{2,19}";
    public final static String TOKEN_REGEX = "[A-HJ-Za-km-z]{32}";
    public final static String ES_INDEX_NAME_REGEX = "logz-[a-z]{32}";

    public static boolean isNameValid(String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    public static boolean isTokenValid(String token) {
        return Pattern.matches(TOKEN_REGEX, token);
    }
}
