package at.stnwtr.qusaml.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public record User(
        String first,
        String last,
        String username,
        String password,
        Integer id,
        Instant createdAt,
        Instant updatedAt
) implements Entity<Integer> {
    public static User fromDatabase(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getString("first"),
                resultSet.getString("last"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getInt("id"),
                resultSet.getTimestamp("created_at").toInstant(),
                resultSet.getTimestamp("updated_at").toInstant()
        );
    }
}
