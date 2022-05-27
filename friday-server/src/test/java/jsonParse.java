
import com.google.common.base.Joiner;

import java.io.IOException;
import java.util.ArrayList;

public class jsonParse {
    public static void main(String[] args) throws IOException {
        ArrayList<String> stringBuilder = new ArrayList<>();
        stringBuilder.add("123");
        stringBuilder.add("213");
        stringBuilder.add("123");
        System.out.println(Joiner.on("").join(stringBuilder));


    }
}
