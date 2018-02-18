package code.exampleaxon.accountdomain.web.command;

import code.exampleaxon.accountdomain.command.*;
import code.exampleaxon.accountdomain.web.request.*;
import code.exampleaxon.accountdomain.web.response.OpenAccountResponse;
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

    @RequestMapping(value = "open",
                    method = RequestMethod.POST,
                    consumes = {MediaType.APPLICATION_JSON},
                    produces = {MediaType.APPLICATION_JSON})
    public OpenAccountResponse openAccount(@RequestBody OpenAccountRequest
                                                   request) {
        String id = identifierFactory.generateIdentifier();
        commandGateway.sendAndWait(new OpenAccountCommand(id, request
               .getName()));
        OpenAccountResponse response = new OpenAccountResponse(id);
        return response;
    }

    @RequestMapping(value = "activate",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON},
            produces = {MediaType.APPLICATION_JSON})
    public void activateAccount(@RequestBody
                                                   ActivateAccountRequest
                                                               request) {
        commandGateway.sendAndWait(new ActivateAccountCommand(request.getId()));
    }

    @RequestMapping(value = "close",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON},
            produces = {MediaType.APPLICATION_JSON})
    public void closeAccount(@RequestBody
                                     CloseAccountRequest
                                        request) {
        commandGateway.sendAndWait(new CloseAccountCommand(request.getId()));
    }

    @RequestMapping(value = "credit",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON},
            produces = {MediaType.APPLICATION_JSON})
    public void creditAmount(@RequestBody
                                     CreditAmountRequest
                                     request) {
        commandGateway.sendAndWait(new CreditAmountCommand(request.getId(),
                request.getAmount()));
    }

    @RequestMapping(value = "debit",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON},
            produces = {MediaType.APPLICATION_JSON})
    public void debitAmount(@RequestBody
                                    DebitAmountRequest
                                     request) {
        commandGateway.sendAndWait(new DebitAmountCommand(request.getId(),
                request.getAmount()));
    }
}
