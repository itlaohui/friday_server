package interfaces;

@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
