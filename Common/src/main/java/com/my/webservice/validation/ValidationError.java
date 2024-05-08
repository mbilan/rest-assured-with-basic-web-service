package com.my.webservice.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ValidationError {
  ACCESS_DENIED_FOR_REQUESTED_USER("You don't have access to requested user"),
  USER_NOT_FOUND("User with userName %s was not found"),
  USER_ALREADY_EXIST("User with userName %s already exists");

  private final String errorMsg;
}
