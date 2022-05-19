package net.laohui;

import org.jetbrains.annotations.NotNull;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@MapperScan("net.laohui.mapper")
@EnableScheduling
@EnableTransactionManagement
public class SsoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsoApplication.class);
    }

    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }

            @Override
            public void addInterceptors(@NotNull InterceptorRegistry registry) {
//                registry.addInterceptor(new ResponseResultHandler()).addPathPatterns("/**");
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
