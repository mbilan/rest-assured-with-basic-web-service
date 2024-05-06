package rest.api.service;

import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import java.sql.Timestamp;
import java.time.ZoneOffset;

import org.springframework.transaction.annotation.Transactional;
import rest.api.repository.RoleRepository;
import rest.api.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public User saveUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRegisteredDateTime(LocalDateTime.now(ZoneOffset.UTC));
    if (user.getRole() == null) {
      user.setRole(roleRepository.findByRoleName(Role.USER.name()));
    }
    if(user.getIsActive() == null){
      user.setIsActive(false);
    }
    return userRepository.save(user);
  }

  public void assignRoleToUser(String userName, String roleName) {

    userRepository.addRole(userName, roleName);

  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUserName(username);
  }

  public List<UserResponse> retrieveUsers() {
    return ((List<User>) userRepository.findAll())
        .stream().map(UserResponse::fromUser).collect(Collectors.toList());
  }
}
