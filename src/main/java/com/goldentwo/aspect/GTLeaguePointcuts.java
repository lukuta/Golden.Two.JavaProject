package com.goldentwo.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class GTLeaguePointcuts {

    @Pointcut("@annotation(Monitored)")
    public void monitored() {}
}
