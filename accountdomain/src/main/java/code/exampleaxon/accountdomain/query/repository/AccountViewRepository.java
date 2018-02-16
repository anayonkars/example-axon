package code.exampleaxon.accountdomain.query.repository;

import code.exampleaxon.accountdomain.query.view.AccountView;
import org.springframework.data.repository.CrudRepository;

public interface AccountViewRepository extends CrudRepository<AccountView, String> {
}
