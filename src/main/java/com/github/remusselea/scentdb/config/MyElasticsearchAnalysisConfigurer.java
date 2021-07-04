package com.github.remusselea.scentdb.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("postgres")
@Configuration
public class MyElasticsearchAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

  @Override
  public void configure(ElasticsearchAnalysisConfigurationContext context) {
    context.analyzer("edge_ngram_analyzer").custom()
        .tokenizer("edge_ngram_tokenizer")
        .tokenFilters("lowercase", "custom_ascii_folding", "english_stop", "english_possessive_stemmer");

    context.tokenFilter("english_stop")
        .type("stop")
        .param("stopwords", "_english_");

//    context.tokenFilter("english_stemmer")
//        .type("stemmer")
//        .param("language", "english");

    context.tokenFilter("english_possessive_stemmer")
        .type("stemmer")
        .param("language", "possessive_english");

    context.tokenFilter("custom_ascii_folding")
        .type("asciifolding")
        .param("preserve_original", "true");

    context.tokenizer("edge_ngram_tokenizer")
        .type("edge_ngram")
        .param("min_gram", 2)
        .param("max_gram", 20)
        .param("token_chars", new String[]{"letter"});

    context.analyzer("edge_ngram_search_analyzer").custom()
        .tokenizer("standard")
        .tokenFilters("lowercase");
  }


}
