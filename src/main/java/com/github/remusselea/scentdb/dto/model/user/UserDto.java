package com.github.remusselea.scentdb.dto.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

  private Long id;
  private String username;
  private String name;
  private String email;
  private String role;
  private String imageUrl;

}