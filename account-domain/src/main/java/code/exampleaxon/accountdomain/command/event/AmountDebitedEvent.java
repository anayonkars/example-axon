package code.exampleaxon.accountdomain.command.event;

public class AmountDebitedEvent {
    private String id;
    private int amount;

    public AmountDebitedEvent() {
    }

    public AmountDebitedEvent(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
}
