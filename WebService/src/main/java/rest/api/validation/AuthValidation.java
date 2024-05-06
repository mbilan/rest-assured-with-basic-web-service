package rest.api.validation;

import com.my.webservice.validation.ValidationError;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import com.my.webservice.entity.Role;

public final class AuthValidation {

  public static void validateUserConsistency(String userNameFromPath, UserDetails authUser) {
    if (!isAdminUser(authUser) && !authUser.getUsername().equals(userNameFromPath)) {
      throw new AccessDeniedException(
          ValidationError.ACCESS_DENIED_FOR_REQUESTED_USER.getErrorMsg());
    }
  }

  public static boolean isAdminUser(UserDetails authUser) {
    return authUser.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ADMIN.name()));
  }
}
