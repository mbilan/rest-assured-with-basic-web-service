package com.my.application.api.client;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

import com.my.application.api.request.RequestBuilder;
import com.my.webservice.entity.Role;
import com.my.webservice.entity.User;
import com.my.webservice.entity.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUsersClient {

  private final RequestBuilder requestBuilder;
  /**
   * POST /api/admin/users
   *
   * @param postBody - body for post request
   * @return Response from server
   */
  public User postUser(User postBody) {
    return given(requestBuilder.adminScopeReqSpec())
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
   * @param userName - target userName
   * @param role - new role for user
   * @return Response from server
   */
  public UserResponse putUserRole(String userName, Role role) {
    return given(requestBuilder.adminScopeReqSpec())
        .when()
        .put("/admin/users/{userName}/roles/{roleName}", userName, role.name())
        .then()
        .assertThat()
        .statusCode(SC_OK)
        .extract()
        .as(UserResponse.class);
  }
}
