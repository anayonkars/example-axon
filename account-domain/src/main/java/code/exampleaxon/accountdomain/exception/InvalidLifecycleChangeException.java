package code.exampleaxon.accountdomain.exception;

public class InvalidLifecycleChangeException extends RuntimeException {
    private String currentStatus;
    private String nextStatus;

    public InvalidLifecycleChangeException(String currentStatus, String nextStatus) {
        this.currentStatus = currentStatus;
        this.nextStatus = nextStatus;
    }

    @Override
    public String getMessage() {
        return "Status " + currentStatus + " cannot be changed to " +
                nextStatus;
    }
}
