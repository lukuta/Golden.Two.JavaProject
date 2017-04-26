package com.goldentwo.jmx;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class MyManagedSettings {

    private int maxThreads = -1;

    private int maxPoolSize = -1;

    public int getMaxThreads() {
        return maxThreads;
    }

    @ManagedAttribute
    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    @ManagedAttribute
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
}
