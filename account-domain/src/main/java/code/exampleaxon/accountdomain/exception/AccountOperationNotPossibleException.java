package code.exampleaxon.accountdomain.exception;

public class AccountOperationNotPossibleException extends RuntimeException {
    private String id;
    private String status;

    public AccountOperationNotPossibleException(String id, String status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return "Operation not possible on account " + id + " with status " +
                status;
    }
}
