package com.itechart.devbooks.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = {@ComponentScan("com.itechart.devbooks")})
public class TestConfig {


    @Bean(name = "pgTestDataSource")
    public BasicDataSource pgTestDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/test_db?currentSchema=customer");
        dataSource.setPassword("1111");
        dataSource.setUsername("postgres");

        return dataSource;
    }
}
