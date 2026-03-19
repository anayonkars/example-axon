package code.exampleaxon.accountdomain.web.controller;

import code.exampleaxon.accountdomain.command.*;
import code.exampleaxon.accountdomain.query.repository.AccountViewRepository;
import code.exampleaxon.accountdomain.query.view.AccountView;
import code.exampleaxon.accountdomain.web.request.*;
import code.exampleaxon.accountdomain.web.response.OpenAccountResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/account")
public class AccountController {
    private static final long COMMAND_TIMEOUT_SECONDS = 5;

    private final IdentifierFactory identifierFactory = IdentifierFactory.getInstance();

    private final CommandGateway commandGateway;
    private final AccountViewRepository repository;

    @Autowired
    public AccountController(CommandGateway commandGateway, AccountViewRepository repository) {
        this.commandGateway = commandGateway;
        this.repository = repository;
    }

    @PostMapping(value = "open",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public OpenAccountResponse openAccount(@Valid @RequestBody OpenAccountRequest request) {
        String id = identifierFactory.generateIdentifier();
        commandGateway.sendAndWait(new OpenAccountCommand(id, request.getName()), COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        return new OpenAccountResponse(id);
    }

    @PostMapping(value = "activate",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void activateAccount(@Valid @RequestBody ActivateAccountRequest request) {
        commandGateway.sendAndWait(new ActivateAccountCommand(request.getId()), COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @PostMapping(value = "close",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void closeAccount(@Valid @RequestBody CloseAccountRequest request) {
        commandGateway.sendAndWait(new CloseAccountCommand(request.getId()), COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @PostMapping(value = "credit",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void creditAmount(@Valid @RequestBody CreditAmountRequest request) {
        commandGateway.sendAndWait(new CreditAmountCommand(request.getId(), request.getAmount()), COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @PostMapping(value = "debit",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public void debitAmount(@Valid @RequestBody DebitAmountRequest request) {
        commandGateway.sendAndWait(new DebitAmountCommand(request.getId(), request.getAmount()), COMMAND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    @GetMapping(value = "get/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public AccountView getAccount(@PathVariable String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found: " + id));
    }
}
