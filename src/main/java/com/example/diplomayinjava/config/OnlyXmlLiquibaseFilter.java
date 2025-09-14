package com.example.diplomayinjava.config;


import liquibase.changelog.IncludeAllFilter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OnlyXmlLiquibaseFilter implements IncludeAllFilter {
    @Override
    public boolean include(String s) {
        return s.endsWith(".xml");
    }
}
