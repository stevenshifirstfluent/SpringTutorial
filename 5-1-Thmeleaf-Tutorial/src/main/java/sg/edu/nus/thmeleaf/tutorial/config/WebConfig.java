package sg.edu.nus.thmeleaf.tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@Configuration
public class WebConfig implements WebMvcConfigurer {


    /*
     * Stores the selected Locale in the HTTP session.
     *
     * English is used as the default language.
     */
    @Bean
    public LocaleResolver localeResolver() {

        SessionLocaleResolver resolver =
                new SessionLocaleResolver();

        resolver.setDefaultLocale(
                Locale.ENGLISH
        );

        return resolver;
    }


    /*
     * Allows the URL parameter:
     *
     *     ?lang=en
     *     ?lang=zh
     *
     * to change the current language.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {

        LocaleChangeInterceptor interceptor =
                new LocaleChangeInterceptor();

        interceptor.setParamName("lang");

        return interceptor;
    }


    /*
     * Register the locale interceptor with Spring MVC.
     */
    @Override
    public void addInterceptors(
            InterceptorRegistry registry) {

        registry.addInterceptor(
                localeChangeInterceptor()
        );
    }
}