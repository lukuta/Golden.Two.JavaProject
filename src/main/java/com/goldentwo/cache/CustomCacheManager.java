package com.goldentwo.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource(objectName = "custom.cache:type=CustomCacheManager")
public class CustomCacheManager {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ManagedOperation(description = "Clears all players cache. Use when refresh necessary")
    @CacheEvict(CacheConstants.PLAYERS_CACHE)
    public void clearPlayersCache(){
        logger.info("Clearing players cache...");
    }
}
