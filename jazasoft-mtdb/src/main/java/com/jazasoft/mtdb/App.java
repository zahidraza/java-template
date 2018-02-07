package com.jazasoft.mtdb;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdzahidraza on 21/07/17.
 */
public abstract class App extends SpringBootServletInitializer {

    @Bean
    public Mapper dozerBeanMapper() {
        List<String> list = new ArrayList<>();
        list.add("dozer_master.xml");
        addDozerMappings(list);
        return new DozerBeanMapper(list);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        List<String> list = new ArrayList<>();
        list.add("classpath:messages_master");
        list.add("classpath:messages");
        addMessageSourceBasenames(list);

        String[] basenames = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            basenames[i] = list.get(i);
        }
        messageSource.setBasenames(basenames);
        return messageSource;
    }

    protected void addDozerMappings(List<String> list){}

    protected void addMessageSourceBasenames(List<String> list) {}

}
