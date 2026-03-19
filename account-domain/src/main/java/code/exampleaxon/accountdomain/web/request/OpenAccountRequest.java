package code.exampleaxon.accountdomain.web.request;

import javax.validation.constraints.NotBlank;

public class OpenAccountRequest {
    @NotBlank(message = "Account name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
