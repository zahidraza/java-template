package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.repository.UrlInterceptorRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class InterceptorService {
    private final Logger LOGGER = LoggerFactory.getLogger(InterceptorService.class);

    private UrlInterceptorRepository urlInterceptorRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired Mapper mapper;

    @Autowired
    public InterceptorService(UrlInterceptorRepository urlInterceptorRepository) {
        this.urlInterceptorRepository = urlInterceptorRepository;
    }

    public UrlInterceptor findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return urlInterceptorRepository.findOne(id);
    }

    public List<UrlInterceptor> findAll(){
        LOGGER.debug("findAll");
        return urlInterceptorRepository.findAll();
    }

    public List<UrlInterceptor> findAll(Company company){
        LOGGER.debug("findAll: company = {}", company.getName());
        if (company != null) {
            return urlInterceptorRepository.findByCompany(company);
        }
        return urlInterceptorRepository.findAll();
    }

    public List<UrlInterceptor> findAllByCompanyAndUrl(Company company, String url) {
        LOGGER.debug("findAllByCompanyAndUrl: company = {}, url = {}", company.getName(), url);
        return urlInterceptorRepository.findByCompanyAndUrl(company,url);
    }

    public List<UrlInterceptor> save(List<UrlInterceptor> urlInterceptorList) {
        urlInterceptorList.forEach(urlInterceptor -> {
            if (urlInterceptor.getCompanyId() != null) {
                urlInterceptor.setCompany(companyRepository.findOne(urlInterceptor.getCompanyId()));
            }
        });
        return urlInterceptorRepository.save(urlInterceptorList);
    }

    public UrlInterceptor save(UrlInterceptor urlInterceptor) {
        LOGGER.debug("save:");
        if (urlInterceptor.getCompanyId() != null) {
            urlInterceptor.setCompany(companyRepository.findOne(urlInterceptor.getCompanyId()));
        }
        return urlInterceptorRepository.save(urlInterceptor);
    }

    public UrlInterceptor update(UrlInterceptor urlInterceptor) {
        LOGGER.debug("update()");
        UrlInterceptor urlInterceptor2 = urlInterceptorRepository.findOne(urlInterceptor.getId());
        mapper.map(urlInterceptor, urlInterceptor2);
        return urlInterceptor2;
    }

    public void delete(Long id) {
        LOGGER.debug("save: urlInterceptorId = {}", id);
        urlInterceptorRepository.delete(id);
    }

    public long count() {
        return urlInterceptorRepository.count();
    }

    public boolean exists(Long id) {
        return urlInterceptorRepository.exists(id);
    }

    public boolean exists(Company company, Long id) {
        LOGGER.debug("exists(): tenant = {} id = {}",company.getName(),id);
        return urlInterceptorRepository.findOneByCompanyAndId(company, id).isPresent();
    }

}
