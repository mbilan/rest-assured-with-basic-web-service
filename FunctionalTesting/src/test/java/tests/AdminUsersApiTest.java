package tests;

import static com.my.application.api.AdminUsersClient.postUser;
import static com.my.application.api.AdminUsersClient.putUserRole;
import static com.my.application.api.PublicUsersClient.getUserByUserName;
import static org.assertj.core.api.Assertions.assertThat;

import com.my.application.api.RequestBuilder;
import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Admin creates new user")
public class AdminUsersApiTest extends BaseTest {

  @Autowired private RequestBuilder requestBuilder;
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
    User userResponse = postUser(requestBuilder.authorizedReqSpec(), user);

    assertThat(userResponse.getUserName()).isEqualTo(user.getUserName());
    assertThat(userResponse.getPassword()).isNull();
    assertThat(userResponse.getIsActive()).isEqualTo(user.getIsActive());
    assertThat(userResponse.getRole().getRoleName()).isEqualTo(Role.USER.name());
  }

  @Nested
  @DisplayName("and then ")
  class AdminRoleUpdateTest {

    @Test
    @DisplayName("his role is updated to ADMIN")
    public void shouldUpdateUserRole() {
      final RequestSpecification adminUserSpec = requestBuilder.authorizedReqSpec();
      UserResponse userResponse = putUserRole(adminUserSpec, userName, Role.ADMIN);
      assertThat(userResponse.getRole()).isEqualTo(Role.ADMIN.name());

      UserResponse getByUserNameResponse = getUserByUserName(adminUserSpec, userName);
      assertThat(getByUserNameResponse.getRole()).isEqualTo(Role.ADMIN.name());
    }
  }
}
