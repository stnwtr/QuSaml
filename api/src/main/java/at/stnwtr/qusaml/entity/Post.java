package at.stnwtr.qusaml.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public record Post(
        String title,
        String content,
        int author,
        Integer id,
        Instant createdAt,
        Instant updatedAt
) implements Entity<Integer> {
    public static Post fromDatabase(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getInt("author"),
                resultSet.getInt("id"),
                resultSet.getTimestamp("created_at").toInstant(),
                resultSet.getTimestamp("created_at").toInstant()
        );
    }
}
