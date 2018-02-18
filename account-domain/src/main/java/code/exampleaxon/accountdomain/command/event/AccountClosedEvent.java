package code.exampleaxon.accountdomain.command.event;

public class AccountClosedEvent {
    private String id;

    public AccountClosedEvent() {
    }

    public AccountClosedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
