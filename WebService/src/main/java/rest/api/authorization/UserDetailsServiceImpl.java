package rest.api.authorization;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rest.api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<com.my.webservice.entity.User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      return User.withUsername(user.get().getUserName())
          .password(user.get().getPassword())
          .authorities(user.get().getRole().getRoleName())
          .accountExpired(false)
          .accountLocked(false)
          .credentialsExpired(false)
          .disabled(!user.get().getIsActive())
          .build();
    } else {
      throw new UsernameNotFoundException("Unknown user " + username);
    }
  }
}
