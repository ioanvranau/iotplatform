package backup;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Magda on 5/31/2014.
 *
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.httpstreaming.api.services", "com.httpstreaming.api.bussiness.model", "com.httpstreaming.api", "com.httpstreaming.api.producer"})
@PropertySource("classpath:jdbc.properties")
@EnableJpaRepositories("com.httpstreaming.api.services")
public class JPAConfig {
    private static final String PROPERTY_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_DATABASE_URL = "db.url";
    private static final String PROPERTY_DATABASE_USERNAME = "db.username";

    private static final String PROPERTY_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_HIBERNATE_HBM2DDL = "hibernate.hbm2ddl.auto";
    private static final String PROPERTY_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_ENTITY_MANAGER_PACKAGES_TO_SCAN = "entity.manager.packages.to.scan";

    @Resource
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getRequiredProperty(PROPERTY_DATABASE_DRIVER));
        dataSource.setUrl(environment.getRequiredProperty(PROPERTY_DATABASE_URL));
        dataSource.setUsername(environment.getRequiredProperty(PROPERTY_DATABASE_USERNAME));
        dataSource.setPassword(environment.getRequiredProperty(PROPERTY_DATABASE_PASSWORD));
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        DataSource dataSource = getDataSource();

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(environment
                .getRequiredProperty(PROPERTY_ENTITY_MANAGER_PACKAGES_TO_SCAN).split(","));

        Properties properties = new Properties();
        properties.put(PROPERTY_HIBERNATE_DIALECT, environment.getRequiredProperty(PROPERTY_HIBERNATE_DIALECT));
        properties.put(PROPERTY_HIBERNATE_HBM2DDL, environment.getRequiredProperty(PROPERTY_HIBERNATE_HBM2DDL));
        properties.put(PROPERTY_HIBERNATE_SHOW_SQL, environment.getRequiredProperty(PROPERTY_HIBERNATE_SHOW_SQL));
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }



    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}
