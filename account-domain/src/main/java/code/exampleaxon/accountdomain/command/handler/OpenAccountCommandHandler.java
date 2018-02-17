package code.exampleaxon.accountdomain.command.handler;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.command.domain.Account;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenAccountCommandHandler {
    @Autowired
    private AxonConfiguration axonConfiguration;

    @CommandHandler
    public void handle(OpenAccountCommand command) {
        try {
            axonConfiguration.repository(Account.class).newInstance(() -> new
                    Account(command.getId(), command.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
