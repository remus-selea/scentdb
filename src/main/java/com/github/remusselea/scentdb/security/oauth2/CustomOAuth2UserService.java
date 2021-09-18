package com.github.remusselea.scentdb.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.remusselea.scentdb.config.WebSecurityConfig;
import com.github.remusselea.scentdb.model.entity.User;
import com.github.remusselea.scentdb.security.CustomUserDetails;
import com.github.remusselea.scentdb.service.UserService;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserService userService;
  private final ObjectMapper objectMapper;

  public CustomOAuth2UserService(UserService userService, ObjectMapper objectMapper) {
    this.userService = userService;
    this.objectMapper = objectMapper;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String providerName = userRequest.getClientRegistration().getRegistrationId();

    if (providerName.equalsIgnoreCase(OAuth2Provider.GITHUB.name())) {
      return getGithubOAuth2UserInfo(oAuth2User);
    } else if (providerName.equalsIgnoreCase(OAuth2Provider.GOOGLE.name())) {
      return getGoogleOAuth2UserInfo(oAuth2User);
    } else if (providerName.equalsIgnoreCase(OAuth2Provider.FACEBOOK.name())) {
      return getFacebookOAuth2UserInfo(oAuth2User);
    } else {
      throw new InternalAuthenticationServiceException(
          String.format("The OAuth2 provider %s is not supported yet", providerName));
    }
  }

  private CustomUserDetails getGithubOAuth2UserInfo(OAuth2User oAuth2User) {
    GithubOAuth2User githubOAuth2User = objectMapper.convertValue(oAuth2User.getAttributes(),
        GithubOAuth2User.class);

    Optional<User> userOptional = userService.getUserByUsername(githubOAuth2User.getLogin());

    User user;
    if (userOptional.isEmpty()) {
      user = createUser(githubOAuth2User.getLogin(),
          githubOAuth2User.getName(),
          githubOAuth2User.getEmail(),
          githubOAuth2User.getAvatarUrl(),
          OAuth2Provider.GITHUB,
          githubOAuth2User.getId()
      );
    } else {
      user = userOptional.get();
    }

    return createCustomUserDetails(oAuth2User, user, githubOAuth2User.getLogin(),
        githubOAuth2User.getName());
  }


  private CustomUserDetails getGoogleOAuth2UserInfo(OAuth2User oAuth2User) {
    GoogleOAuth2UserInfo googleOAuth2UserInfo = objectMapper.convertValue(
        oAuth2User.getAttributes(), GoogleOAuth2UserInfo.class);

    Optional<User> userOptional = userService.getUserByUsername(googleOAuth2UserInfo.getEmail());

    User user;
    if (userOptional.isEmpty()) {
      user = createUser(googleOAuth2UserInfo.getEmail(),
          googleOAuth2UserInfo.getName(),
          googleOAuth2UserInfo.getEmail(),
          googleOAuth2UserInfo.getPicture(),
          OAuth2Provider.GOOGLE,
          googleOAuth2UserInfo.getSub()
      );
    } else {
      user = userOptional.get();
    }

    return createCustomUserDetails(oAuth2User, user, googleOAuth2UserInfo.getEmail(),
        googleOAuth2UserInfo.getName());
  }

  private CustomUserDetails getFacebookOAuth2UserInfo(OAuth2User oAuth2User) {
    FacebookOauth2UserInfo facebookOauth2UserInfo = objectMapper.convertValue(
        oAuth2User.getAttributes(), FacebookOauth2UserInfo.class);

    String imageUrl = getFacebookUserImg(oAuth2User);

    Optional<User> userOptional = userService.getUserByUsername(facebookOauth2UserInfo.getEmail());

    User user;
    if (userOptional.isEmpty()) {
      user = createUser(
          facebookOauth2UserInfo.getEmail(),
          facebookOauth2UserInfo.getName(),
          facebookOauth2UserInfo.getEmail(),
          imageUrl,
          OAuth2Provider.FACEBOOK,
          facebookOauth2UserInfo.getId()
      );
    } else {
      user = userOptional.get();
    }

    return createCustomUserDetails(oAuth2User, user, facebookOauth2UserInfo.getEmail(),
        facebookOauth2UserInfo.getName());
  }

  private User createUser(String login, String name, String email, String avatarUrl,
      OAuth2Provider oAuth2Provider, String id) {
    User user = new User();
    user.setUsername(login);
    user.setName(name);
    user.setEmail(email);
    user.setRole(WebSecurityConfig.USER);
    user.setImageUrl(avatarUrl);
    user.setProvider(oAuth2Provider);
    user.setProviderId(id);
    user = userService.saveUser(user);
    return user;
  }

  private CustomUserDetails createCustomUserDetails(OAuth2User oAuth2User, User user, String email, String name) {
    CustomUserDetails customUserDetails = new CustomUserDetails();

    customUserDetails.setId(user.getId());
    customUserDetails.setUsername(email);
    customUserDetails.setName(name);
    customUserDetails.setEmail(email);
    customUserDetails.setAttributes(oAuth2User.getAttributes());
    customUserDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

    return customUserDetails;
  }

  private String getFacebookUserImg(OAuth2User oAuth2User) {
    String imageUrl = null;

    Map<String, Object> attributes = oAuth2User.getAttributes();
    if (attributes.containsKey("picture")) {
      Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
      if (pictureObj.containsKey("data")) {
        Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
        if (dataObj.containsKey("url")) {
          imageUrl = (String) dataObj.get("url");
        }
      }
    }

    return imageUrl;
  }

}
