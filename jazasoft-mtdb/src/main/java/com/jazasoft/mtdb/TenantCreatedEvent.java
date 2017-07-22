package com.jazasoft.mtdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/**
 * Created by mdzahidraza on 27/06/17.
 */
public class TenantCreatedEvent extends ApplicationContextEvent {

    private String dbName;

    public TenantCreatedEvent(ApplicationContext source, String dbName) {
        super(source);
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
