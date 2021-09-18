package com.github.remusselea.scentdb.model.entity;

import com.github.remusselea.scentdb.security.oauth2.OAuth2Provider;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "User")
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue
  private long id;

  private String username;
  private String password;
  private String name;
  private String email;
  private String role;
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private OAuth2Provider provider;

  private String providerId;

  public User() {
  }

}
