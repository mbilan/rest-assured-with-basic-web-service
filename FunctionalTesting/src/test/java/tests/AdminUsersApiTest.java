package tests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import configuration.TestConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import request.RequestBuilder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@SpringBootTest
@DisplayName("New user is created")
public class AdminUsersApiTest {

  @Autowired private RequestBuilder requestBuilder;
  private static String userName;

  @Test
  @DisplayName(" using POST /api/admin/users")
  public void shouldCreateUser() {

    User user =
        User.builder()
            .userName("testUser" + RandomStringUtils.randomNumeric(5))
            .password(RandomStringUtils.randomAlphabetic(10))
            .isActive(true)
            .build();
    userName = user.getUserName();
    User userResponse =
        given(requestBuilder.authorizedReqSpec())
            .body(user)
            .when()
            .post("/admin/users")
            .as(User.class);

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
      UserResponse userResponse =
          given(requestBuilder.authorizedReqSpec())
              .when()
              .put("/admin/users/{userName}/roles/{roleName}", userName, Role.ADMIN.name())
              .as(UserResponse.class);
      assertThat(userResponse.getRole()).isEqualTo(Role.ADMIN.name());

      UserResponse getByUserNameResponse =
          given(requestBuilder.authorizedReqSpec())
              .when()
              .get("/public/users/{userName}", userName)
              .as(UserResponse.class);
      assertThat(getByUserNameResponse.getRole()).isEqualTo(Role.ADMIN.name());
    }
  }
}
