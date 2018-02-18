package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.ActivateAccountCommand;
import code.exampleaxon.accountdomain.command.CloseAccountCommand;
import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.AccountActivatedEvent;
import code.exampleaxon.accountdomain.command.event.AccountClosedEvent;
import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import code.exampleaxon.accountdomain.command.event.AmountCreditedEvent;
import code.exampleaxon.accountdomain.exception.AccountClosureNotValidException;
import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class CloseAccountCommandHandlerTest {
    private FixtureConfiguration<Account> fixtureConfiguration;
    private EventSourcingRepository<Account> eventSourcingRepository;

    @Before
    public void setup() {
        fixtureConfiguration = Fixtures.newGivenWhenThenFixture(Account.class);
        eventSourcingRepository = new EventSourcingRepository<Account>
                (Account.class, fixtureConfiguration.getEventStore());
        fixtureConfiguration.registerRepository(eventSourcingRepository);
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                CloseAccountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                ActivateAccountCommandHandler(eventSourcingRepository));
        /*fixtureConfiguration.registerAnnotatedCommandHandler(new
                CreditAmountCommandHandler(eventSourcingRepository));*/
        /*fixtureConfiguration.registerAnnotatedCommandHandler(new
                OpenAccountCommandHandler(eventSourcingRepository));*/
        //fixtureConfiguration.setReportIllegalStateChange(false);
    }

    @Test
    public void expectAccountClosure() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"))
                            .when(new CloseAccountCommand("test-id"))
                            .expectEvents(new AccountClosedEvent("test-id"));
    }

    @Test
    public void expectExceptionWhenReclosingAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new CloseAccountCommand("test-id"))
                .expectException(AccountStateChangeNotValidException.class);
    }

    @Test
    public void expectExceptionWhenActivatingClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new ActivateAccountCommand("test-id"))
                .expectException(AccountStateChangeNotValidException.class);
    }

    @Test
    public void expectExceptionWhenClosingAccountWithBalance() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AmountCreditedEvent("test-id", 100))
                .when(new CloseAccountCommand("test-id"))
                .expectException(AccountClosureNotValidException.class);
    }

    /*@Test
    public void expectExceptionWhenOpeningClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new OpenAccountCommand("test-id", "test-name"))
                .expectException(AccountStateChangeNotValidException.class);
    }*/

}
