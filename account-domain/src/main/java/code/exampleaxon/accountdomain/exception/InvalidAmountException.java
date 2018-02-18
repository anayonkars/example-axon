package code.exampleaxon.accountdomain.exception;

public class InvalidAmountException extends RuntimeException {
    private String id;
    private int amount;

    public InvalidAmountException(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return "Invalid amount " + amount + " for account " + id;
    }
}
