package code.exampleaxon.accountdomain.command.event;

public class AccountActivatedEvent {
    private String id;

    public AccountActivatedEvent() {
    }

    public AccountActivatedEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
