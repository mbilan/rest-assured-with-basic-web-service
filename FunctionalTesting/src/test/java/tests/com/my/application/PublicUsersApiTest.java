package tests.com.my.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.my.application.api.client.PublicUsersClient;
import com.my.application.repository.UsersRepository;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import com.my.webservice.entity.UsersResponse;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Public api: /users")
public class PublicUsersApiTest extends BaseTest {

  @Autowired private PublicUsersClient publicUsersClient;
  @Autowired private UsersRepository usersRepository;

  @DisplayName("should be consistent with db and response by /users/{userName}")
  @Test
  public void shouldReturnUsersList() {
    UsersResponse usersResponse = publicUsersClient.getUsers();

    // select all users from DB and create map with key=username and value = User object
    Map<String, User> usersFromDB =
        StreamSupport.stream(usersRepository.findAll().spliterator(), false)
            .collect(Collectors.toMap(User::getUserName, user -> user));
    for (UserResponse user : usersResponse.getUsers()) {
      UserResponse userProfile = publicUsersClient.getUserByUserName(user.getUserName());
      // compare with response on /users/{userName}
      assertThat(user).usingRecursiveComparison().isEqualTo(userProfile);
      // compare with DB entity
      assertThat(user)
          .usingRecursiveComparison()
          .isEqualTo(UserResponse.fromUser(usersFromDB.get(user.getUserName())));
    }
  }
}
