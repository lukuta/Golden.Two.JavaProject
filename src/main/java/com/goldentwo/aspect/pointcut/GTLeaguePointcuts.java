package com.goldentwo.aspect.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class GTLeaguePointcuts {

    @Pointcut("@annotation(com.goldentwo.aspect.annotation.Monitored)")
    public void monitored() {
    }

    @Pointcut("execution(* *..controller..*(..))")
    public void anyControllerMethod() {
    }

}
