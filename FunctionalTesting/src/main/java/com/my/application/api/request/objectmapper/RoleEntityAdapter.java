package com.my.application.api.request.objectmapper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.my.webservice.entity.RoleEntity;
import java.lang.reflect.Type;

public class RoleEntityAdapter implements JsonSerializer<RoleEntity>, JsonDeserializer<RoleEntity> {

  @Override
  public RoleEntity deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
      throws JsonParseException {
    return RoleEntity.builder().roleName(jsonElement.getAsString()).build();
  }

  @Override
  public JsonElement serialize(
      RoleEntity roleEntity, Type type, JsonSerializationContext jsonSerializationContext) {
    return new JsonPrimitive(roleEntity.getRoleName());
  }
}
