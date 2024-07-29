package com.liutyk.first_demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class PersistenceConfiguration {
//    @Bean(name="dataSource")
//    public DataSource dataSource(){
//        DataSourceBuilder<?> builder = DataSourceBuilder.create();
//        System.out.println("-------Custom datasource bean has been initialized and set-------");
//        return builder
//                    .url("jdbc:h2:./data/Spring")
//                    .username("DatabaseH2")
//                    .password("")
//                    .build();
//
//    }
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("com/liutyk/first_demo/models"); // Пакет, де знаходяться ваші сутності
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        em.setJpaProperties(additionalProperties());
//        return em;
//    }
//
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//
//    @Bean(name = "transactionManagerDataSource")
//    public PlatformTransactionManager transactionManagerDataSource() {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//    private Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // Ваші налаштування Hibernate
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        return properties;
//    }
}
