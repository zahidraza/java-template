package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.TenantCreatedEvent;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.repository.CompanyRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class CompanyService implements ApplicationEventPublisherAware {
    private final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

    private CompanyRepository companyRepository;

    private ApplicationEventPublisher publisher;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired Mapper mapper;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return companyRepository.findOne(id);
    }

    public List<Company> findAll(){
        LOGGER.debug("findAll");
        return companyRepository.findAll();
    }

    public Company save(Company company) {
        LOGGER.debug("save: company = {}", company);
        publisher.publishEvent(new TenantCreatedEvent(applicationContext,company.getDbName()));
        company.setEnabled(true);
        return companyRepository.save(company);
    }

    public Company update(Company company) {
        LOGGER.debug("update: ");
        Company company2 = companyRepository.findOne(company.getId());
        mapper.map(company,company2);
        return company2;
    }

    public void delete(Long id) {
        LOGGER.debug("delete: id = {}", id);
        Company company = companyRepository.findOne(id);
        company.setEnabled(false);
    }
    
    public long count() {
        return companyRepository.count();
    }

    public boolean exists(Long id) {
        return companyRepository.exists(id);
    }
}
