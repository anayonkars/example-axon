package code.exampleaxon.accountdomain.query.view;


import code.exampleaxon.accountdomain.command.domain.AccountStatus;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class AccountView {
    @Id
    private String id;
    private String name;
    private int balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public AccountView() {
    }

    public AccountView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
