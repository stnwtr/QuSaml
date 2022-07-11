package at.stnwtr.qusaml.factory;

import at.stnwtr.qusaml.entity.User;
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

public class UserQueryFactory implements CrudQueryFactory<User, Integer> {
    private final DataSource dataSource;

    public UserQueryFactory(DataSource dataSource) {
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
    public DMLQuery createQuery(User user) {
        return new CreateQuery(user);
    }

    @Override
    public DQLQuery<Optional<User>> readOneQuery(Integer integer) {
        return new ReadOneQuery(integer);
    }

    @Override
    public DQLQuery<List<User>> readAllQuery() {
        return new ReadAllQuery();
    }

    @Override
    public DMLQuery updateQuery(Integer integer, User user) {
        return new UpdateQuery(integer, user);
    }

    @Override
    public DMLQuery deleteQuery(Integer integer) {
        return new DeleteQuery(integer);
    }

    private class CreateTableQuery implements DDLQuery {
        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    CREATE TABLE IF NOT EXISTS "users"
                    (
                        "first"      TEXT        NOT NULL,
                        "last"       TEXT        NOT NULL,
                        "username"   TEXT        NOT NULL,
                        "password"   TEXT        NOT NULL,
                        "id"         SERIAL      NOT NULL,
                        "created_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        "updated_at" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT "users_pk" PRIMARY KEY ("id"),
                        CONSTRAINT "username_uq" UNIQUE ("username")
                    );""";
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
                    DROP TABLE IF EXISTS "users";""";
        }
    }

    private class CreateQuery implements DMLQuery {
        private final User user;

        public CreateQuery(User user) {
            this.user = user;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
                preparedStatement.setString(1, user.first());
                preparedStatement.setString(2, user.last());
                preparedStatement.setString(3, user.username());
                preparedStatement.setString(4, user.password());
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    INSERT INTO "users" ("first", "last", "username", "password")
                    VALUES (?, ?, ?, ?);""";
        }
    }

    private class ReadOneQuery implements DQLQuery<Optional<User>> {
        private final int id;

        public ReadOneQuery(int id) {
            this.id = id;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> preparedStatement.setInt(1, id);
        }

        @Override
        public ThrowingFunction<ResultSet, Optional<User>, SQLException> resultMapper() {
            return resultSet -> {
                if (resultSet.next()) {
                    return Optional.of(User.fromDatabase(resultSet));
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
                    SELECT "first", "last", "username", "password", "id", "created_at", "updated_at"
                    FROM "users"
                    WHERE "id" = ?;""";
        }

        @Override
        public Function<SQLException, Optional<User>> exceptionHandler() {
            return sqlException -> {
                System.err.println(sqlException.getMessage());
                return Optional.empty();
            };
        }
    }

    private class ReadAllQuery implements DQLQuery<List<User>> {
        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
            };
        }

        @Override
        public ThrowingFunction<ResultSet, List<User>, SQLException> resultMapper() {
            return resultSet -> {
                List<User> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(User.fromDatabase(resultSet));
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
                    SELECT "first", "last", "username", "password", "id", "created_at", "updated_at"
                    FROM "users";""";
        }

        @Override
        public Function<SQLException, List<User>> exceptionHandler() {
            return sqlException -> {
                System.err.println(sqlException.getMessage());
                return Collections.emptyList();
            };
        }
    }

    private class UpdateQuery implements DMLQuery {
        private final int id;
        private final User user;

        public UpdateQuery(int id, User user) {
            this.id = id;
            this.user = user;
        }

        @Override
        public ThrowingConsumer<PreparedStatement, SQLException> statementEditor() {
            return preparedStatement -> {
                preparedStatement.setString(1, user.first());
                preparedStatement.setString(2, user.last());
                preparedStatement.setString(3, user.username());
                preparedStatement.setString(4, user.password());
                preparedStatement.setInt(5, id);
            };
        }

        @Override
        public DataSource dataSource() {
            return dataSource;
        }

        @Override
        public String query() {
            return """
                    UPDATE "users"
                    SET "first"      = ?,
                        "last"       = ?,
                        "username"   = ?,
                        "password"   = ?,
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
                    FROM "users"
                    WHERE "id" = ?;""";
        }
    }
}
