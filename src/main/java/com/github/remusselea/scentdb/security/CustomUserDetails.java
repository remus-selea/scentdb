package com.github.remusselea.scentdb.security;

import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public class CustomUserDetails implements OAuth2User, UserDetails {

  private Long id;
  private String username;
  private String password;
  private String name;
  private String email;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}