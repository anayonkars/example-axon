package code.exampleaxon.accountdomain.web.command;

import code.exampleaxon.accountdomain.command.OpenAccountCommand;
import code.exampleaxon.accountdomain.web.request.OpenAccountRequest;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/account")
public class CommandController {
    private final IdentifierFactory identifierFactory = IdentifierFactory.getInstance();

    private final CommandGateway commandGateway;

    @Autowired
    public CommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping(value = "open", method = RequestMethod.POST, consumes =
            {MediaType.APPLICATION_JSON})
    public String test(@RequestBody OpenAccountRequest request) {
        String id = identifierFactory.generateIdentifier();
        commandGateway.sendAndWait(new OpenAccountCommand(id, request
               .getName()));
        return id;
    }
}
