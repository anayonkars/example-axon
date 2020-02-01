package code.exampleaxon.accountdomain.command;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class DebitAmountCommand {
    @TargetAggregateIdentifier
    private String id;
    private int amount;

    public DebitAmountCommand(String id, int amount) {
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
