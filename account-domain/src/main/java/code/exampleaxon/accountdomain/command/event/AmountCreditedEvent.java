package code.exampleaxon.accountdomain.command.event;

public class AmountCreditedEvent {
    private String id;
    private int amount;

    public AmountCreditedEvent() {
    }

    public AmountCreditedEvent(String id, int amount) {
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
