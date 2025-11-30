package code.exampleaxon.accountdomain.command.domain;

import code.exampleaxon.accountdomain.command.event.*;
import code.exampleaxon.accountdomain.exception.AccountClosureNotValidException;
import code.exampleaxon.accountdomain.exception.AccountOperationNotPossibleException;
import code.exampleaxon.accountdomain.exception.InsufficientBalanceException;
import code.exampleaxon.accountdomain.exception.InvalidAmountException;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;

import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;

import static code.exampleaxon.accountdomain.command.domain.AccountStatus.*;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Entity
@AggregateRoot
@Aggregate
public class Account {

    @Id
    @AggregateIdentifier
    private String id;
    private String name;
    private int balance;
    private AccountStatus status;

    public Account() {
    }

    public Account(String id, String name) {
        apply(new AccountOpenedEvent(id, name));
    }

    public void activate() {
        validateAccountStateChange(this.id, this.status, ACTIVE);
        apply(new AccountActivatedEvent(this.id));
    }

    public void close() {
        validateAccountStateChange(this.id, this.status, CLOSE);
        validateEligibilityForClosure();
        apply(new AccountClosedEvent(this.id));
    }

    public void credit(int amount) {
        validateCreditOperation(amount);
        apply(new AmountCreditedEvent(this.id, amount));
    }

    public void debit(int amount) {
        validateDebitOperation(amount);
        apply(new AmountDebitedEvent(this.id, amount));
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.balance = 0;
        this.status = OPEN;
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        this.status = ACTIVE;
    }

    @EventSourcingHandler
    public void on(AccountClosedEvent event) {
        this.status = CLOSE;
    }

    @EventSourcingHandler
    public void on(AmountCreditedEvent event) {
        this.balance += event.getAmount();
    }

    @EventSourcingHandler
    public void on(AmountDebitedEvent event) {
        this.balance -= event.getAmount();
    }

    private void validateEligibilityForClosure() {
        if (balance != 0) {
            throw new AccountClosureNotValidException(id, balance);
        }
    }

    private void validateEligibilityForOperation() {
        if (status != ACTIVE) {
            throw new AccountOperationNotPossibleException(id, status.name());
        }
    }

    private void validateAmount(int amount) {
        if (amount < 0) {
            throw new InvalidAmountException(id, amount);
        }
    }

    private void validateAmountForDebit(int amount) {
        if (amount > balance) {
            throw new InsufficientBalanceException(id, balance);
        }
    }

    private void validateCreditOperation(int amount) {
        validateEligibilityForOperation();
        validateAmount(amount);
    }

    private void validateDebitOperation(int amount) {
        validateEligibilityForOperation();
        validateAmount(amount);
        validateAmountForDebit(amount);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
