package code.exampleaxon.accountdomain.command;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class ActivateAccountCommand {
    @TargetAggregateIdentifier
    private String id;

    public ActivateAccountCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
