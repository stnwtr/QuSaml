package at.stnwtr.qusaml.factory;

import at.stnwtr.qusaml.query.DDLQuery;
import at.stnwtr.qusaml.query.DMLQuery;
import at.stnwtr.qusaml.query.DQLQuery;

import java.util.List;
import java.util.Optional;

public interface CrudQueryFactory<T, ID> {
    DDLQuery createTableQuery();

    DDLQuery dropTableQuery();

    DMLQuery createQuery(T t);

    DQLQuery<Optional<T>> readOneQuery(ID id);

    DQLQuery<List<T>> readAllQuery();

    DMLQuery updateQuery(ID id, T t);

    DMLQuery deleteQuery(ID id);
}
