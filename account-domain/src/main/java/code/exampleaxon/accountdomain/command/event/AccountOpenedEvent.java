package code.exampleaxon.accountdomain.command.event;

public class AccountOpenedEvent {
    private String id;
    private String name;

    public AccountOpenedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
