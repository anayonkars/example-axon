package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.CreditAmountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditAmountCommandHandler {
    private EventSourcingRepository<Account> repository;

    @Autowired
    public CreditAmountCommandHandler(EventSourcingRepository<Account> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(CreditAmountCommand command) {
        repository.load(command.getId()).execute(account -> account.credit(command.getAmount()));
    }
}
