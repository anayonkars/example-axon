package code.exampleaxon.accountdomain.query.listener;

import code.exampleaxon.accountdomain.command.event.AmountDebitedEvent;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmountDebitedEventListener {
    private final AccountViewRepository accountViewRepository;

    @Autowired
    public AmountDebitedEventListener(AccountViewRepository accountViewRepository) {
        this.accountViewRepository = accountViewRepository;
    }

    @EventHandler
    public void on(AmountDebitedEvent event) {
        AccountView accountView = accountViewRepository.findOne(event.getId());
        int balance = accountView.getBalance() - event.getAmount();
        accountView.setBalance(balance);
        accountViewRepository.save(accountView);
    }
}