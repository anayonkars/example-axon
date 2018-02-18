package code.exampleaxon.accountdomain.exception;

public class InsufficientBalanceException extends RuntimeException {
    private String id;
    private int balance;

    public InsufficientBalanceException(String id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    @Override
    public String getMessage() {
        return "Account " + id + " is having insufficient balance " + balance
                + " for operation";
    }
}
