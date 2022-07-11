package at.stnwtr.qusaml.query;

import at.stnwtr.qusaml.function.ThrowingConsumer;
import at.stnwtr.qusaml.function.ThrowingFunction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DQLQuery<T> extends Query<T> {
    ThrowingConsumer<PreparedStatement, SQLException> statementEditor();

    ThrowingFunction<ResultSet, T, SQLException> resultMapper();

    @Override
    default T execute() {
        try (
                Connection connection = dataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query())
        ) {
            statementEditor().accept(statement);
            return resultMapper().apply(statement.executeQuery());
        } catch (SQLException e) {
            return exceptionHandler().apply(e);
        }
    }
}
