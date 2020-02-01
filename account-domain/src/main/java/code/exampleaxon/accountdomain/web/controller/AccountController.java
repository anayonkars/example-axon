package code.exampleaxon.accountdomain.web.controller;

import code.exampleaxon.accountdomain.command.*;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import code.exampleaxon.accountdomain.web.request.*;
import code.exampleaxon.accountdomain.web.response.OpenAccountResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final IdentifierFactory identifierFactory = IdentifierFactory.getInstance();

    private final CommandGateway commandGateway;
    private final AccountViewRepository repository;

    @Autowired
    public AccountController(CommandGateway commandGateway, AccountViewRepository repository) {
        this.commandGateway = commandGateway;
        this.repository = repository;
    }

    @PostMapping(value = "open",
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public OpenAccountResponse openAccount(@RequestBody OpenAccountRequest request) {
        String id = identifierFactory.generateIdentifier();
        commandGateway.sendAndWait(new OpenAccountCommand(id, request.getName()));
        OpenAccountResponse response = new OpenAccountResponse(id);
        return response;
    }

    @PostMapping(value = "activate",
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public void activateAccount(@RequestBody ActivateAccountRequest request) {
        commandGateway.sendAndWait(new ActivateAccountCommand(request.getId()));
    }

    @PostMapping(value = "close",
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public void closeAccount(@RequestBody CloseAccountRequest request) {
        commandGateway.sendAndWait(new CloseAccountCommand(request.getId()));
    }

    @PostMapping(value = "credit",
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public void creditAmount(@RequestBody CreditAmountRequest request) {
        commandGateway.sendAndWait(new CreditAmountCommand(request.getId(), request.getAmount()));
    }

    @PostMapping(value = "debit",
            consumes = {APPLICATION_JSON},
            produces = {APPLICATION_JSON})
    public void debitAmount(@RequestBody DebitAmountRequest request) {
        commandGateway.sendAndWait(new DebitAmountCommand(request.getId(), request.getAmount()));
    }

    @GetMapping(value = "get/{id}",
            produces = {APPLICATION_JSON})
    public AccountView getAccount(@PathVariable String id) {
        return repository.findById(id).orElse(null);
    }
}
