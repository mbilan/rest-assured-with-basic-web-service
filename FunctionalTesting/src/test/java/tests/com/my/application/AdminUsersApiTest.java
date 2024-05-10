package tests.com.my.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.my.application.api.client.AdminUsersClient;
import com.my.application.api.request.CredentialsProvider;
import com.my.application.api.client.PublicUsersClient;
import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Admin creates new user")
public class AdminUsersApiTest extends BaseTest {

  @Autowired
  private AdminUsersClient adminUsersClient;
  @Autowired
  private PublicUsersClient publicUsersClient;
  @Autowired
  private CredentialsProvider credentialsProvider;

  private static String userName;

  @Test
  @DisplayName(" via POST /api/admin/users")
  public void shouldCreateUser() {

    User user =
        User.builder()
            .userName("testUser" + RandomStringUtils.randomNumeric(5))
            .password(RandomStringUtils.randomAlphabetic(10))
            .isActive(true)
            .build();
    userName = user.getUserName();
    User userResponse = adminUsersClient.postUser(user);

    // group assertions is used to see all failure reported if they are present
    assertAll(
        "userResponse",
        () -> assertThat(userResponse.getUserName()).isEqualTo(user.getUserName()),
        () -> assertThat(userResponse.getUserName()).isEqualTo(user.getUserName()),
        () -> assertThat(userResponse.getPassword()).isNull(),
        () -> assertThat(userResponse.getIsActive()).isEqualTo(user.getIsActive()),
        () -> assertThat(userResponse.getRole().getRoleName()).isEqualTo(Role.USER.name()));
  }

  @Nested
  @DisplayName("and then ")
  class AdminRoleUpdateTest {

    @Test
    @DisplayName("his role is updated to ADMIN")
    public void shouldUpdateUserRole() {
      UserResponse userResponse = adminUsersClient.putUserRole(userName, Role.ADMIN);
      assertThat(userResponse.getRole()).isEqualTo(Role.ADMIN.name());

      UserResponse getByUserNameResponse = publicUsersClient.getUserByUserName(credentialsProvider.getDefaultAdminCredentials(),userName);
      assertThat(getByUserNameResponse.getRole()).isEqualTo(Role.ADMIN.name());
    }
  }
}
