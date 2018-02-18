package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.CloseAccountCommand;
import code.exampleaxon.accountdomain.command.CreditAmountCommand;
import code.exampleaxon.accountdomain.command.DebitAmountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.*;
import code.exampleaxon.accountdomain.exception.AccountOperationNotPossibleException;
import code.exampleaxon.accountdomain.exception.InsufficientBalanceException;
import code.exampleaxon.accountdomain.exception.InvalidAmountException;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.test.FixtureConfiguration;
import org.axonframework.test.Fixtures;
import org.junit.Before;
import org.junit.Test;

public class DebitAmountCommandHandlerTest {
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
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                CreditAmountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new
                DebitAmountCommandHandler(eventSourcingRepository));
        /*fixtureConfiguration.registerAnnotatedCommandHandler(new
                OpenAccountCommandHandler(eventSourcingRepository));*/
        //fixtureConfiguration.setReportIllegalStateChange(false);
    }

    @Test
    public void expectDebitOperation() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AmountCreditedEvent("test-id", 100))
                .when(new DebitAmountCommand("test-id", 100))
                .expectEvents(new AmountDebitedEvent("test-id", 100));
    }

    @Test
    public void expectExceptionForNonActivatedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"))
                .when(new DebitAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new DebitAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForInvalidAmount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"))
                .when(new DebitAmountCommand("test-id", -100))
                .expectException(InvalidAmountException.class);
    }

    @Test
    public void expectInsufficientBalanceException() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"),
                new AmountCreditedEvent("test-id", 100))
                .when(new DebitAmountCommand("test-id", 200))
                .expectException(InsufficientBalanceException.class);
    }

    @Test
    public void expectSuccessfulClosure() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"),
                new AmountCreditedEvent("test-id", 100), new
                        AmountDebitedEvent("test-id", 100))
                .when(new CloseAccountCommand("test-id"))
                .expectEvents(new AccountClosedEvent("test-id"));
    }
}
