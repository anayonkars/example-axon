package code.exampleaxon.accountdomain.command;

public class OpenAccountCommand {
    private String id;
    private String name;

    public OpenAccountCommand(String id, String name) {
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
