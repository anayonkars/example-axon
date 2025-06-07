package code.exampleaxon.accountdomain.command.domain;

import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;
import org.junit.Test;

public class AccountStatusTransitionTest {

    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidCurrentStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", null, AccountStatus.OPEN);
    }

    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidNextStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", AccountStatus.OPEN, null);
    }

    @Test(expected = AccountStateChangeNotValidException.class)
    public void backwardTransitionShouldThrow() {
        AccountStatus.validateAccountStateChange("id", AccountStatus.ACTIVE, AccountStatus.OPEN);
    }
}
