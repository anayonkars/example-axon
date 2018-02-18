package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountActivatedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import code.exampleaxon.accountdomain.web.vo.AccountStatus;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountActivatedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountActivatedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        AccountView accountView = accountViewRepository.findOne(event.getId());
        accountView.setStatus(AccountStatus.ACCOUNT_STATUS_ACTIVE);
        accountViewRepository.save(accountView);
    }
}
