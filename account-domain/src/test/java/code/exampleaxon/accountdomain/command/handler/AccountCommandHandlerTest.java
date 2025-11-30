package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.*;
import code.exampleaxon.accountdomain.command.domain.Account;
import code.exampleaxon.accountdomain.command.event.*;
import code.exampleaxon.accountdomain.exception.*;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

public class AccountCommandHandlerTest {
    private static final String TEST_ID = "test-id";
    private static final String TEST_NAME = "test-name";
    private FixtureConfiguration<Account> fixtureConfiguration;
    private EventSourcingRepository<Account> eventSourcingRepository;

    @Before
    public void setup() {
        fixtureConfiguration = new AggregateTestFixture<>(Account.class);
        eventSourcingRepository = EventSourcingRepository.builder(Account.class)
                .eventStore(fixtureConfiguration.getEventStore())
                .build();
        fixtureConfiguration.registerRepository(eventSourcingRepository);
        fixtureConfiguration
                .registerAnnotatedCommandHandler(new ActivateAccountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new CloseAccountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new DebitAmountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new CreditAmountCommandHandler(eventSourcingRepository));
        fixtureConfiguration.registerAnnotatedCommandHandler(new OpenAccountCommandHandler(eventSourcingRepository));
    }

    @Test
    public void expectAccountActivation() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME))
                .when(new ActivateAccountCommand(TEST_ID))
                .expectEvents(new AccountActivatedEvent(TEST_ID));
    }

    @Test
    public void expectExceptionWhenReactivatingAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME), new AccountActivatedEvent(TEST_ID))
                .when(new ActivateAccountCommand(TEST_ID))
                .expectException(AccountStateChangeNotValidException.class);
    }

    @Test
    public void expectAccountClosure() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME), new AccountActivatedEvent(TEST_ID))
                .when(new CloseAccountCommand(TEST_ID))
                .expectEvents(new AccountClosedEvent(TEST_ID));
    }

    @Test
    public void expectExceptionWhenReclosingAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AccountClosedEvent(TEST_ID))
                .when(new CloseAccountCommand(TEST_ID))
                .expectException(AccountStateChangeNotValidException.class);
    }

    @Test
    public void expectExceptionWhenActivatingClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AccountClosedEvent(TEST_ID))
                .when(new ActivateAccountCommand(TEST_ID))
                .expectException(AccountStateChangeNotValidException.class);
    }

    @Test
    public void expectExceptionWhenClosingAccountWithBalance() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AmountCreditedEvent(TEST_ID, 100))
                .when(new CloseAccountCommand(TEST_ID))
                .expectException(AccountClosureNotValidException.class);
    }

    @Test
    public void expectCreditOperation() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME), new AccountActivatedEvent(TEST_ID))
                .when(new CreditAmountCommand(TEST_ID, 100))
                .expectEvents(new AmountCreditedEvent(TEST_ID, 100));
    }

    @Test
    public void expectExceptionForCreditOperationForNonActivatedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME))
                .when(new CreditAmountCommand(TEST_ID, 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForCreditOperationForClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AccountClosedEvent(TEST_ID))
                .when(new CreditAmountCommand(TEST_ID, 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForCreditOperationForInvalidAmount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID))
                .when(new CreditAmountCommand(TEST_ID, -100))
                .expectException(InvalidAmountException.class);
    }

    @Test
    public void expectDebitOperation() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AmountCreditedEvent(TEST_ID, 100))
                .when(new DebitAmountCommand(TEST_ID, 100))
                .expectEvents(new AmountDebitedEvent(TEST_ID, 100));
    }

    @Test
    public void expectExceptionForDebitOperationForNonActivatedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME))
                .when(new DebitAmountCommand(TEST_ID, 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForDebitOperationForClosedAccount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AccountClosedEvent(TEST_ID))
                .when(new DebitAmountCommand(TEST_ID, 100))
                .expectException(AccountOperationNotPossibleException.class);
    }

    @Test
    public void expectExceptionForDebitOperationForInvalidAmount() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME), new AccountActivatedEvent(TEST_ID))
                .when(new DebitAmountCommand(TEST_ID, -100))
                .expectException(InvalidAmountException.class);
    }

    @Test
    public void expectInsufficientBalanceException() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AmountCreditedEvent(TEST_ID, 100))
                .when(new DebitAmountCommand(TEST_ID, 200))
                .expectException(InsufficientBalanceException.class);
    }

    @Test
    public void expectSuccessfulClosure() {
        fixtureConfiguration.given(new AccountOpenedEvent(TEST_ID, TEST_NAME),
                new AccountActivatedEvent(TEST_ID),
                new AmountCreditedEvent(TEST_ID, 100),
                new AmountDebitedEvent(TEST_ID, 100))
                .when(new CloseAccountCommand(TEST_ID))
                .expectEvents(new AccountClosedEvent(TEST_ID));
    }

    @Test
    public void expectAccountOpenedEvent() {
        fixtureConfiguration.given()
                .when(new OpenAccountCommand(TEST_ID, TEST_NAME))
                .expectEvents(new AccountOpenedEvent(TEST_ID, TEST_NAME));
    }

}
