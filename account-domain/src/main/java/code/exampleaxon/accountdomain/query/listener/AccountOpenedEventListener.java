package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountOpenedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static code.exampleaxon.accountdomain.web.vo.AccountStatus.ACCOUNT_STATUS_OPEN;

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
        accountView.setStatus(ACCOUNT_STATUS_OPEN);
        accountViewRepository.save(accountView);
    }
}
