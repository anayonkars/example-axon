package code.exampleaxon.accountdomain.web.vo;

import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;
import org.junit.Test;

public class AccountStatusTest {
    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidCurrentStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", "BAD", AccountStatus.ACCOUNT_STATUS_OPEN);
    }

    @Test(expected = AccountStateChangeNotValidException.class)
    public void invalidNextStatusShouldThrow() {
        AccountStatus.validateAccountStateChange("id", AccountStatus.ACCOUNT_STATUS_OPEN, "BAD");
    }
}
