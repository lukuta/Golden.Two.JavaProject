package com.goldentwo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MonitoringAspect {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Around("com.goldentwo.aspect.pointcut.GTLeaguePointcuts.monitored()")
    public Object monitor(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        Object result = countProceedingTime(proceedingJoinPoint, stopWatch);
        logger.info("Method '{}' took {} ms", proceedingJoinPoint.getSignature(), stopWatch.getTotalTimeMillis());

        return result;
    }

    private Object countProceedingTime(ProceedingJoinPoint proceedingJoinPoint, StopWatch stopWatch) throws Throwable {
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        return result;
    }
}
