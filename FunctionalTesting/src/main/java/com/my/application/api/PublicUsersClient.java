package com.my.application.api;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

import com.my.webservice.entity.UserResponse;
import com.my.webservice.entity.UsersResponse;
import io.restassured.specification.RequestSpecification;

public final class PublicUsersClient {

  /**
   * GET /api/public/users/{userName}
   *
   * @param userSpec userSpec requestSpecification with user basic authorization
   * @param userName target userName for requested link
   * @return Response from server
   */
  public static UserResponse getUserByUserName(final RequestSpecification userSpec, final String userName) {
    return given(userSpec)
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
   * @param userSpec requestSpecification with user basic authorization
   * @return Response from server
   */
  public static UsersResponse getUsers(final RequestSpecification userSpec) {
    return given(userSpec)
        .when()
        .get("/public/users")
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UsersResponse.class);
  }
}
