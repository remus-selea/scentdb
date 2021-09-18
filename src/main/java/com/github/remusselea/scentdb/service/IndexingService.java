package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.model.entity.Company;
import com.github.remusselea.scentdb.model.entity.Note;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IndexingService {

  private final EntityManager entityManager;

  public IndexingService(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional
  public void initiateIndexing() {
    log.info("Initiating indexing...");
    try {
      SearchSession searchSession = Search.session(entityManager);
      MassIndexer indexer = searchSession.massIndexer(Perfume.class, Company.class, Note.class, Perfumer.class).threadsToLoadObjects(7);

      indexer.startAndWait();
    } catch (InterruptedException interruptedException) {
      log.error(interruptedException.getMessage());
      interruptedException.printStackTrace();
    }

    log.info("All entities indexed");
  }
}