package rest.api.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.my.webservice.entity.Role;
import com.my.webservice.entity.RoleEntity;
import com.my.webservice.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rest.api.repository.RoleRepository;
import rest.api.repository.UserRepository;
import rest.api.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

  @InjectMocks private UserService userService;
  @Mock private UserRepository userRepository;
  @Mock private RoleRepository roleRepository;
  @Mock private BCryptPasswordEncoder passwordEncoder;
  @Captor private ArgumentCaptor<User> userArgumentCaptor;

  @Test
  public void shouldSaveUser() {
    final String encodedPassw = RandomStringUtils.randomAlphabetic(10);
    final RoleEntity userRole = RoleEntity.builder().id(1L).roleName(Role.USER.name()).build();
    User user =
        User.builder()
            .userName("userName")
            .password(RandomStringUtils.randomAlphabetic(10))
            .isActive(true)
            .build();
    when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassw);
    when(roleRepository.findByRoleName(Role.USER.name())).thenReturn(userRole);
    userService.saveUser(user);

    verify(userRepository).save(userArgumentCaptor.capture());
    User savedUser = userArgumentCaptor.getValue();
    assertThat(savedUser.getPassword()).isEqualTo(encodedPassw);
    assertThat(savedUser.getRole()).isEqualTo(userRole);
    assertThat(savedUser.getRegisteredDateTime()).isNotNull();
  }
}
