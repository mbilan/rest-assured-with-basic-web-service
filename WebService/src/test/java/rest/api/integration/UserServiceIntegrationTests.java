package rest.api.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import rest.api.repository.UserRepository;
import rest.api.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Log4j2
@DisplayName("Should save user: ")
public class UserServiceIntegrationTests {

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private BCryptPasswordEncoder passwordEncoder;

  private User user;

  @BeforeEach
  public void beforeEach() {
    user =
        User.builder()
            .userName(RandomStringUtils.randomAlphabetic(5))
            .password(RandomStringUtils.randomAlphabetic(5))
            .isActive(true)
            .build();
  }

  @AfterEach
  public void afterEach() {
    userRepository.removeUserByUserName(user.getUserName());
  }

  @Test
  @DisplayName("with encrypted password")
  public void shouldSaveUserAndEncryptPassword() throws NotFoundException {
    final String password = user.getPassword();
    userService.saveUser(user);
    User createdUser =
        userService.getUserByUsername(user.getUserName()).orElseThrow(NotFoundException::new);
    // verify date was generated
    assertThat(createdUser.getRegisteredDateTime())
        .isCloseTo(LocalDateTime.now(ZoneOffset.UTC), within(1, ChronoUnit.SECONDS));
    // verify password was saved and not equal initial pswd
    assertThat(createdUser.getPassword()).isNotBlank().isNotEqualTo(password);
    // verify encoded paswd matches used provided password
    assertThat(passwordEncoder.matches(password, createdUser.getPassword())).isTrue();

    assertThat(createdUser.getIsActive()).isEqualTo(user.getIsActive());
    // if role is empty, default role should be USER
    assertThat(createdUser.getRole().getRoleName()).isEqualTo(Role.USER.name());
  }

  @Test
  @DisplayName("with basic USER role and IsActive=false if not provided")
  public void shouldSaveUserWithBasicRoleAndIsActiveFalse() throws NotFoundException {

    user.setRole(null);
    user.setIsActive(null);
    userService.saveUser(user);
    User createdUser =
        userService.getUserByUsername(user.getUserName()).orElseThrow(NotFoundException::new);

    // verify isActive is false by default
    assertThat(createdUser.getIsActive()).isFalse();
    // default role should be USER
    assertThat(createdUser.getRole().getRoleName()).isEqualTo(Role.USER.name());
  }

  @Test
  @DisplayName("and assign new role to user")
  public void shouldAssignRoleToUser() throws NotFoundException {
    userService.saveUser(user);
    userService.assignRoleToUser(user.getUserName(), Role.ADMIN.name());
    User userFromDB =
        userService.getUserByUsername(user.getUserName()).orElseThrow(NotFoundException::new);
    assertThat(userFromDB.getRole().getRoleName()).isEqualTo(Role.ADMIN.name());
  }
}
