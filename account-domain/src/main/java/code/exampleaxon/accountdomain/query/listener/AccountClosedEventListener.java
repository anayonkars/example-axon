package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AccountClosedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static code.exampleaxon.accountdomain.command.domain.AccountStatus.CLOSE;

@Component
public class AccountClosedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AccountClosedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AccountClosedEvent event) {
        AccountView accountView = accountViewRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalStateException("Account not found: " + event.getId()));
        accountView.setStatus(CLOSE);
        accountViewRepository.save(accountView);
    }
}
