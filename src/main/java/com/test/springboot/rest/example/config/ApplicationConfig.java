package com.test.springboot.rest.example.config;

import java.time.Clock;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableTransactionManagement
public class ApplicationConfig {

  @Autowired
  private DataSource dataSource;

  @Bean
  public Clock getClock() {
    return Clock.systemUTC();
  }

  @Bean("DBHealth")
  public DataSourceHealthIndicator requiredDataSourceHealthIndicator() {
    return new DataSourceHealthIndicator(dataSource, "SELECT 1 FROM DUAL");
  }
}
