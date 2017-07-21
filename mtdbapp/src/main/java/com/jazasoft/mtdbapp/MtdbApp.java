package com.jazasoft.mtdbapp;

import com.jazasoft.mtdb.App;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.service.CompanyService;
import com.jazasoft.mtdb.service.RoleService;
import com.jazasoft.mtdb.service.UserService;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdzahidraza on 21/07/17.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.jazasoft.mtdb"})
public class MtdbApp extends App{

    private final Logger LOGGER = LoggerFactory.getLogger(MtdbApp.class);

    public static void main(String[] args){
        SpringApplication.run(MtdbApp.class);
    }

    @Bean
    CommandLineRunner init(
            UserService userService,
            RoleService roleService,
            CompanyService companyService) {

        return (args) -> {
            if (roleService.count() == 0) {
                LOGGER.info("No of roles = " + 0);
                roleService.save(new Role("MASTER","Super user for Tennant management"));
            }
            if(userService.count() == 0){
                LOGGER.info("No of users = " + 0);
                Role role = roleService.findOneByName("MASTER");
                System.out.println(role);
                User user = new User("Md Zahid Raza","zahid7292","zahid7292@gmail.com","admin","8987525008",true,false,false,false);
                user.addRole(role);
                userService.save(user);
            }
        };
    }

    @Bean
    public Mapper dozerBeanMapper() {
        List<String> list = new ArrayList<>();
        list.add("dozer_mapping.xml");
        return new DozerBeanMapper(list);
    }
}
