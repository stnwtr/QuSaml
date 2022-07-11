package at.stnwtr.qusaml;

import at.stnwtr.qusaml.entity.Post;
import at.stnwtr.qusaml.entity.User;
import com.github.javafaker.Faker;

public class FakeUtil {
    public static User randomUser() {
        Faker faker = new Faker();
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.name().username(),
                faker.lorem().characters(8, 16),
                null, null, null
        );
    }

    public static Post randomPost(int id) {
        Faker faker = new Faker();
        return new Post(
                faker.animal().name().toUpperCase() + " " + faker.company().name(),
                faker.lorem().characters(8, 256),
                id,
                null, null, null
        );
    }

    public static Post randomPost(User user) {
        return randomPost(user.id());
    }
}
