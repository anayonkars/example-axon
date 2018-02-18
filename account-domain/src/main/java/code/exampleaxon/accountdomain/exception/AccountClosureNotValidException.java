package code.exampleaxon.accountdomain.exception;

public class AccountClosureNotValidException  extends RuntimeException {
    private String id;
    private int balance;

    public AccountClosureNotValidException(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    @Override
    public String getMessage() {
        return "Unable to close account " + id + " with balance " + balance;
    }
}
