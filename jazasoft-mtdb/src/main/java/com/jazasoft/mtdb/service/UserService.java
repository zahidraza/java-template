package com.jazasoft.mtdb.service;


import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.UserCreatedEvent;
import com.jazasoft.mtdb.dto.Permission;
import com.jazasoft.mtdb.dto.UserDto;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.repository.RoleRepository;
import com.jazasoft.mtdb.repository.UrlInterceptorRepository;
import com.jazasoft.mtdb.repository.UserRepository;
import com.jazasoft.mtdb.util.Utils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager")
public class UserService implements ApplicationEventPublisherAware {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private ApplicationEventPublisher publisher;

    UserRepository userRepository;

    Mapper mapper;

    CompanyRepository companyRepository;

    RoleRepository roleRepository;

    UrlInterceptorRepository interceptorRepository;

    ApplicationContext applicationContext;

    IEmailService emailService;

//
//    @PersistenceContext
//    EntityManager entityManager;


    public UserService(ApplicationEventPublisher publisher, UserRepository userRepository, Mapper mapper, CompanyRepository companyRepository, RoleRepository roleRepository, UrlInterceptorRepository interceptorRepository, ApplicationContext applicationContext, IEmailService emailService) {
        this.publisher = publisher;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.interceptorRepository = interceptorRepository;
        this.applicationContext = applicationContext;
        this.emailService = emailService;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public User findOne(Long id) {
        LOGGER.debug("findOne(): id = {}",id);
        User user = userRepository.findOne(id);
        if (user == null) return  null;
        Set<Permission> permissions = new HashSet<>();
        user.getRoleList().forEach(role -> {
            List<UrlInterceptor> interceptors = interceptorRepository.findByAccess(role.getName());
            //TODO: In case resource overlap for role, access of one will override other
            permissions.addAll(Utils.getPermissions(interceptors));
        });
        user.setPermissions(permissions);
        return user;
    }

    public User getProfile(String username) {
        User user = findByEmail(username);
        if (user == null) {
            user = findByUsername(username);
        }
        if (user == null) return null;
        Set<Permission> permissions = new HashSet<>();
        user.getRoleList().forEach(role -> {
            List<UrlInterceptor> interceptors = interceptorRepository.findByAccess(role.getName());
            //TODO: In case resource overlap for role, access of one will override other
            permissions.addAll(Utils.getPermissions(interceptors));
        });
        user.setPermissions(permissions);
        return user;
    }

    public List<User> findAll() {
        LOGGER.debug("findAll()");
        return userRepository.findAll();
    }

    public List<User> findAll(List<Long> idList) {
        LOGGER.debug("findAll()");
        return userRepository.findAll(idList);
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
        return userRepository.findOneByEmail(email).orElse(null);
    }

    public User findByUsername(String username) {
        LOGGER.debug("findByUsername(): username = {}" , username);
        return userRepository.findOneByUsername(username).orElse(null);
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
            //check for company. Master will send company using comapnyId field. If Master is adding, find role only by name
            if (user.getCompany() != null && user.getCompanyId() == null) {
                Utils
                    .getRoleList(user.getRoles()).stream()
                    .forEach(role -> user.addRole(roleRepository.findByNameAndCompany("ROLE_"+role, user.getCompany()).orElse(null)));
            } else {
                Utils
                    .getRoleList(user.getRoles()).stream()
                    .forEach(role -> user.addRole(roleRepository.findByName("ROLE_"+role).stream().findAny().orElse(null)));
            }
        }

        User user2 = userRepository.save(user);

        /* If User is not master publish user created event to add user in tenant user table*/
        Set<Role> roles = user2.getRoleList();
        if (!roles.isEmpty()) {
            //If Role is not master
            if (roles.stream().filter(role -> role.getName().equals(Constants.ROLE_MASTER)).count() == 0) {
                if (user2.getCompany() != null) {
                    UserCreatedEvent event = new UserCreatedEvent(applicationContext, user2.getId(), user2.getName(), user2.getUsername(), user2.getEmail(), user2.getMobile(), user2.getCompany().getDbName());
                    publisher.publishEvent(event);
                }else {
                    LOGGER.warn("User does not belong to any company.");
                }
            }
        }else {
            LOGGER.warn("Saving user without any role.");
        }
        return user2;
    }

    @Transactional
    public User update(UserDto userDto) {
        LOGGER.debug("update()");
        User user = userRepository.findOne(userDto.getId());
        mapper.map(userDto,user);
        if (userDto.getRoles() != null) {
            user.getRoleList().clear();
            if (userDto.getCompany() != null) {
                Utils.getRoleList(user.getRoles()).stream().forEach(role -> user.addRole(roleRepository.findByNameAndCompany("ROLE_"+role, userDto.getCompany()).get()));
            }else {
                Utils.getRoleList(user.getRoles()).stream().forEach(role -> user.addRole(roleRepository.findByName("ROLE_"+role).stream().findAny().get()));
            }

        }
        return user;
    }

    @Transactional
    public void delete(Long id) {
        LOGGER.debug("delete(): id = {}",id);
        User user = userRepository.findOne(id);
        user.setEnabled(false);
    }

    @Transactional
    public User update(User user){
        User user2 = userRepository.findOne(user.getId());

        if (user.getName() != null) user2.setName(user.getName());
        if (user.getUsername() != null) user2.setUsername(user.getUsername());
        if (user.getEmail() != null) user2.setEmail(user.getEmail());
        if (user.getMobile() != null) user2.setMobile(user.getMobile());

        return user2;
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findOne(userId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    public  boolean emailModeResetPassword(User user) {
        user = userRepository.findOne(user.getId());
        String resetPassword = com.jazasoft.util.Utils.getRandomAlphaNemeric(10);
        String email = user.getEmail();
        String subject = "Password Reset";
        String message = "Hello, " + user.getName() + "\n\n" +
                "New Password: " + resetPassword + "\n\n" +
                "Please, Do change your password." + "\n\n\n" +
                "Jaza Software (OPC) Private Limited.";
        emailService.sendSimpleEmail(new String[]{email}, subject, message);
        user.setPassword(resetPassword);
        return true;
    }

    public boolean sendOtp(User user, String resetMode) {

        return false;
    }

    public boolean confirmOtp(User user, String otp) {
        return false;
    }

    public boolean changeForgotPassword(User user, String otp, String newPassword) {

        return false;
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
