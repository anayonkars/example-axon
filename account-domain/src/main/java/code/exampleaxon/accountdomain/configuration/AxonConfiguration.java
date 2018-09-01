package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import static org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler.subscribe;

@Configuration
@EntityScan(basePackageClasses = {
        DomainEventEntry.class,
        SnapshotEventEntry.class,
        Account.class,
        AccountView.class
})
@EnableJpaRepositories(basePackages = {
        "code.exampleaxon.accountdomain.query",
})
public class AxonConfiguration {

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    public Serializer serializer() {
        return new JacksonSerializer();
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus simpleCommandBus = new SimpleCommandBus();
        simpleCommandBus.setTransactionManager(new SpringTransactionManager
                (transactionManager));
        return simpleCommandBus;
    }

    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGateway(CommandBus commandBus) {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus);
        return factory;
    }

    @Bean
    public EventStore eventStore(EntityManagerProvider entityManagerProvider, Serializer serializer) {
        return new JpaEventStore(entityManagerProvider, serializer);
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public EventSourcingRepository<Account> accountRepository(EventStore eventStore, EventBus eventBus) {
        EventSourcingRepository<Account> repository = new EventSourcingRepository<Account>(Account.class, eventStore);
        repository.setEventBus(eventBus);
        return repository;
    }

    @Bean
    public AggregateAnnotationCommandHandler commandHandler
            (EventSourcingRepository<Account> accountRepository, CommandBus commandBus) {
        return  subscribe(Account.class, accountRepository, commandBus);
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
