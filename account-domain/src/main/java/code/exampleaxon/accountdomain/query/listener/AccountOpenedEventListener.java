package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountOpenedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountOpenedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountOpenedEvent event) {
        AccountView accountView = new AccountView(event.getId(), event
                .getName());
        accountViewRepository.save(accountView);
    }
}
