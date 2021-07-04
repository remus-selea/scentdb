package com.github.remusselea.scentdb;

import com.github.remusselea.scentdb.service.IndexingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@SpringBootApplication
@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
public class ScentdbApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(ScentdbApplication.class, args);

    context.getBean(IndexingService.class).initiateIndexing();
  }

}
