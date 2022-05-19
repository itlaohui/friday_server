import interfaces.CallBack;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class shiroTest {
    @Test
    public void mathRound() {
        long round = Math.round(-1.5);
        System.out.println(round);
    }

    @Test
    public void stringExtend() {
        String str = "张123";
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        System.out.println(sb);
        System.out.println(sb.substring(0, 1));
    }

    @Test
    public void fileExtend() {
        String fileName = "D:\\Downloads\\test.txt";
        boolean exists = Files.exists(Paths.get(fileName));
        System.out.println(exists);
    }

    @Test
    public void CollectionExtend() {
        String str = "123";
        String s = new String("123");
        System.out.println(s.hashCode());
        System.out.println(str.hashCode());
    }

    @Test
    public void IntergetTest() {
        Integer i = 123;
        System.out.println(i.hashCode());
    }

    private void test(CallBack callBack) {
        System.out.println("test");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        callBack.execute("123");
    }


    @Test
    public void callBackTest() {
        this.test((CallBack<String>) s -> {
            System.out.println("execute" + s);
            return null;
        });
        System.out.println("end");
    }
}

class t {

}
