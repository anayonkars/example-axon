package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AmountCreditedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmountCreditedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AmountCreditedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AmountCreditedEvent event) {
        AccountView accountView = accountViewRepository.findById(event.getId())
                .orElseThrow(() -> new IllegalStateException("Account not found: " + event.getId()));
        int balance = accountView.getBalance() + event.getAmount();
        accountView.setBalance(balance);
        accountViewRepository.save(accountView);
    }
}
