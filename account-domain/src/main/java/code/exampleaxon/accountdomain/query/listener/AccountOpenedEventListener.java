package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static code.exampleaxon.accountdomain.command.domain.AccountStatus.OPEN;

@Component
public class AccountOpenedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountOpenedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountOpenedEvent event) {
        AccountView accountView = new AccountView(event.getId(), event.getName());
        accountView.setBalance(0);
        accountView.setStatus(OPEN);
        accountViewRepository.save(accountView);
    }
}
