import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.laohui.SsoApplication;
import net.laohui.mapper.UserMapper;
import net.laohui.pojo.User;
import net.laohui.util.RedisUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SsoApplication.class)
@MapperScan("net.laohui.mapper")
public class SsoTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void testMapper() {
//        redisUtils.set("name", "绳俊辉");
//        System.out.println(redisUtils.get("name"));
//        boolean b = userMapper.hasUser("root");
//        System.out.println(b);
    }

    @Test
    public void selectUserList() {
        User user = new User();
        user.setUser_id(1);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(user);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectUserById() {
        String userId = "1";
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        User user = userMapper.selectOne(queryWrapper);
        queryWrapper.or();
        System.out.println(user);
    }

    @Test
    public void RedisTest() {
        redisUtils.hset("UserInfo", "userId", 1);

    }

    @Test
    public void randomUUID() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid.toString());
    }

    public static void main(String[] args) {
//        Map<String, Object> stringObjectMap = ClassToMap.asMap(new User());
//        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
        ttt("shengjunhui", "password", "email");
    }

    public static void ttt(String name, String password, String email) {
        System.out.println(name + ":" + password + ":" + email);
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        // 冒泡排序
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
    }

}
