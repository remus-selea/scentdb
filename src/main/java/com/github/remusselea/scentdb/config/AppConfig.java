package com.github.remusselea.scentdb.config;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "scentdb")
public class AppConfig {

  private CorsConfig corsConfig;

  private JwtConfig jwtConfig;

  private Oauth2Config oauth2Config;

  @Getter
  @Setter
  public static class CorsConfig {

    @NotEmpty
    @NotNull
    private List<String> allowedOrigins;

  }

  @Getter
  @Setter
  public static class JwtConfig {

    @NotBlank
    private String secret;

    @Min(1)
    private Long expirationMinutes;

  }

  @Getter
  @Setter
  public static class Oauth2Config {

    @NotBlank
    private String redirectUri;
  }

}
