package code.exampleaxon.accountdomain.query.repository;

import code.exampleaxon.accountdomain.query.view.AccountView;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountViewRepository extends PagingAndSortingRepository<AccountView, String> {
}
