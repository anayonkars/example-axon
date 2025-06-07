package code.exampleaxon.accountdomain.command.domain;

import code.exampleaxon.accountdomain.exception.AccountStateChangeNotValidException;

import java.util.Arrays;
import java.util.List;

public enum AccountStatus {
    OPEN,
    ACTIVE,
    CLOSE;

    private static final List<AccountStatus> LIFE_CYCLE = Arrays.asList(OPEN, ACTIVE, CLOSE);

    private static boolean isValidChange(AccountStatus currentStatus, AccountStatus nextStatus) {
        return currentStatus != null &&
                nextStatus != null &&
                LIFE_CYCLE.contains(currentStatus) &&
                LIFE_CYCLE.contains(nextStatus) &&
                LIFE_CYCLE.indexOf(currentStatus) < LIFE_CYCLE.indexOf(nextStatus);
    }

    public static void validateAccountStateChange(String id, AccountStatus currentStatus, AccountStatus nextStatus) {
        if (!isValidChange(currentStatus, nextStatus)) {
            throw new AccountStateChangeNotValidException(id,
                    currentStatus != null ? currentStatus.name() : null,
                    nextStatus != null ? nextStatus.name() : null);
        }
    }
}
