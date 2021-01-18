package com.test.springboot.rest.example.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.test.springboot.rest.example.account.repository", "com.test.springboot.rest.example.transaction.repository"})
@EntityScan(basePackages = {"com.test.springboot.rest.example.account.persistent", "com.test.springboot.rest.example.transaction.persistent"})
public class JpaConfig {
}
