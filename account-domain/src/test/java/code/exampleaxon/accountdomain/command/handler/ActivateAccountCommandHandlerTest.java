package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.ActivateAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.AccountActivatedEvent;
import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class ActivateAccountCommandHandlerTest {
    private FixtureConfiguration<Account> fixtureConfiguration;
    private EventSourcingRepository<Account> eventSourcingRepository;

    @Before
    public void setup() {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(Account.class);
        eventSourcingRepository = new EventSourcingRepository<Account>
                (Account.class, fixtureConfiguration.getEventStore());
        fixtureConfiguration.registerRepository(eventSourcingRepository);
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                ActivateAccountCommandHandler(eventSourcingRepository));
    }

    @Test
    public void expectAccountActivation() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"))
                            .when(new ActivateAccountCommand("test-id"))
                            .expectEvents(new AccountActivatedEvent
                                    ("test-id"));
    }

    @Test
    public void expectExceptionWhenReactivatingAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"))
                            .when(new ActivateAccountCommand("test-id"))
                            .expectException
                                    (AccountStateChangeNotValidException.class);
    }
}
