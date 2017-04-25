package com.goldentwo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TracingAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Before("com.goldentwo.aspect.pointcut.GTLeaguePointcuts.anyControllerMethod()")
    public void trace(JoinPoint joinPoint) {
        logger.info("Method called: {}", joinPoint.getSignature());
    }
}
