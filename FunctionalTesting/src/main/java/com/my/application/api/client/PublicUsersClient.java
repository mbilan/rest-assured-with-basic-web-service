package com.my.application.api.client;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import com.my.application.api.request.Credentials;
import com.my.application.api.request.RequestBuilder;
import com.my.webservice.entity.UserResponse;
import com.my.webservice.entity.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class PublicUsersClient {

  private final RequestBuilder requestBuilder;

  /**
   * GET /api/public/users/{userName}
   *
   * @param userName target userName for requested link
   * @return Response from server
   */
  public UserResponse getUserByUserName(final String userName) {
    return given(requestBuilder.userScopeReqSpec())
        .when()
        .get("/public/users/{userName}", userName)
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UserResponse.class);
  }

  /**
   * @param credentials user credentials to authorize
   * @param userName requested userName
   * @return Response from server
   */
  public UserResponse getUserByUserName(final Credentials credentials, final String userName) {
    return given(requestBuilder.authorizedRequestSpec(credentials))
        .when()
        .get("/public/users/{userName}", userName)
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UserResponse.class);
  }

  /**
   * GET /api/public/users
   *
   * @return Response from server
   */
  public UsersResponse getUsers() {
    return given(requestBuilder.userScopeReqSpec())
        .when()
        .get("/public/users")
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UsersResponse.class);
  }
}
