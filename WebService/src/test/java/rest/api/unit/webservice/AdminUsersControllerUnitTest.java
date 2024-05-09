package rest.api.unit.webservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rest.api.util.LocationBuilder.generateLocation;

import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import com.my.webservice.validation.ValidationError;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rest.api.service.UserService;
import rest.api.webservice.AdminUsersController;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdminUsersController should")
public class AdminUsersControllerUnitTest {
  @InjectMocks private AdminUsersController adminUsersController;
  @Mock private UserService userService;

  private static MockHttpServletRequest request;
  private static User user;

  @BeforeAll
  public static void beforeAll() {
    request = new MockHttpServletRequest();

    user =
        User.builder()
            .userName("testUserName")
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .build();
  }

  @Test
  @DisplayName("create user and add location header")
  public void shouldCreateUserWithLocation() {
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    when(userService.getUserByUsername(user.getUserName())).thenReturn(Optional.empty());
    when(userService.saveUser(user)).thenReturn(user);

    ResponseEntity<UserResponse> responseEntity = adminUsersController.createUser(user, request);
    // verify method saveUser() was called with argument user
    verify(userService).saveUser(user);
    // verify response to have 201 Created statusCode and appropriate body
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(responseEntity.getBody())
        .usingRecursiveComparison()
        .ignoringFields("id", "registeredTime")
        .isEqualTo(UserResponse.fromUser(user));

    assertThat(responseEntity.getHeaders().getLocation())
        .isNotNull()
        .isEqualTo(generateLocation(request, user.getUserName()));
  }

  @Test
  @DisplayName("assign role ADMIN to  user")
  public void shouldAssignAdminRoleToUser() {
    // RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    User userFromDB = user.toBuilder().id(10L).build();
    when(userService.getUserByUsername(user.getUserName())).thenReturn(Optional.of(userFromDB));

    ResponseEntity<UserResponse> responseEntity =
        adminUsersController.updateUserRole(user.getUserName(), Role.ADMIN.name());

    verify(userService).assignRoleToUser(user.getUserName(), Role.ADMIN.name());
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody())
        .usingRecursiveComparison()
        .isEqualTo(UserResponse.fromUser(userFromDB));
  }

  @Test
  @DisplayName("response with 409 on POST /users if user already exists")
  public void shouldResponseWith409ConflictOnPostUsers() {
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    when(userService.getUserByUsername(user.getUserName())).thenReturn(Optional.of(user));

    ResponseEntity<UserResponse> responseEntity = adminUsersController.createUser(user, request);
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(responseEntity.getBody()).isNotNull();
    assertThat(responseEntity.getBody().getErrorMessage())
        .isEqualTo(
            String.format(ValidationError.USER_ALREADY_EXIST.getErrorMsg(), user.getUserName()));
  }

  @Test
  @DisplayName("response with 404 when assign role if user DOES NOT exists")
  public void shouldResponseWith404NotFoundIfUserDoesNotExist() {
    when(userService.getUserByUsername(user.getUserName())).thenReturn(Optional.empty());

    ResponseEntity<UserResponse> responseEntity =
        adminUsersController.updateUserRole(user.getUserName(), Role.ADMIN.name());
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(responseEntity.getBody()).isNull();
  }
}
