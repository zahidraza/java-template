package com.jazasoft.mtdb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by mdzahidraza on 27/06/17.
 */
public class MyEvent extends ContextRefreshedEvent {

    private String dbName;

    public MyEvent(ApplicationContext source, String dbName) {
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
