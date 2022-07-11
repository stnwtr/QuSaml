package at.stnwtr.qusaml.query;

import at.stnwtr.qusaml.function.ThrowingConsumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DMLQuery extends Query<Integer> {
    ThrowingConsumer<PreparedStatement, SQLException> statementEditor();

    @Override
    default Integer execute() {
        try (
                Connection connection = dataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query())
        ) {
            statementEditor().accept(statement);
            return statement.executeUpdate();
        } catch (SQLException e) {
            return exceptionHandler().apply(e);
        }
    }
}
