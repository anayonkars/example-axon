package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.CreditAmountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditAmountCommandHandler {
    private EventSourcingRepository<Account> repository;

    @Autowired
    public CreditAmountCommandHandler(EventSourcingRepository<Account> repository) {
        this.repository = repository;
    }

    @CommandHandler
    public void handle(CreditAmountCommand command) {
        Optional.of(repository.load(command.getId())).ifPresent(account ->
                account.credit(command.getAmount()));
    }
}
