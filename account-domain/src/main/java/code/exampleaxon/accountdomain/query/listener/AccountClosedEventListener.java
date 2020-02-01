package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountClosedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static code.exampleaxon.accountdomain.web.vo.AccountStatus.ACCOUNT_STATUS_CLOSE;

@Component
public class AccountClosedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountClosedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountClosedEvent event) {
        AccountView accountView = accountViewRepository.findById(event.getId()).get();
        accountView.setStatus(ACCOUNT_STATUS_CLOSE);
        accountViewRepository.save(accountView);
    }
}
