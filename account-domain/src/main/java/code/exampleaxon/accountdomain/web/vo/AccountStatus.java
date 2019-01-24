package code.exampleaxon.accountdomain.web.vo;

import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountStatus {
    public static final String ACCOUNT_STATUS_OPEN = "OPEN";
    public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
    public static final String ACCOUNT_STATUS_CLOSE = "CLOSE";

    private static final List<String> accountLifeCycle = new ArrayList<>(Arrays.asList(
                                                                            ACCOUNT_STATUS_OPEN,
                                                                            ACCOUNT_STATUS_ACTIVE,
                                                                            ACCOUNT_STATUS_CLOSE));

    private static boolean isValidChange(String currentStatus, String nextStatus) {
        return currentStatus != null
                && nextStatus != null
                && accountLifeCycle.indexOf(currentStatus) < accountLifeCycle.indexOf(nextStatus);
    }

    public static void validateAccountStateChange(String id, String currentStatus, String nextStatus) {
        if(!isValidChange(currentStatus, nextStatus)) {
            throw new AccountStateChangeNotValidException(id, currentStatus,
                    nextStatus);
        }
    }
}
