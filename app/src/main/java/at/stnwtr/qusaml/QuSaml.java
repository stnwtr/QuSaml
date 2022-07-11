package at.stnwtr.qusaml;

import at.stnwtr.qusaml.factory.PostQueryFactory;
import at.stnwtr.qusaml.factory.UserQueryFactory;
import at.stnwtr.qusaml.repository.PostRepository;
import at.stnwtr.qusaml.repository.UserRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.concurrent.ThreadLocalRandom;

public class QuSaml {
    private final HikariDataSource hikariDataSource;

    private final UserQueryFactory userQueryFactory;
    private final PostQueryFactory postQueryFactory;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public QuSaml(HikariConfig hikariConfig) {
        this.hikariDataSource = new HikariDataSource(hikariConfig);

        this.userQueryFactory = new UserQueryFactory(hikariDataSource);
        this.postQueryFactory = new PostQueryFactory(hikariDataSource);

        this.userRepository = new UserRepository(userQueryFactory);
        this.postRepository = new PostRepository(postQueryFactory);

        create();
        test();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void create() {
        this.userQueryFactory.createTableQuery().execute();
        this.postQueryFactory.createTableQuery().execute();
    }

    private void drop() {
        this.postQueryFactory.dropTableQuery().execute();
        this.userQueryFactory.dropTableQuery().execute();
    }

    private void test() throws RuntimeException {
        userRepository.delete(ThreadLocalRandom.current().nextInt(4));
        postRepository.delete(ThreadLocalRandom.current().nextInt(16));

        userRepository.create(FakeUtil.randomUser());
        userRepository.create(FakeUtil.randomUser());
        userRepository.create(FakeUtil.randomUser());
        userRepository.create(FakeUtil.randomUser());
        var all = userRepository.readAll();

        for (int i = 0; i < 16; i++) {
            postRepository.create(FakeUtil.randomPost(all.get(ThreadLocalRandom.current().nextInt(all.size()))));
        }
    }

    private void shutdown() {
        hikariDataSource.close();
    }
}
