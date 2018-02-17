package code.exampleaxon.accountdomain.web.command;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class CommandController {
    private final IdentifierFactory identifierFactory = IdentifierFactory.getInstance();

    private final CommandGateway commandGateway;

    @Autowired
    public CommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping(value = "open/{name}", method = RequestMethod.POST)
    public String test(@PathVariable String name) {
        String id = identifierFactory.generateIdentifier();
        commandGateway.send(new OpenAccountCommand(id, name));
        return id;
    }
}
