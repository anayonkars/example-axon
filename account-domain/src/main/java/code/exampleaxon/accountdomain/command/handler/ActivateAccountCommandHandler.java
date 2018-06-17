package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.ActivateAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Optional.of;

@Component
public class ActivateAccountCommandHandler {
    private EventSourcingRepository<Account> repository;

    @Autowired
    public ActivateAccountCommandHandler(EventSourcingRepository<Account> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(ActivateAccountCommand command) {
        of(repository.load(command.getId()))
                .ifPresent(account -> account.activate());
    }
}
