package code.exampleaxon.accountdomain.web.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class CreditAmountRequest {
    @NotBlank(message = "Account ID is required")
    private String id;

    @Positive(message = "Amount must be positive")
    private int amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
