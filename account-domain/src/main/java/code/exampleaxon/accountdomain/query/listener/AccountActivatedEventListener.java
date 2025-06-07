package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountActivatedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static code.exampleaxon.accountdomain.command.domain.AccountStatus.ACTIVE;

@Component
public class AccountActivatedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountActivatedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountActivatedEvent event) {
        AccountView accountView = accountViewRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalStateException("Account not found: " + event.getId()));
        accountView.setStatus(ACTIVE);
        accountViewRepository.save(accountView);
    }
}
