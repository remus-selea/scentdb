package com.github.remusselea.scentdb.service;


import com.github.remusselea.scentdb.dto.mapper.PerfumerMapper;
import com.github.remusselea.scentdb.dto.model.perfumer.PerfumerDto;
import com.github.remusselea.scentdb.dto.request.PerfumerModel;
import com.github.remusselea.scentdb.model.entity.Company;
import com.github.remusselea.scentdb.model.entity.Perfume;
import com.github.remusselea.scentdb.model.entity.Perfumer;
import com.github.remusselea.scentdb.repo.CompanyRepository;
import com.github.remusselea.scentdb.repo.PerfumeRepository;
import com.github.remusselea.scentdb.repo.PerfumerRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.backend.elasticsearch.search.query.ElasticsearchSearchQuery;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.BooleanPredicateClausesStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.CompositeSortComponentsStep;
import org.hibernate.search.engine.search.sort.dsl.FieldSortOptionsStep;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class PerfumerService {

  private final PerfumerRepository perfumerRepository;

  private final CompanyRepository companyRepository;

  private final PerfumeRepository perfumeRepository;

  private final PerfumerMapper perfumerMapper;

  private final EntityManager entityManager;

  private final ImageService imageService;

  @Value("${perfumers.images.dir:${user.home}}")
  public String uploadDir;

  public PerfumerService(PerfumerRepository perfumerRepository,
      CompanyRepository companyRepository,
      PerfumeRepository perfumeRepository,
      PerfumerMapper perfumerMapper, EntityManager entityManager,
      ImageService imageService) {
    this.perfumerRepository = perfumerRepository;
    this.companyRepository = companyRepository;
    this.perfumeRepository = perfumeRepository;
    this.perfumerMapper = perfumerMapper;
    this.entityManager = entityManager;
    this.imageService = imageService;
  }

  @Transactional(readOnly = true)
  public PerfumerDto getPerfumerById(long perfumerId) {
    Perfumer perfumer = perfumerRepository.findById(perfumerId).orElseThrow(
        () -> new EntityNotFoundException("Could not find perfumer with id: " + perfumerId));

    return perfumerMapper.perfumerToPerfumerDto(perfumer);
  }

  @Transactional
  public PerfumerDto savePerfumer(PerfumerModel perfumerModel, MultipartFile multipartImageFile) {
    Perfumer perfumerToSave = perfumerMapper.perfumerModelToPerfumer(perfumerModel);

    Optional<Company> optionalCompany = companyRepository.findById(perfumerModel.getCompanyId());
    optionalCompany.ifPresent(perfumerToSave::setCompany);

    Set<Perfume> perfumeList = new HashSet<>(perfumerModel.getPerfumeIdList().size());
    for (Long perfumeId : perfumerModel.getPerfumeIdList()) {
      Optional<Perfume> optionalPerfume = perfumeRepository.findById(perfumeId);
      optionalPerfume.ifPresent(perfumeList::add);
    }
    perfumerToSave.setPerfumes(perfumeList);

    String imageFileName = imageService.storeImage(multipartImageFile, uploadDir);
    String perfumerImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/perfumers/")
        .path(imageFileName)
        .toUriString();
    perfumerToSave.setImagePath(perfumerImagePath);

    Perfumer savedPerfumer = perfumerRepository.save(perfumerToSave);

    return perfumerMapper.perfumerToPerfumerDto(savedPerfumer);
  }

  public List<PerfumerDto> getAllPerfumers() {
    List<Perfumer> perfumerList = perfumerRepository.findAll();
    List<PerfumerDto> perfumerDtoList = new ArrayList<>(perfumerList.size());

    for (Perfumer perfumer : perfumerList) {
      PerfumerDto perfumerDto = perfumerMapper.perfumerToPerfumerDto(perfumer);
      perfumerDtoList.add(perfumerDto);
    }

    return perfumerDtoList;
  }


  public Page<PerfumerDto> search(Pageable pageable, String query) {
    Page<PerfumerDto> perfumerDtoPage;

    if ((query == null || query.isBlank())) {
      perfumerDtoPage = searchMatchAll(pageable);
    } else {
      perfumerDtoPage = searchTerms(pageable, query);
    }

    return perfumerDtoPage;
  }


  @Transactional(readOnly = true)
  public Page<PerfumerDto> searchMatchAll(Pageable pageable) {
    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Perfumer> scope = searchSession.scope(Perfumer.class);
    SearchSort searchSort = getSearchSort(pageable, scope);

    ElasticsearchSearchQuery<Perfumer> searchQuery = searchSession.search(Perfumer.class)
        .extension(ElasticsearchExtension.get())
        .where(SearchPredicateFactory::matchAll)
        .sort(searchSort)
        .toQuery();

    SearchResult<Perfumer> result = searchQuery
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    List<Perfumer> perfumerList = result.hits();
    List<PerfumerDto> perfumerDtoList = new ArrayList<>(perfumerList.size());

    for (Perfumer perfumer : perfumerList) {
      PerfumerDto perfumerDto = perfumerMapper.perfumerToPerfumerDto(perfumer);
      perfumerDtoList.add(perfumerDto);
    }

    return new PageImpl<>(perfumerDtoList, pageable, result.total().hitCount());
  }

  @Transactional(readOnly = true)
  public Page<PerfumerDto> searchTerms(Pageable pageable, String query) {
    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Perfumer> scope = searchSession.scope(Perfumer.class);

    SearchPredicate boolPredicates = formSearchPredicates(query, scope);
    SearchSort searchSort = getSearchSort(pageable, scope);

    SearchResult<Perfumer> result = searchSession
        .search(scope)
        .where(boolPredicates)
        .sort(searchSort)
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    List<Perfumer> perfumerList = result.hits();
    List<PerfumerDto> perfumerDtoList = new ArrayList<>(perfumerList.size());

    for (Perfumer perfumer : perfumerList) {
      PerfumerDto perfumerDto = perfumerMapper.perfumerToPerfumerDto(perfumer);
      perfumerDtoList.add(perfumerDto);
    }

    return new PageImpl<>(perfumerDtoList, pageable, result.total().hitCount());
  }

  private SearchSort getSearchSort(Pageable pageable, SearchScope<Perfumer> scope) {
    List<Order> sortOrderList = pageable.getSort().stream().collect(Collectors.toList());
    SearchSortFactory searchSortFactory = scope.sort();
    CompositeSortComponentsStep<?> compositeSortComponentsStep = searchSortFactory.composite();

    for (Order sortOrder : sortOrderList) {
      String fieldToSortBy = sortOrder.getProperty();

      if (fieldToSortBy.equals("bestMatch")) {
        compositeSortComponentsStep.add(searchSortFactory.score());
      } else {
        SortOrder order = SortOrder.DESC;

        if (sortOrder.isAscending()) {
          order = SortOrder.ASC;
        }

        FieldSortOptionsStep<?, ? extends SearchPredicateFactory> fieldSortOptionsStep = searchSortFactory.field(
            fieldToSortBy).order(order);
        compositeSortComponentsStep.add(fieldSortOptionsStep.toSort());
      }
    }

    return compositeSortComponentsStep.toSort();
  }

  private SearchPredicate formSearchPredicates(String query, SearchScope<Perfumer> searchScope) {
    SearchPredicateFactory factory = searchScope.predicate();

    BooleanPredicateClausesStep<?> booleanJunction = factory.bool();
    prepareNamePredicate(booleanJunction, factory, query);

    return booleanJunction.toPredicate();
  }


  private void prepareNamePredicate(BooleanPredicateClausesStep<?> booleanJunction,
      SearchPredicateFactory factory, String query) {
    if (StringUtils.isBlank(query)) {
      return;
    }
    booleanJunction.must(factory.match().field("name").matching(query).fuzzy(2).toPredicate());
  }

  public void removePerfumerById(Long perfumerId) {
    perfumerRepository.deleteById(perfumerId);
  }

}
