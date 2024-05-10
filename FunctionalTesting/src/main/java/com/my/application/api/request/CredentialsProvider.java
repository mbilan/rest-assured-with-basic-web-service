package com.my.application.api.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CredentialsProvider {

  @Value("${api.adminScope.userName}")
  private String apiAdminUserName;

  @Value("${api.adminScope.password}")
  private String adminPassword;

  @Value("${api.userScope.userName}")
  private String simpleUserName;

  @Value("${api.userScope.password}")
  private String simplePassword;

  public Credentials getDefaultAdminCredentials() {
    return new Credentials(apiAdminUserName, adminPassword);
  }

  public Credentials getDefaultUserScopeCredentials() {
    return new Credentials(simpleUserName, simplePassword);
  }
}
