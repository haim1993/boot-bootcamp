package generator;

import org.junit.Test;

public class GeneratorTest {
    @Test
    public void generateTokenTest() {
        System.out.println(Generator.generateToken());
    }

    @Test
    public void generateEsIndexNameTest() {
        System.out.println(Generator.generateElasticsearchIndexName());
    }

    @Test
    public void generateNameTest() {
        System.out.println(Generator.generateName());
    }

    @Test
    public void generateMessageTest() {
        System.out.println(Generator.generateMessage(10));
    }
}
