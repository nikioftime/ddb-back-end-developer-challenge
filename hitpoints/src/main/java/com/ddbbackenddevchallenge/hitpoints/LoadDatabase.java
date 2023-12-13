package com.ddbbackenddevchallenge.hitpoints;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ddbbackenddevchallenge.hitpoints.database.DefensesRepository;
import com.ddbbackenddevchallenge.hitpoints.database.HealthRepository;
import com.ddbbackenddevchallenge.hitpoints.model.data.Defenses;
import com.ddbbackenddevchallenge.hitpoints.model.data.Health;
import com.ddbbackenddevchallenge.hitpoints.model.data.Player;
import com.google.gson.Gson;


@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
  private static final Gson gson = new Gson();
  private final String brivFile = "src/main/resources/static/briv.json";

  @Bean
  CommandLineRunner initDatabase(HealthRepository healthRepository, DefensesRepository defensesRepository) throws FileNotFoundException {
    final FileReader brivReader = new FileReader(brivFile);
    final Player briv = gson.fromJson(brivReader, Player.class);
    

    return args -> {
        log.info("Loading Briv's health data: " + healthRepository.save(new Health(briv.getName(), briv.getHitPoints())));
        log.info("Loading Briv's defense data: " + defensesRepository.save(new Defenses(briv.getName(), briv.getDefenses())));
    };
  }
}
