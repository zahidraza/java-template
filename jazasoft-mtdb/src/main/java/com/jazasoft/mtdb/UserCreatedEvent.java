package com.jazasoft.mtdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by mdzahidraza on 22/07/17.
 */
public class UserCreatedEvent extends ApplicationContextEvent {
    private Long userId;
    private String name;
    private String username;
    private String email;
    private String mobile;

    private String tenantId;

    public UserCreatedEvent(ApplicationContext source, Long userId, String name, String username, String email, String mobile, String tenantId) {
        super(source);
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" + "userId=" + userId + ", tenantId='" + tenantId + '\'' + '}';
    }
}
