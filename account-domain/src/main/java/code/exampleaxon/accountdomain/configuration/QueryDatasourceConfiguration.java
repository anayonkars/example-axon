package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventstore.jpa.SnapshotEventEntry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "queryEntityManagerFactory",
        basePackages = {"code.exampleaxon.accountdomain.query",
                        "org.axonframework.eventstore.jpa"}
        //basePackageClasses = {AccountViewRepository.class, AccountView.class}
)
@EntityScan(basePackageClasses = {
        DomainEventEntry.class,
        SnapshotEventEntry.class,
        Account.class,
        AccountView.class
})
public class QueryDatasourceConfiguration {
    @Bean(name = "queryDataSource")
    @ConfigurationProperties(prefix = "query.datasource")
    public DataSource queryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "queryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean queryEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                              @Qualifier("queryDataSource") DataSource queryDataSource) {
        LocalContainerEntityManagerFactoryBean
        localContainerEntityManagerFactoryBean =  builder
                .dataSource(queryDataSource)
                .packages("code.exampleaxon.accountdomain.query")
                .persistenceUnit("queryPU")
               .build();
        localContainerEntityManagerFactoryBean.setPackagesToScan("code" +
                        ".exampleaxon.accountdomain.query",
                "org.axonframework.eventstore.jpa");
        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "queryTransactionManager")
    public PlatformTransactionManager queryTransactionManager(@Qualifier("queryEntityManagerFactory")EntityManagerFactory commanEntityManagerFactory) {
        return new JpaTransactionManager(commanEntityManagerFactory);
    }
}
