package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.repository.RoleRepository;
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
public class RoleService {
    private final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private RoleRepository roleRepository;
    
    @Autowired
    CompanyRepository companyRepository;

    @Autowired Mapper mapper;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findOne(Long id){
        LOGGER.debug("findOne: id = {}", id);
        return roleRepository.findOne(id);
    }

    public Role findOneByName(String role) {
        return roleRepository.findOneByName("ROLE_" + role).orElse(null);
    }

    public List<Role> findAll(){
        LOGGER.debug("findAll");
        return roleRepository.findAll();
    }

    public List<Role> findAll(Company company){
        LOGGER.debug("findAll: company = {}", company.getName());
        if (company != null) {
            return roleRepository.findByCompany(company);
        }
        return roleRepository.findAll();
    }
    
    public Role save(Role role) {
        LOGGER.debug("save: role = {}", role);
        if (role.getCompanyId() != null) {
            role.setCompany(companyRepository.findOne(role.getCompanyId()));
        }
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        LOGGER.debug("update()");
        Role role2 = roleRepository.findOne(role.getId());
        mapper.map(role, role2);
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
        return roleRepository.findOneByName("ROLE_" + role).isPresent();
    }
}
