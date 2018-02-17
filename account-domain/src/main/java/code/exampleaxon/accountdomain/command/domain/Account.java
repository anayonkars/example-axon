package code.exampleaxon.accountdomain.command.domain;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.axonframework.eventsourcing.annotation.EventSourcingHandler;

import javax.persistence.Id;

public class Account extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    @Id
    private String id;
    private String name;
    private int balance;
    private String status;

    public Account() {
    }

    public Account(OpenAccountCommand openAccountCommand) {
        apply(new AccountOpenedEvent(openAccountCommand.getId(),
                openAccountCommand.getName()));
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
