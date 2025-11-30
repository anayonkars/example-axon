package code.exampleaxon.accountdomain.configuration;

import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventsourcing.eventstore.jpa.SnapshotEventEntry;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

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
        @org.springframework.context.annotation.Bean
        public org.axonframework.eventsourcing.EventSourcingRepository<Account> accountRepository(
                        org.axonframework.eventsourcing.eventstore.EventStore eventStore) {
                return org.axonframework.eventsourcing.EventSourcingRepository.builder(Account.class)
                                .eventStore(eventStore).build();
        }
}
