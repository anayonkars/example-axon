package code.exampleaxon.accountdomain.web.vo;

import java.util.*;

public class AccountStatus {
    public static final String ACCOUNT_STATUS_OPEN = "OPEN";
    public static final String ACCOUNT_STATUS_ACTIVE = "ACTIVE";
    public static final String ACCOUNT_STATUS_CLOSE = "CLOSE";

    private static final List<String> accountLifeCycle = new ArrayList<>
            (Arrays.asList(ACCOUNT_STATUS_OPEN, ACCOUNT_STATUS_ACTIVE,
                    ACCOUNT_STATUS_CLOSE));

    public static boolean isValidChange(String currentStatus, String
            nextStatus) {
        int currentIndex = -1;
        int nextIndex = -1;
        if(currentStatus != null && nextStatus != null) {
            currentIndex = accountLifeCycle.indexOf(currentStatus);
            nextIndex = accountLifeCycle.indexOf(nextStatus);
        }
        return currentIndex != -1
                && nextIndex != -1
                && currentIndex < nextIndex;
    }
}