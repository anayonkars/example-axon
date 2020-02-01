package code.exampleaxon.accountdomain.integration;

import code.exampleaxon.accountdomain.AccountDomain;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountDomain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTest {
    @Test
    public void noOpTest() {
        //no-op test
    }
}
