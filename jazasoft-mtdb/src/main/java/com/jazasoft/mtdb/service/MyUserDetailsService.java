package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.repository.UserRepository;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Service
@Transactional(value = "masterTransactionManager", readOnly = true)
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired Mapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.trace("Looking for user for {}", username);
        try {
            Optional<User> user = userRepository.findOneByUsername(username);
            if (!user.isPresent()) {
                user = userRepository.findOneByEmail(username);
                if (!user.isPresent()) {
                    LOGGER.info("USER NOT PRESENT for {}", username);
                    throw new UsernameNotFoundException("user not found");
                }
            }
            LOGGER.trace("Found user for {}", username);
            return user.get();
        } catch (Exception e) {
            LOGGER.error("Error loading user {}", username, e);
        }
        return null;
    }

}
