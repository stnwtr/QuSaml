package at.stnwtr.qusaml.function;

public interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
