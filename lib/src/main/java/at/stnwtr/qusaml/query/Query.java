package at.stnwtr.qusaml.query;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.function.Function;

public interface Query<T> {
    DataSource dataSource();

    String query();

    Function<SQLException, T> exceptionHandler();

    T execute();
}
