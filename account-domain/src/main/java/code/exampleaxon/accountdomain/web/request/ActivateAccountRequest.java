package code.exampleaxon.accountdomain.web.request;

import javax.validation.constraints.NotBlank;

public class ActivateAccountRequest {
    @NotBlank(message = "Account ID is required")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
