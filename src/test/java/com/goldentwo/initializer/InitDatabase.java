package com.goldentwo.initializer;

import com.goldentwo.model.Match;
import com.goldentwo.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@PropertySource("classpath:application-test.properties")
public class InitDatabase {

    @Value("${test.db.init.matches.size}")
    private int matchesSize;

    @Autowired
    MatchRepository matchRepository;

    @PostConstruct
    public void initDB(){
        for (long i = 1; i <= matchesSize; i++) {
            Match match = Match.builder().id(i).build();
            matchRepository.saveAndFlush(match);
        }
    }
}
