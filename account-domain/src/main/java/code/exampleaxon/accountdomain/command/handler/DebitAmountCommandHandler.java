package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.DebitAmountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DebitAmountCommandHandler {
    private EventSourcingRepository<Account> repository;

    @Autowired
    public DebitAmountCommandHandler(EventSourcingRepository<Account> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(DebitAmountCommand command) {
        repository.load(command.getId()).execute(account -> account.debit(command.getAmount()));
    }
}
