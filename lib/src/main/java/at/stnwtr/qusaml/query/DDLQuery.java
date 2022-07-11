package at.stnwtr.qusaml.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DDLQuery extends Query<Integer> {
    @Override
    default Integer execute() {
        try (
                Connection connection = dataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query())
        ) {
            return statement.executeUpdate();
        } catch (SQLException e) {
            return exceptionHandler().apply(e);
        }
    }
}
