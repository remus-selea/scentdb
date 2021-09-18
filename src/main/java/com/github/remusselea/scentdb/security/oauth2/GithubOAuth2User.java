package com.github.remusselea.scentdb.security.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubOAuth2User {

  private String id;

  private String login;

  private String name;

  private String email;

  @JsonProperty("avatar_url")
  private String avatarUrl;

}