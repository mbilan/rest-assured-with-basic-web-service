package rest.api.webservice;

import static rest.api.validation.RolesValidator.validateRoleName;

import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import rest.api.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminUsersController {

  private final UserService userService;

  @PostMapping("/users")
  public ResponseEntity<UserResponse> createUser(
      @RequestBody User user, HttpServletRequest request) {
    if (userService.getUserByUsername(user.getUserName()).isPresent()) {
      return new ResponseEntity<>(
          UserResponse.builder()
              .errorMessage("User with userName + " + user.getUserName() + " already exists")
              .build(),
          HttpStatus.CONFLICT);
    }
    User savedUser = userService.saveUser(user);

    URI location =
        UriComponentsBuilder.fromPath("/api/public/users/{userId}")
            .scheme(request.getScheme())
            .host(request.getServerName())
            .port(request.getServerPort())
            .buildAndExpand(user.getUserName())
            .toUri();
    return ResponseEntity.created(location).body(UserResponse.fromUser(savedUser));
  }

  @PutMapping("/users/{userName}/roles/{roleName}")
  public ResponseEntity<UserResponse> createUser(
      @PathVariable(name = "userName") String userName,
      @PathVariable(name = "roleName") String roleName) {
    try {
      validateRoleName(roleName);
      if (userService.getUserByUsername(userName).isEmpty()) {
        return ResponseEntity.notFound().build();
      }
      userService.assignRoleToUser(userName, roleName);
      UserResponse updatedUser =
          UserResponse.fromUser(userService.getUserByUsername(userName).get());
      return ResponseEntity.ok(updatedUser);
    } catch (ValidationException e) {
      UserResponse userResponse = UserResponse.builder().errorMessage(e.getMessage()).build();
      return ResponseEntity.badRequest().body(userResponse);
    }
  }
}
