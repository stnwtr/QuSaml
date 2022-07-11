package at.stnwtr.qusaml.factory;

import at.stnwtr.qusaml.query.DDLQuery;
import at.stnwtr.qusaml.query.DMLQuery;
import at.stnwtr.qusaml.query.DQLQuery;

import java.util.List;
import java.util.Optional;

public interface CrudQueryFactory<T, ID> {
    DDLQuery createTableQuery();

    DDLQuery dropTableQuery();

    DMLQuery create(T t);

    DQLQuery<Optional<T>> readOne(ID id);

    DQLQuery<List<T>> readAll();

    DMLQuery update(ID id, T t);

    DMLQuery delete(ID id);
}
