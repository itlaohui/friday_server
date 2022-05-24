package net.laohui;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import net.laohui.interceptor.RequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class}) // 去掉数据源自动配置
@EnableScheduling
@EnableDubbo
@EnableAspectJAutoProxy
public class SsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class);
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
//                registry.addInterceptor(new AuthInterceptor())
//                        .addPathPatterns("/user/*")
//                        .addPathPatterns("/admin/*")
//                        .excludePathPatterns("/user/login")
//                        .excludePathPatterns("/user/register")
//                        .excludePathPatterns("/user/logout")
//                        .excludePathPatterns("/user/hasUser")
//                        .excludePathPatterns("/user/sendCode")
//                        .excludePathPatterns("/user/getUserInfo")
//                        .excludePathPatterns("/user/hasLogin");
            }

//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        //是否发送Cookie
//                        .allowCredentials(true)
//                        //放行哪些原始域
//                        .allowedOriginPatterns("http://localhost:8080")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("Authorization", "token");
//            }
        };
    }
}
