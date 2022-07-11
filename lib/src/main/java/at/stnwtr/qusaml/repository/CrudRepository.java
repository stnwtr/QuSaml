package at.stnwtr.qusaml.repository;

import at.stnwtr.qusaml.factory.CrudQueryFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CrudRepository<T, ID> {
    CrudQueryFactory<T, ID> queryFactory();

    default int create(T t) {
        return queryFactory().createQuery(t).execute();
    }

    default Optional<T> readOne(ID id) {
        return queryFactory().readOneQuery(id).execute();
    }

    default List<T> readAll() {
        return queryFactory().readAllQuery().execute();
    }

    default int update(ID id, T t) {
        return queryFactory().updateQuery(id, t).execute();
    }

    default int delete(ID id) {
        return queryFactory().deleteQuery(id).execute();
    }

    default CompletableFuture<Integer> createAsync(T t) {
        return CompletableFuture.supplyAsync(() -> create(t));
    }

    default CompletableFuture<Optional<T>> readOneAsync(ID id) {
        return CompletableFuture.supplyAsync(() -> readOne(id));
    }

    default CompletableFuture<List<T>> readAllAsync() {
        return CompletableFuture.supplyAsync(this::readAll);
    }

    default CompletableFuture<Integer> updateAsync(ID id, T t) {
        return CompletableFuture.supplyAsync(() -> update(id, t));
    }

    default CompletableFuture<Integer> deleteAsync(ID id) {
        return CompletableFuture.supplyAsync(() -> delete(id));
    }
}
