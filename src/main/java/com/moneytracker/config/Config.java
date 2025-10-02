package com.moneytracker.config;

import com.moneytracker.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {


  @Bean
  public Transaction transaction() {
    return new Transaction();
  }
}

