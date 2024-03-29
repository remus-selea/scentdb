package com.github.remusselea.scentdb.service;

import com.github.remusselea.scentdb.dto.mapper.CompanyMapper;
import com.github.remusselea.scentdb.dto.model.company.CompanyDto;
import com.github.remusselea.scentdb.dto.request.CompanyModel;
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
import javax.validation.constraints.NotNull;
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
public class CompanyService {

  private final PerfumerRepository perfumerRepository;

  private final CompanyRepository companyRepository;

  private final PerfumeRepository perfumeRepository;

  private final CompanyMapper companyMapper;

  private final ImageService imageService;

  private final EntityManager entityManager;

  @Value("${companies.images.dir:${user.home}}")
  public String uploadDir;

  public CompanyService(PerfumerRepository perfumerRepository,
      CompanyRepository companyRepository,
      PerfumeRepository perfumeRepository,
      CompanyMapper companyMapper,
      ImageService imageService,
      EntityManager entityManager) {
    this.perfumerRepository = perfumerRepository;
    this.companyRepository = companyRepository;
    this.perfumeRepository = perfumeRepository;
    this.companyMapper = companyMapper;
    this.imageService = imageService;
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public CompanyDto getCompanyById(long companyId) {
    Company company = companyRepository.findById(companyId).orElseThrow(
        () -> new EntityNotFoundException("Could not find company with id: " + companyId));

    return companyMapper.companyToCompanyDto(company);
  }

  @Transactional
  public CompanyDto saveCompany(CompanyModel companyModel, MultipartFile multipartImageFile) {
    Company companyToSave = companyMapper.companyModelToCompany(companyModel);

    Set<Perfume> perfumeList = new HashSet<>(companyModel.getPerfumeIdList().size());
    for (Long perfumeId : companyModel.getPerfumeIdList()) {
      Optional<Perfume> optionalPerfume = perfumeRepository.findById(perfumeId);
      optionalPerfume.ifPresent(perfumeList::add);
    }
    companyToSave.setPerfumes(perfumeList);


    Set<Perfumer> perfumerList = new HashSet<>(companyModel.getPerfumerIdList().size());
    for (Long perfumerId : companyModel.getPerfumerIdList()) {
      Optional<Perfumer> optionalPerfumer = perfumerRepository.findById(perfumerId);
      optionalPerfumer.ifPresent(perfumerList::add);
    }
    companyToSave.setPerfumers(perfumerList);

    String imageFileName = imageService.storeImage(multipartImageFile, uploadDir);
    String companyImagePath = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/scentdb/v1/images/companies/")
        .path(imageFileName)
        .toUriString();
    companyToSave.setImagePath(companyImagePath);

    Company savedCompany = companyRepository.save(companyToSave);

    return companyMapper.companyToCompanyDto(savedCompany);
  }

  public List<CompanyDto> getAllCompanies() {
    List<Company> companyList = companyRepository.findAll();
    List<CompanyDto> companyDtoList = new ArrayList<>(companyList.size());

    for (Company company : companyList) {
      CompanyDto companyDto = companyMapper.companyToCompanyDto(company);
      companyDtoList.add(companyDto);
    }

    return companyDtoList;
  }

  public Page<CompanyDto> search(Pageable pageable, String query) {
    Page<CompanyDto> companyDtoPage;

    if ((query == null || query.isBlank()) ) {
      companyDtoPage = searchMatchAll(pageable);
    } else {
      companyDtoPage = searchTerms(pageable, query);
    }

    return companyDtoPage;
  }


  @Transactional(readOnly = true)
  public Page<CompanyDto> searchMatchAll(Pageable pageable) {
    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Company> scope = searchSession.scope(Company.class);
    SearchSort searchSort = getSearchSort(pageable, scope);

    ElasticsearchSearchQuery<Company> searchQuery = searchSession.search(Company.class)
        .extension(ElasticsearchExtension.get())
        .where(SearchPredicateFactory::matchAll)
        .sort(searchSort)
        .toQuery();

    SearchResult<Company> result = searchQuery
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    return getCompanyPage(pageable, result);
  }

  @Transactional(readOnly = true)
  public Page<CompanyDto> searchTerms(Pageable pageable, String query) {
    SearchSession searchSession = Search.session(entityManager);
    SearchScope<Company> scope = searchSession.scope(Company.class);

    SearchPredicate boolPredicates = formSearchPredicates(query, scope);
    SearchSort searchSort = getSearchSort(pageable, scope);

    SearchResult<Company> result = searchSession.search(scope)
        .where(boolPredicates)
        .sort(searchSort)
        .fetch((int) pageable.getOffset(), pageable.getPageSize());

    return getCompanyPage(pageable, result);
  }

  @NotNull
  private Page<CompanyDto> getCompanyPage(Pageable pageable, SearchResult<Company> result) {
    List<Company> companyList = result.hits();
    List<CompanyDto> companyDtoList = new ArrayList<>(companyList.size());

    for (Company company : companyList) {
      CompanyDto companyDto = companyMapper.companyToCompanyDto(company);
      companyDtoList.add(companyDto);
    }

    return new PageImpl<>(companyDtoList, pageable, result.total().hitCount());
  }

  private SearchSort getSearchSort(Pageable pageable, SearchScope<Company> scope) {
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

  private SearchPredicate formSearchPredicates(String query, SearchScope<Company> searchScope) {
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

  public void removeCompanyById(Long companyId) {
    companyRepository.deleteById(companyId);
  }

}
