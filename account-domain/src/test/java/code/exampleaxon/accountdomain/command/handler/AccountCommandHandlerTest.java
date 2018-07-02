package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.*;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.*;
import code.exampleaxon.accountdomain.exception.*;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.test.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.Fixtures.newGivenWhenThenFixture;

public class AccountCommandHandlerTest {
    private FixtureConfiguration<Account> fixtureConfiguration;
    private EventSourcingRepository<Account> eventSourcingRepository;

    @Before
    public void setup() {
        fixtureConfiguration = newGivenWhenThenFixture(Account.class);
        eventSourcingRepository = new EventSourcingRepository<>(Account.class, fixtureConfiguration.getEventStore());
        fixtureConfiguration.registerRepository(eventSourcingRepository);
        fixtureConfiguration.registerAnnotatedCommandHandler(new ActivateAccountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new CloseAccountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new DebitAmountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new CreditAmountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new OpenAccountCommandHandler(eventSourcingRepository));
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

    @Test
    public void expectCreditOperation() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"))
                .when(new CreditAmountCommand("test-id", 100))
                .expectEvents(new AmountCreditedEvent("test-id",
                        100));
    }

    @Test
    public void expectExceptionForCreditOperationForNonActivatedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"))
                .when(new CreditAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForCreditOperationForClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new CreditAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForCreditOperationForInvalidAmount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"))
                .when(new CreditAmountCommand("test-id", -100))
                .expectException(InvalidAmountException.class);
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
    public void expectExceptionForDebitOperationForNonActivatedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"))
                .when(new DebitAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForDebitOperationForClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent("test-id",
                "test-name"), new AccountActivatedEvent("test-id"), new
                AccountClosedEvent("test-id"))
                .when(new DebitAmountCommand("test-id", 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForDebitOperationForInvalidAmount() {
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

    @Test
    public void expectAccountOpenedEvent() {
        fixtureConfiguration.given()
                .when(new OpenAccountCommand("test-id",
                        "test-name"))
                .expectEvents(new AccountOpenedEvent("test-id",
                        "test-name"));
    }

}
