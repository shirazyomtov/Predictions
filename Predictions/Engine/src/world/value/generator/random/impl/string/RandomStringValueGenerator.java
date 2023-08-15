package world.value.generator.random.impl.string;

import world.value.generator.random.api.AbstractRandomValueGenerator;

import java.io.Serializable;
import java.util.Random;

public class RandomStringValueGenerator extends AbstractRandomValueGenerator<String> implements Serializable {
    @Override
    public String generateValue() {
        String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?,_-(). ";
        Random random = new Random();
        int length = random.nextInt(50) + 1;
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(validCharacters.length());
            char randomChar = validCharacters.charAt(randomIndex);
            randomString.append(randomChar);
        }
        return randomString.toString();

    }
}
