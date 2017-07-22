package com.jazasoft.mtdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by mdzahidraza on 22/07/17.
 */
public class UserCreatedEvent extends ApplicationContextEvent {
    private Long userId;
    private String tenantId;

    public UserCreatedEvent(ApplicationContext source, Long userId, String tenantId) {
        super(source);
        this.userId = userId;
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

    @Override
    public String toString() {
        return "UserCreatedEvent{" + "userId=" + userId + ", tenantId='" + tenantId + '\'' + '}';
    }
}
