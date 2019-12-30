package regex;

import org.junit.Test;
import regex.RegexValidator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class RegexValidatorTest {

    @Test
    public void isNameValidTest() {
        assertTrue(RegexValidator.isNameValid("Aa_152215"));
        assertFalse(RegexValidator.isNameValid("Aa_*52215"));
    }

    @Test
    public void isTokenValidTest() {
        assertTrue(RegexValidator.isTokenValid("YZZXfOLKfTJEMGgKknWaKOpURnvALnRi"));
        assertFalse(RegexValidator.isTokenValid("YZZXfOLKfTJEMGgKklWaKOpURnvALnRI"));
    }

}
