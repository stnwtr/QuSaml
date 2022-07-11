package at.stnwtr.qusaml.factory;

import at.stnwtr.qusaml.entity.Post;
import at.stnwtr.qusaml.function.ThrowingConsumer;
import at.stnwtr.qusaml.function.ThrowingFunction;
import at.stnwtr.qusaml.query.DDLQuery;
import at.stnwtr.qusaml.query.DMLQuery;
import at.stnwtr.qusaml.query.DQLQuery;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PostQueryFactory implements CrudQueryFactory<Post, Integer> {
    private final DataSource dataSource;

    public PostQueryFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DDLQuery createTableQuery() {
        return new CreateTableQuery();
    }

    @Override
    public DDLQuery dropTableQuery() {
        return new DropTableQuery();
    }

    @Override
    public DMLQuery createQuery(Post post) {
        return new CreateQuery(post);
    }

    @Override
    public DQLQuery<Optional<Post>> readOneQuery(Integer integer) {
        return new ReadOneQuery(integer);
    }

    @Override
    public DQLQuery<List<Post>> readAllQuery() {
        return null;
    }

    @Override
    public DMLQuery updateQuery(Integer integer, Post post) {
        return null;
    }

    @Override
    public DMLQuery deleteQuery(Integer integer) {
        return null;
    }

    private class CreateTableQuery implements DDLQuery {
        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    CREATE TABLE IF NOT EXISTS posts
                    (
                        "title"      TEXT        NOT NULL,
                        "content"    TEXT        NOT NULL,
                        "author"     INT         NOT NULL,
                        "id"         SERIAL      NOT NULL,
                        "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT "posts_pk" PRIMARY KEY ("id"),
                        CONSTRAINT "posts_users_fk" FOREIGN KEY ("author") REFERENCES "users" ("id")
                    )""";
        }
    }

    private class DropTableQuery implements DDLQuery {
        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    DROP TABLE IF EXISTS "posts";""";
        }
    }

    private class CreateQuery implements DMLQuery {
        private final Post post;

        public CreateQuery(Post post) {
            this.post = post;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
                preparedStatement.setString(1, post.title());
                preparedStatement.setString(2, post.content());
                preparedStatement.setInt(3, post.author());
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    INSERT INTO "posts" ("title", "content", "author")
                    VALUES (?, ?, ?);""";
        }
    }

    private class ReadOneQuery implements DQLQuery<Optional<Post>> {
        private final int id;

        public ReadOneQuery(int id) {
            this.id = id;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> preparedStatement.setInt(1, id);
        }

        @Override
        public ThrowingFunction<ResultSet, Optional<Post>, SQLException> resultMapper() {
            return resultSet -> {
                if (resultSet.next()) {
                    return Optional.of(Post.fromDatabase(resultSet));
                }

                return Optional.empty();
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    SELECT "title", "content", "author", "id", "created_at", "updated_at"
                    FROM "posts"
                    WHERE "id" = ?;""";
        }

        @Override
        public Function<SQLException, Optional<Post>> exceptionHandler() {
            return sqlException -> {
                System.err.println(sqlException.getMessage());
                return Optional.empty();
            };
        }
    }

    private class ReadAllQuery implements DQLQuery<List<Post>> {
        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
            };
        }

        @Override
        public ThrowingFunction<ResultSet, List<Post>, SQLException> resultMapper() {
            return resultSet -> {
                List<Post> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(Post.fromDatabase(resultSet));
                }
                return list;
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    SELECT "title", "content", "author", "id", "created_at", "updated_at"
                    FROM "posts";""";
        }

        @Override
        public Function<SQLException, List<Post>> exceptionHandler() {
            return sqlException -> {
                System.err.println(sqlException.getMessage());
                return Collections.emptyList();
            };
        }
    }

    private class UpdateQuery implements DMLQuery {
        private final int id;
        private final Post post;

        public UpdateQuery(int id, Post post) {
            this.id = id;
            this.post = post;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
                preparedStatement.setString(1, post.title());
                preparedStatement.setString(2, post.content());
                preparedStatement.setInt(3, post.author());
                preparedStatement.setInt(4, id);
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    UPDATE "posts"
                    SET "title"      = ?,
                        "content"    = ?,
                        "author"     = ?,
                        "updated_at" = CURRENT_TIMESTAMP
                    WHERE "id" = ?;""";
        }
    }

    private class DeleteQuery implements DMLQuery {
        private final int id;

        public DeleteQuery(int id) {
            this.id = id;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> preparedStatement.setInt(1, id);
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    DELETE
                    FROM "posts"
                    WHERE "id" = ?;""";
        }
    }
}
