package sg.iss.nus.spring.tutorial.interceptor.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TimeExecuteInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(TimeExecuteInterceptor.class);
    private static final String START = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long start = System.currentTimeMillis();
        // 1) make available to controller as @RequestAttribute
        request.setAttribute(START, start);
        log.info("TimeExecuteInterceptor preHandle() startTime={}", start);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView mav) {
        Long start = (Long) request.getAttribute(START);
        if (start == null) return;

        long complete = System.currentTimeMillis();
        long executeMs = complete - start;

        log.info("TimeExecuteInterceptor postHandle() completeTime={}, executeTime={} ms", complete, executeMs);

        // 2) send directly to the view model (works for template views)
        if (mav != null) {
            mav.addObject("startTime", start);
            mav.addObject("completeTime", complete);
            mav.addObject("executeTime", executeMs);
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        Long start = (Long) request.getAttribute(START);
        if (start != null) {
        	long complete = System.currentTimeMillis();
        long executeMs = complete - start;
            log.info("TimeExecuteInterceptor afterCompletion() - time taken: {} ms", executeMs);
        } else {
            log.info("TimeExecuteInterceptor afterCompletion()");
        }
    }
}