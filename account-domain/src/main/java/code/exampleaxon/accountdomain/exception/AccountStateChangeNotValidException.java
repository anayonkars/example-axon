package code.exampleaxon.accountdomain.exception;

public class AccountStateChangeNotValidException extends RuntimeException {
    private String id;
    private String currentStatus;
    private String nextStatus;

    public AccountStateChangeNotValidException(String id, String currentStatus,
                                               String nextStatus) {
        this.id = id;
        this.currentStatus = currentStatus;
        this.nextStatus = nextStatus;
    }

    @Override
    public String getMessage() {
        return "For id " + id + " status " + currentStatus + " cannot be " +
                "changed to " + nextStatus;
    }
}
