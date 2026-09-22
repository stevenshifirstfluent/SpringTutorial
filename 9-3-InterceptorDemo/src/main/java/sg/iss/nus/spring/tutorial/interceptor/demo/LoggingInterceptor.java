package sg.iss.nus.spring.tutorial.interceptor.demo;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    private static final String START_TIME = "LOG_START_NANO";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.nanoTime());

        log.info("LoggingInterceptor preHandle()");
        log.info("Request URL: {} {}", request.getMethod(), request.getRequestURL());

        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            for (String v : request.getParameterValues(name)) {
                log.info("Request param: {} = {}", name, v);
            }
        }
        return true; // continue to controller
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, org.springframework.web.servlet.ModelAndView modelAndView) {
        log.info("LoggingInterceptor postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long start = (Long) request.getAttribute(START_TIME);
        if (start != null) {
            long tookMs = (System.nanoTime() - start) / 1_000_000;
            log.info("LoggingInterceptor afterCompletion() - time taken: {} ms", tookMs);
        } else {
            log.info("LoggingInterceptor afterCompletion()");
        }
    }
}