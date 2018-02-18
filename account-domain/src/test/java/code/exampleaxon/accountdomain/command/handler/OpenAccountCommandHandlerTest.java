package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStoreException;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class OpenAccountCommandHandlerTest {
    private FixtureConfiguration<Account> fixtureConfiguration;
    private EventSourcingRepository<Account> eventSourcingRepository;

    @Before
    public void setup() {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(Account.class);
        eventSourcingRepository = new EventSourcingRepository<Account>
                (Account.class, fixtureConfiguration.getEventStore());
        fixtureConfiguration.registerRepository(eventSourcingRepository);
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                OpenAccountCommandHandler(eventSourcingRepository));
    }

    @Test
    public void expectAccountOpenedEvent() {
        fixtureConfiguration.given()
                            .when(new OpenAccountCommand("test-id",
                                    "test-name"))
                            .expectEvents(new AccountOpenedEvent("test-id",
                                    "test-name"));
        /*fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"))
                .when(new OpenAccountCommand("test-id",
                        "test-name"))
                .expectException(EventStoreException.class);*/
    }

}
