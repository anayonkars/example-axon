package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenAccountCommandHandler {
    private EventSourcingRepository<Account> repository;

    @Autowired
    public OpenAccountCommandHandler(EventSourcingRepository<Account> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(OpenAccountCommand command) {
        repository.add(new Account(command));
    }
}
