package com.github.remusselea.scentdb.config;

import com.github.remusselea.scentdb.security.TokenAuthenticationFilter;
import com.github.remusselea.scentdb.security.oauth2.CustomAuthenticationSuccessHandler;
import com.github.remusselea.scentdb.security.oauth2.CustomOAuth2UserService;
import java.util.Arrays;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String ADMIN = "ADMIN";
  public static final String USER = "USER";

  private final UserDetailsService userDetailsService;
  private final CustomOAuth2UserService customOauth2UserService;
  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  private final TokenAuthenticationFilter tokenAuthenticationFilter;
  private final AppConfig appConfig;

  public WebSecurityConfig(
      UserDetailsService userDetailsService,
      CustomOAuth2UserService customOauth2UserService,
      CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
      TokenAuthenticationFilter tokenAuthenticationFilter,
      AppConfig appConfig) {
    this.userDetailsService = userDetailsService;
    this.customOauth2UserService = customOauth2UserService;
    this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    this.appConfig = appConfig;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET,  "/", "/error", "/public/**", "/v3/api-docs/**", "/api/swagger-ui/**", "/auth/**", "/oauth2/**", "/scentdb/v1/**" )
        .permitAll()
        .anyRequest().authenticated();

    http.oauth2Login()
        .userInfoEndpoint().userService(customOauth2UserService)
        .and()
        .authorizationEndpoint().baseUri("/oauth2/authorize")
        .and()
        .redirectionEndpoint().baseUri("/oauth2/callback/*")
        .and()
        .successHandler(customAuthenticationSuccessHandler);

    http.logout(logoutConfigurer -> logoutConfigurer.logoutSuccessUrl("/").permitAll());
    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling(
        exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.cors().configurationSource(corsConfigurationSource());
    http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
  }

  private CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(appConfig.getCorsConfig().getAllowedOrigins());
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
