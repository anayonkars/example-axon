package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.common.jdbc.ConnectionProvider;
import org.axonframework.common.jdbc.SpringDataSourceConnectionProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventstore.jpa.JpaEventStore;
import org.axonframework.eventstore.jpa.SnapshotEventEntry;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.json.JacksonSerializer;
import org.axonframework.unitofwork.SpringTransactionManager;
import org.axonframework.unitofwork.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
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
        "code.exampleaxon.accountdomain.query",
        "org.axonframework.eventstore.jpa"
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
        SimpleCommandBus simpleCommandBus = new SimpleCommandBus();
        simpleCommandBus.setTransactionManager(transactionManager);
        return simpleCommandBus;
    }

    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGateway
            (CommandBus commandBus) {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus);
        return factory;
    }

    @Bean
    public EventStore eventStore(EntityManagerProvider entityManagerProvider,
                                    Serializer serializer) {
        return new JpaEventStore(entityManagerProvider, serializer);
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public EventSourcingRepository<Account> accountRepository(EventStore
                                                                          eventStore, EventBus eventBus) {
        return new EventSourcingRepository<Account>(Account.class, eventStore);
    }

    @Bean
    public AggregateAnnotationCommandHandler commandHandler
            (EventSourcingRepository<Account>
                                                     accountRepository,
                                       CommandBus commandBus) {
        return  AggregateAnnotationCommandHandler.subscribe(Account.class,
                accountRepository, commandBus);
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor
    annotationEventListenerBeanPostProcessor(EventBus eventBus) {
        AnnotationEventListenerBeanPostProcessor processor = new AnnotationEventListenerBeanPostProcessor();
        processor.setEventBus(eventBus);
        return processor;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor
    annotationCommandHandlerBeanPostProcessor(CommandBus commandBus) {
        AnnotationCommandHandlerBeanPostProcessor processor = new AnnotationCommandHandlerBeanPostProcessor();
        processor.setCommandBus(commandBus);
        return processor;
    }
}
