package code.exampleaxon.accountdomain.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CloseAccountCommand {
    @TargetAggregateIdentifier
    private String id;

    public CloseAccountCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
