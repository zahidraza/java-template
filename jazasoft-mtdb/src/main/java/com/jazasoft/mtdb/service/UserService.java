package com.jazasoft.mtdb.service;


import com.jazasoft.mtdb.dto.UserDto;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.repository.RoleRepository;
import com.jazasoft.mtdb.repository.UserRepository;
import com.jazasoft.mtdb.util.Utils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;

    @Autowired Mapper mapper;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    RoleRepository roleRepository;
//
//    @PersistenceContext
//    EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findOne(Long id) {
        LOGGER.debug("findOne(): id = {}",id);
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        LOGGER.debug("findAll()");
        return userRepository.findAll();
    }

    public List<User> findAllAfter(long after) {
        LOGGER.debug("findAllAfter(): after = {}" , after);
        return userRepository.findByModifiedAtGreaterThan(new Date(after));
    }

    public List<User> findAllByCompanyAfter(Company company, long after) {
        LOGGER.debug("findAllAfter(): after = {}" , after);
        return userRepository.findByModifiedAtGreaterThanAndCompany(new Date(after), company);
    }

    public User findByEmail(String email) {
        LOGGER.debug("findByEmail(): email = {}",email);
        return userRepository.findOneByEmail(email).get();
    }

    public User findByUsername(String username) {
        LOGGER.debug("findByUsername(): username = " , username);
        return userRepository.findOneByUsername(username).get();
    }

    public Boolean exists(Long id) {
        LOGGER.debug("exists(): id = ",id);
        return userRepository.exists(id);
    }

    public boolean exists(Company company, Long id) {
        LOGGER.debug("exists(): tenant = {} id = {}",company.getName(),id);
        return userRepository.findOneByCompanyAndId(company, id).isPresent();
    }

    public Long count(){
        LOGGER.debug("count()");
        return userRepository.count();
    }

    @Transactional
    public User save(User user) {
        LOGGER.debug("save()");
        user.setPassword(user.getMobile());
        user.setEnabled(true);
        if (user.getCompanyId() != null) {
            user.setCompany(companyRepository.findOne(user.getCompanyId()));
        }
        if (user.getRoles() != null) {
            Utils.getRoleList(user.getRoles()).stream().forEach(role -> user.addRole(roleRepository.findOneByName("ROLE_"+role).get()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(UserDto userDto) {
        LOGGER.debug("update()");
        User user = userRepository.findOne(userDto.getId());
        mapper.map(userDto,user);
        if (userDto.getRoles() != null) {
            user.getRoleList().clear();
            Utils.getRoleList(user.getRoles()).stream().forEach(role -> user.addRole(roleRepository.findOneByName("ROLE_"+role).get()));
        }
        return user;
    }

    @Transactional
    public void delete(Long id) {
        LOGGER.debug("delete(): id = {}",id);
        User user = userRepository.findOne(id);
        user.setEnabled(false);
    }


    public User update(User user){
        User user2 = userRepository.findOne(user.getId());

        if (user.getName() != null) user2.setName(user.getName());
        if (user.getUsername() != null) user2.setUsername(user.getUsername());
        if (user.getEmail() != null) user2.setEmail(user.getEmail());
        if (user.getMobile() != null) user2.setMobile(user.getMobile());

        return user2;
    }

//    public void findLastChangeRevision(Long id) {
//        AuditReader reader = AuditReaderFactory.get(entityManager);
//        List<Object[]> list = reader.createQuery()
//                .forRevisionsOfEntity(User.class,false,true)
//                .add(AuditEntity.id().eq(id))
//                .getResultList();
//
//        list.forEach(l -> {
//            User user = (User) l[0];
//            MyRevisionEntity ur = (MyRevisionEntity)l[1];
//            RevisionType revisionType = (RevisionType)l[2];
//
//            System.out.println("user = " + user);
//            System.out.println("user_rev = " + ur);
//            System.out.println("rev_type = " + revisionType.name());
//        });
//
//
//        Revision<Integer,User> revision = userRepository.findLastChangeRevision(id);
//
//        System.out.println("Last Change Revision: ");
//        printRevision(revision);
//
//        System.out.println("All revisions: ");
//        Revisions<Integer, User> revisions = userRepository.findRevisions(id);
//        revisions.iterator().forEachRemaining(revisin -> {
//            printRevision(revisin);
//        });
//    }
//
//    private void printRevision(Revision<Integer,User> revision) {
//        System.out.println("Revision no = " +revision.getRevisionNumber());
//        System.out.println("Revision date = " + revision.getRevisionDate());
//        System.out.println("revision data = " + revision.getEntity());
//        if (revision.getMetadata().getDelegate() != null){
//            MyRevisionEntity entity = (MyRevisionEntity)revision.getMetadata().getDelegate();
//            System.out.println("modifiedBy = " + entity.getUsername());
//        }
//    }

}
