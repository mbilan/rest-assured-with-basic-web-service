package com.my.webservice.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(Include.NON_NULL)
public class UserResponse {

  private Long id;
  private String userName;
  private Boolean isActive;
  private LocalDateTime registeredTime;
  private String role;
  private String errorMessage;

  public static UserResponse fromUser(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .userName(user.getUserName())
        .isActive(user.getIsActive())
        .registeredTime(user.getRegisteredDateTime())
        .role(user.getRole()!=null ? user.getRole().getRoleName(): null)
        .build();
  }
}
