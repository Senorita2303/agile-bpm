package com.dstz.springboot.autoconfigure.web.aspect;

import com.dstz.base.web.controller.aspect.AbControllerAspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Controller aspect configuration
 *
 */
@ConditionalOnClass({HttpServletRequest.class, AbControllerAspect.class})
@Configuration
public class AbControllerAspectConfiguration {

    @Bean
    public Advisor abControllerAdvisor() {
        AbControllerAspect abControllerAspect = new AbControllerAspect();
        Pointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                return (targetClass.isAnnotationPresent(RestController.class) || method.isAnnotationPresent(ResponseBody.class)) && AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class) != null;
            }
        };
        return new DefaultPointcutAdvisor(pointcut, abControllerAspect);
    }

}
