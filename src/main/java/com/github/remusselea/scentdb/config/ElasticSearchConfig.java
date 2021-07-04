package com.github.remusselea.scentdb.config;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Profile("postgres")
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.github.remusselea.scentdb.model.document")
@ComponentScan(basePackages = { "com.github.remusselea.scentdb.service" })
public class ElasticSearchConfig {

  @Value("${spring.elasticsearch.rest.uris}")
  private String elasticsearchUri;

  @Bean
  public RestHighLevelClient client() {
    ClientConfiguration clientConfiguration
        = ClientConfiguration.builder()
        .connectedTo(elasticsearchUri)
        .build();

    log.debug("Connecting to elastic URI: " + elasticsearchUri);
    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchOperations elasticsearchTemplate() {
    return new ElasticsearchRestTemplate(client());
  }
}