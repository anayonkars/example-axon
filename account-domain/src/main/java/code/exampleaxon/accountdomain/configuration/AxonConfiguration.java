package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.Registration;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.jdbc.DataSourceConnectionProvider;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.SnapshotEventEntry;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.spring.jdbc.SpringDataSourceConnectionProvider;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
@EntityScan(basePackageClasses = {
        DomainEventEntry.class,
        SnapshotEventEntry.class,
        Account.class,
        AccountView.class
})
@EnableJpaRepositories(basePackages = {
        "code.exampleaxon.accountdomain.command",
        "code.exampleaxon.accountdomain.query"
})
//@Import({CommandDataSourceConfiguration.class})
public class AxonConfiguration {

    @Qualifier("commandTransactionManager")
    @Autowired
    private PlatformTransactionManager commandTransactionManager;

    @Qualifier("commandDataSource")
    @Autowired
    private DataSource commandDataSource;

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        EntityManagerProvider entityManagerProvider = new EntityManagerProvider() {
            private EntityManager entityManager;

            @Override
            public EntityManager getEntityManager() {
                return entityManager;
            }

            @PersistenceContext(unitName = "commandPU")
            public void setEntityManager(EntityManager entityManager) {
                this.entityManager = entityManager;
            }
        };
        return entityManagerProvider;
    }

    @Bean
    public Serializer serializer() {
        return new JacksonSerializer();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return new SpringDataSourceConnectionProvider(commandDataSource);
    }

    @Bean
    public TransactionManager transactionManager() {
        return new SpringTransactionManager(commandTransactionManager);
    }

    @Bean
    public CommandBus commandBus(TransactionManager transactionManager) {
        return new SimpleCommandBus(transactionManager, NoOpMessageMonitor.INSTANCE);
    }

    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGateway
            (CommandBus commandBus) {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus);
        return factory;
    }

    @Bean
    public EventStorageEngine eventStorageEngine(EntityManagerProvider entityManagerProvider, TransactionManager transactionManager) {
        return new JpaEventStorageEngine(entityManagerProvider, transactionManager);
    }

    @Bean
    public EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public EventSourcingRepository<Account> accountRepository(EventStore
                                                                          eventStore, EventBus eventBus) {
        return new EventSourcingRepository<Account>(Account.class, eventStore);
    }

    @Bean
    public Registration registration(EventSourcingRepository<Account>
                                                     accountRepository,
                                       CommandBus commandBus) {
        AggregateAnnotationCommandHandler aggregateAnnotationCommandHandler =
                new AggregateAnnotationCommandHandler(Account.class, accountRepository);
        return aggregateAnnotationCommandHandler.subscribe(commandBus);
    }
}
