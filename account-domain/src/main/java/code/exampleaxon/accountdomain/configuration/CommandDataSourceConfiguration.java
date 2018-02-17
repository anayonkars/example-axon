package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
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
import org.springframework.context.annotation.Primary;
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
        entityManagerFactoryRef = "commandEntityManagerFactory",
        basePackages = {"code.exampleaxon.accountdomain.command",
                        "org.axonframework.eventstore.jpa"}
        //basePackageClasses = {AccountViewRepository.class, AccountView.class}
)
@EntityScan(basePackageClasses = {
        DomainEventEntry.class,
        SnapshotEventEntry.class,
        Account.class,
        AccountView.class
})
public class CommandDataSourceConfiguration {
    @Primary
    @Bean(name = "commandDataSource")
    @ConfigurationProperties(prefix = "command.datasource")
    public DataSource commandDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "commandEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean commandEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                              @Qualifier("commandDataSource") DataSource commandDataSource) {
        LocalContainerEntityManagerFactoryBean
        localContainerEntityManagerFactoryBean =  builder
                .dataSource(commandDataSource)
                .packages("code.exampleaxon.accountdomain.command")
                .persistenceUnit("commandPU")
                .build();
        localContainerEntityManagerFactoryBean.setPackagesToScan("code.exampleaxon.accountdomain.command",
                "org.axonframework.eventstore.jpa");
        return localContainerEntityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "commandTransactionManager")
    public PlatformTransactionManager commandTransactionManager(@Qualifier("commandEntityManagerFactory")EntityManagerFactory commanEntityManagerFactory) {
        return new JpaTransactionManager(commanEntityManagerFactory);
    }
}
