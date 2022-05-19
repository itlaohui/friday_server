package testAbstract;

public interface interfaceMethod {
    default void method() {
        System.out.println("interfaceMethod");
    }
}
