package com.jazasoft.mtdbapp.service;

import com.jazasoft.mtdb.UserCreatedEvent;
import com.jazasoft.mtdb.tenant.MultiTenantConnectionProviderImpl;
import com.jazasoft.mtdbapp.entity.TUser;
import com.jazasoft.mtdbapp.repository.TUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by mdzahidraza on 22/07/17.
 */
@Service

public class TUserService implements ApplicationListener<UserCreatedEvent> {
    private final Logger LOGGER = LoggerFactory.getLogger(TUserService.class);

    @Autowired
    MultiTenantConnectionProviderImpl provider;

    private TUserRepository tUserRepository;

    public TUserService(TUserRepository tUserRepository) {
        this.tUserRepository = tUserRepository;
    }

    @Override
    public void onApplicationEvent(UserCreatedEvent userCreatedEvent) {
        Long userId = userCreatedEvent.getUserId();
        TUser user = new TUser(userId);
        DataSource dataSource = provider.selectDataSource(userCreatedEvent.getTenantId());
        boolean result = save(dataSource, user);
        if (result) {
            throw new RuntimeException("Unbale to persist user in tenant user table");
        }
    }

    private boolean save(DataSource dataSource, TUser user) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "insert into  t_user values(?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1,user.getId());
            boolean rs = ps.execute();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(value = "tenantTransactionManager", readOnly = true)
    public TUser findOne(Long id) {
        return tUserRepository.findOne(id);
    }

}
