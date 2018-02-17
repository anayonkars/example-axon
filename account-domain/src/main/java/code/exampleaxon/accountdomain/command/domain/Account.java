package code.exampleaxon.accountdomain.command.domain;

import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.eventsourcing.EventSourcingHandler;

import javax.persistence.Entity;
import javax.persistence.Id;

@AggregateRoot
public class Account {
    @AggregateIdentifier
    @Id
    private String id;
    private String name;
    private int balance;
    private String status;


    public Account(String id, String name) {
        AggregateLifecycle.apply(new AccountOpenedEvent(id, name));
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
