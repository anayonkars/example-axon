package code.exampleaxon.accountdomain.web.vo;

import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;
import org.junit.Test;

public class AccountStatusTransitionTest {

    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidCurrentStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", "INVALID", AccountStatus.ACCOUNT_STATUS_OPEN);
    }

    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidNextStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", AccountStatus.ACCOUNT_STATUS_OPEN, "UNKNOWN");
    }

    @Test(expected = AccountStateChangeNotValidException.class)
    public void backwardTransitionShouldThrow() {
        AccountStatus.validateAccountStateChange("id", AccountStatus.ACCOUNT_STATUS_ACTIVE, AccountStatus.ACCOUNT_STATUS_OPEN);
    }
}
