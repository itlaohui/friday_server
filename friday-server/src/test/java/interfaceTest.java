import interfaces.Callable;
import org.junit.Test;

public class interfaceTest implements Callable {
    public Integer eq(String s) {
        return s.length();
    }

    @Test
    public void test() {
        System.err.println("Running interfaceTest");
        System.out.println(this.eq("111"));
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
