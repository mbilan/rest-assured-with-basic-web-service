package tests.com.my.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@DisplayName("Testing user access to admin/public resources:")
// new test instance will be created once per test class
@TestInstance(Lifecycle.PER_CLASS)
public class AccessApiTest extends BaseTest{

    private String adminUser;
    private String simpleUser;
    @BeforeAll
    public void beforeAll(){

    }
}
