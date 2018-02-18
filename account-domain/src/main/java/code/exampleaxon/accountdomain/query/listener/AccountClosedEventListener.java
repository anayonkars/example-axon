package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountClosedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import code.exampleaxon.accountdomain.web.vo.AccountStatus;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountClosedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountClosedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountClosedEvent event) {
        AccountView accountView = accountViewRepository.findOne(event.getId());
        accountView.setStatus(AccountStatus.ACCOUNT_STATUS_CLOSE);
        accountViewRepository.save(accountView);
    }
}
