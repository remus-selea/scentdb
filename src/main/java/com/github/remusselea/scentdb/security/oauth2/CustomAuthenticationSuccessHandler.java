package com.github.remusselea.scentdb.security.oauth2;

import com.github.remusselea.scentdb.config.AppConfig;
import com.github.remusselea.scentdb.security.TokenProvider;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    private final AppConfig appConfig;

    private CustomAuthenticationSuccessHandler(TokenProvider tokenProvider, AppConfig appConfig) {
        this.tokenProvider = tokenProvider;
        this.appConfig = appConfig;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);

        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = appConfig.getOauth2Config().getRedirectUri();

        if (targetUrl.isEmpty()){
            targetUrl =  determineTargetUrl(request, response, authentication);
        }

        String token = tokenProvider.generate(authentication);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}