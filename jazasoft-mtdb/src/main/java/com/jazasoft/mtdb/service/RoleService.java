package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.repository.RoleRepository;
import com.jazasoft.mtdb.repository.UrlInterceptorRepository;
import com.jazasoft.mtdb.util.Utils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class RoleService {
    private final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private RoleRepository roleRepository;
    private UrlInterceptorRepository urlInterceptorRepository;
    private CompanyRepository companyRepository;
    private Mapper mapper;

    public RoleService(RoleRepository roleRepository, UrlInterceptorRepository urlInterceptorRepository, CompanyRepository companyRepository, Mapper mapper) {
        this.roleRepository = roleRepository;
        this.urlInterceptorRepository = urlInterceptorRepository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    //TODO: set role permission as well
    public Role findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return roleRepository.findOne(id);
    }

    public Role findAnyByName(String role) {
        return roleRepository.findByName("ROLE_" + role).stream().findAny().orElse(null);
    }

    public List<Role> findAll(){
        LOGGER.debug("findAll");
        return roleRepository.findAll();
    }

    public List<Role> findAll(Company company){
        LOGGER.debug("findAll: company = {}", company.getName());
        List<Role> roles = new ArrayList<>();
        if (company != null) {
            roles = roleRepository.findByCompany(company);
            roles.forEach(role -> {
                List<UrlInterceptor> interceptors = urlInterceptorRepository.findByCompanyAndAccess(company, role.getName());
                role.setPermissions(Utils.getPermissions(interceptors));
            });
        }
        return roles;
    }
    
    public Role save(Role role) {
        LOGGER.debug("save: role = {}", role);
        if (role.getCompanyId() != null) {
            role.setCompany(companyRepository.findOne(role.getCompanyId()));
        }
        role.setName("ROLE_" + role.getName());
        role = roleRepository.save(role);
        List<UrlInterceptor> interceptors = urlInterceptorRepository.findByCompanyAndAccess(role.getCompany(), role.getName());
        role.setPermissions(Utils.getPermissions(interceptors));
        return role;
    }

    public Role update(Role role) {
        LOGGER.debug("update()");
        Role role2 = roleRepository.findOne(role.getId());
        mapper.map(role, role2);
        role2.setName("ROLE_" + role2.getName());
        LOGGER.info("role = {}", role2.getName());
        LOGGER.info("company: ", role2.getCompany());
        List<UrlInterceptor> interceptors = urlInterceptorRepository.findByCompanyAndAccess(role2.getCompany(), role2.getName());
        LOGGER.info("intc size = {}", interceptors.size());
        role2.setPermissions(Utils.getPermissions(interceptors));
        return role2;
    }

    public long count() {
        return roleRepository.count();
    }

    public boolean exists(Long id) {
        return roleRepository.exists(id);
    }

    public boolean exists(Company company, Long id) {
        LOGGER.debug("exists(): tenant = {} id = {}",company.getName(),id);
        return roleRepository.findOneByCompanyAndId(company, id).isPresent();
    }

    public boolean exists(String role) {
        return roleRepository.findByName("ROLE_" + role).stream().findAny().isPresent();
    }

    public boolean exists(String role, Company company) {
        return roleRepository.findByNameAndCompany("ROLE_" + role, company).isPresent();
    }
}
