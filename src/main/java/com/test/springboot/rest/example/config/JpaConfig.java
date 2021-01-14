package com.test.springboot.rest.example.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.test.springboot.rest.example.account.repository", "com.test.springboot.rest.example.transaction.repository"})
@EntityScan(basePackages = {"com.test.springboot.rest.example.account.persistent", "com.test.springboot.rest.example.transaction.persistent"})
public class JpaConfig {

}
