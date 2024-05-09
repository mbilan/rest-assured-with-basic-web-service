package com.my.webservice.entity;

import com.google.common.base.MoreObjects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder=true)
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "password")
  private String password;

  @Column(name = "registered_date_time")
  private LocalDateTime registeredDateTime;

  @Column(name = "is_active")
  private Boolean isActive;

  @ManyToOne private RoleEntity role;

  @Override
  public String toString() {

    return MoreObjects.toStringHelper(this)
        .add("userName", userName)
        .add("role", role != null ? role.getRoleName() : null)
        .add("isActive", isActive)
        .add("registeredDateTime", registeredDateTime)
        .omitNullValues()
        .toString();
  }
}
