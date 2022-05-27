import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import net.laohui.SsoApplication;
import net.laohui.api.bean.domain.Configs;
import net.laohui.api.service.ConfigsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SsoApplication.class)
@EnableDubbo
public class MapperTest {
    @Reference(version = "1.0.0", timeout = 60000)
    ConfigsService configsService;

    @Test
    public void getConfig() {
        String name = "sysConfig";
        Configs config = configsService.getConfig(name);
        System.out.println("=======================================================");
        System.out.println("config: " + name);
        config.getConfig_list().forEach(System.out::println);
        System.out.println("=======================================================");
    }
}
