package sg.iss.nus.spring.tutorial.interceptor.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;
    
    @Autowired
    private TimeExecuteInterceptor timeExecuteInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor).order(1);
        registry.addInterceptor(timeExecuteInterceptor).order(2).addPathPatterns("/account/*", "/common/*", "/time/*");
    }
}
