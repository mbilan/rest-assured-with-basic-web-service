package com.my.application.api.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.my.application.api.request.objectmapper.LocalDateTypeAdapter;
import com.my.application.api.request.objectmapper.RoleEntityAdapter;
import com.my.webservice.entity.RoleEntity;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestBuilder {
  private final String baseUrl;
  private final String basePath;
  private final CredentialsProvider credentialsProvider;

  @Autowired
  public RequestBuilder(
      @Value("${api.base-url}") String baseUrl,
      @Value("${api.base-path}") String basePath,
      CredentialsProvider credentialsProvider) {
    this.baseUrl = baseUrl;
    this.basePath = basePath;
    this.credentialsProvider = credentialsProvider;
  }

  public RequestSpecification baseRequestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseUrl)
        .setBasePath(basePath)
        .setContentType(ContentType.JSON)
        .setConfig(customConfig())
        .log(LogDetail.ALL)
        .build();
  }

  public RequestSpecification adminScopeReqSpec() {
    final Credentials credentials = credentialsProvider.getDefaultAdminCredentials();
    return authorizedRequestSpec(credentials);
  }

  public RequestSpecification userScopeReqSpec() {
    final Credentials credentials = credentialsProvider.getDefaultAdminCredentials();
    return authorizedRequestSpec(credentials);
  }

  public RequestSpecification authorizedRequestSpec(Credentials credentials) {
    return baseRequestSpec().auth().basic(credentials.login(), credentials.password());
  }

  /** Build custom gson mapper for LocalDateTime and RoleEntity fields; configure logConfig */
  public RestAssuredConfig customConfig() {
    Gson gson =
        new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(RoleEntity.class, new RoleEntityAdapter())
            .create();
    return RestAssured.config()
        .objectMapperConfig(new ObjectMapperConfig().gsonObjectMapperFactory((type, s) -> gson))
        .logConfig(
            LogConfig.logConfig()
                .enableLoggingOfRequestAndResponseIfValidationFails()
                .enablePrettyPrinting(true));
  }
}
