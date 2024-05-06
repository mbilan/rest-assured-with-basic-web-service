package rest.api.validation;

import com.my.webservice.entity.Role;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;

public final class RolesValidator {

  public static void validateRoleName(@NotNull final String roleName) {
    try {
      Role.valueOf(roleName.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new ValidationException(
          "Unknown roleName " + roleName + ". Valid roles are : " + Arrays.toString(Role.values()));
    }
  }
}
