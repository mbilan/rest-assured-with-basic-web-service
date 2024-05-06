package rest.api.webservice;

import static rest.api.validation.AuthValidation.isAdminUser;
import static rest.api.validation.AuthValidation.validateUserConsistency;
import static com.my.webservice.validation.ValidationError.USER_NOT_FOUND;

import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import com.my.webservice.entity.UsersResponse;
import rest.api.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/users")
public class PublicUsersController {
  private final UserService userService;

  @GetMapping("/{userName}")
  public ResponseEntity<UserResponse> getUserByUserName(
      @PathVariable(name = "userName") String userName,
      @AuthenticationPrincipal UserDetails userDetails) {
    try {
      validateUserConsistency(userName, userDetails);
      User user =
          userService
              .getUserByUsername(userName)
              .orElseThrow(
                  () ->
                      new EntityNotFoundException(
                          String.format(USER_NOT_FOUND.getErrorMsg(), userName)));
      return new ResponseEntity<>(UserResponse.fromUser(user), HttpStatus.OK);
    } catch (AccessDeniedException e) {
      return new ResponseEntity<>(
          UserResponse.builder().errorMessage(e.getMessage()).userName(userName).build(),
          HttpStatus.FORBIDDEN);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(
          UserResponse.builder().errorMessage(e.getMessage()).userName(userName).build(),
          HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<UsersResponse> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
    if (isAdminUser(userDetails)) {
      List<UserResponse> users = userService.retrieveUsers();
      return new ResponseEntity<>(UsersResponse.builder().users(users).build(), HttpStatus.OK);
    } else {
      UserResponse userResponse =
          UserResponse.fromUser(userService.getUserByUsername(userDetails.getUsername()).get());
      return new ResponseEntity<>(
          UsersResponse.builder().users(Collections.singletonList(userResponse)).build(),
          HttpStatus.OK);
    }
  }
}
