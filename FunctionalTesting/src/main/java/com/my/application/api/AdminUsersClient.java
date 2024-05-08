package com.my.application.api;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import io.restassured.specification.RequestSpecification;

public class AdminUsersClient {

  /**
   * POST /api/admin/users
   *
   * @param userSpec - RequestSpecification with user basic authorization
   * @param postBody - body for post request
   * @return Response from server
   */
  public static User postUser(RequestSpecification userSpec, User postBody) {
    return given(userSpec)
        .body(postBody)
        .when()
        .post("/admin/users")
        .then()
        .assertThat()
        .statusCode(SC_CREATED)
        .extract()
        .as(User.class);
  }

  /**
   * PUT /api/admin/users/{userName}/roles/{roleName}
   * @param userSpec - RequestSpecification with user basic authorization
   * @param userName - target userName
   * @param role - new role for user
   * @return Response from server
   */
  public static UserResponse putUserRole(RequestSpecification userSpec, String userName, Role role) {
    return given(userSpec)
        .when()
        .put("/admin/users/{userName}/roles/{roleName}", userName, role.name())
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UserResponse.class);
  }
}
