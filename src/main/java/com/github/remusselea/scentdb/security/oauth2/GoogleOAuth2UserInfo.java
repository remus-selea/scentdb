package com.github.remusselea.scentdb.security.oauth2;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuth2UserInfo {

    private String sub;

    private String name;

    private String email;

    private String picture;

}
