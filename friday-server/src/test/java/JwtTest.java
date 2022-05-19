import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.laohui.api.bean.KafkaMessage;
import net.laohui.util.JWTUtil;
import org.apache.shiro.session.ProxiedSession;
import org.apache.shiro.session.Session;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JwtTest {
    @Test
    public void jwt() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", "zhaohy");
        String token = JWTUtil.createToken(data);
        System.out.println("token == " + token);
        Map<String, Object> stringStringMap = JWTUtil.verifyToken(token);
        assert stringStringMap != null;
        stringStringMap.forEach((k, v) -> System.out.println(k + " == " + v));

        //新建定时任务
        Runnable runnable = new Runnable() {
            //run方法中是定时执行的操作
            public void run() {
                System.out.println(new Date());
                Map<String, Object> stringStringMap1 = JWTUtil.verifyToken(token);
                assert stringStringMap1 != null;
                stringStringMap1.forEach((k, v) -> System.out.println(k + " == " + v));
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        /*
         * 参数一:command：执行线程
         * 参数二:initialDelay：初始化延时
         * 参数三:period：两次开始执行最小间隔时间
         * 参数四:unit：计时单位
         */
        service.scheduleAtFixedRate(runnable, 0, 4, TimeUnit.SECONDS);
        Session session = new ProxiedSession(null);
        System.in.read();
    }

    @Test
    public void checkLogin () {
        int action = new Random().nextInt(6);
        System.out.println(action);
        if (action % 2 == 1) {
            System.out.println("检测用户是否登录");
        } else {
            System.out.println("不检测");
        }
    }



    @Test
    public  void sleepSort() {
        int[] nums = {1666, 36, 3, 4, 5, 6111, 7333, 8, 9, 10};
        for (int i = 0; i < nums.length; i++) {
            final int index = nums[i];
            new Thread(() -> {
                try {
                    Thread.sleep(index);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(index);
            }).start();
        }
    }

    @Test
    public void test() {
        Map<String, Object> data = new Hashtable<>();
        data.put("userId", "zhaohy");
        System.out.println(data);
    }

    @Test
    public void objectMapperTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setValue("hello");
        kafkaMessage.setTopic("test");
        String s = "";
        try {
            s = objectMapper.writeValueAsString(kafkaMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
    }

    @Test
    public void vectorTest() {
        long l = System.currentTimeMillis();
//        int[] nums = {233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10,233, 12, 555, 4, 11, 7889, 7, 8, 9, 10};
        int[] nums = {2,424,546,7,8,68,1};
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(nums));
        System.out.println(System.currentTimeMillis() - l);
    }
}
