package rest.api.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;

public final class LocationBuilder {
  private static final String USER_PATH = "/api/public/users/{userId}";

  public static URI generateLocation(HttpServletRequest request, String userName) {
    return UriComponentsBuilder.fromPath(USER_PATH)
        .scheme(request.getScheme())
        .host(request.getServerName())
        .port(request.getServerPort())
        .buildAndExpand(userName)
        .toUri();
  }
}
