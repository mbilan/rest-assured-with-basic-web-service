package rest.api.unit.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static rest.api.validation.AuthValidation.validateUserConsistency;

import com.my.webservice.entity.Role;
import com.my.webservice.validation.ValidationError;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthValidationUnitTest {

  @Test
  public void shouldThrowExceptionIfUserDiffers() {
    final String userNameInPath = "testUser";
    UserDetails authUser = baseUser.authorities(Role.USER.name()).build();

    Throwable thrown = catchThrowable(() -> validateUserConsistency(userNameInPath, authUser));

    assertThat(thrown)
        .isInstanceOf(AccessDeniedException.class)
        .hasMessage(ValidationError.ACCESS_DENIED_FOR_REQUESTED_USER.getErrorMsg());
  }

  @Test
  public void shouldNotThrowExceptionIfUserDiffersButItIsAdmin() {
    final String userNameInPath = "testUser";
    UserDetails authUser = baseUser.authorities(Role.ADMIN.name()).build();
    validateUserConsistency(userNameInPath, authUser);
  }

  @Test
  public void shouldNotThrowExceptionIfUserTheSame() {
    final String userNameInPath = "testUser";
    UserDetails authUser = baseUser.username(userNameInPath).build();
    validateUserConsistency(userNameInPath, authUser);
  }

  private final User.UserBuilder baseUser =
      User.withUsername(RandomStringUtils.randomAlphabetic(6))
          .password("test")
          .authorities(Role.USER.name())
          .accountExpired(false)
          .accountLocked(false)
          .credentialsExpired(false)
          .disabled(false);
}
