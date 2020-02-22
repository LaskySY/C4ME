package com.c4me.server.core.credential.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.c4me.server.config.AuthStateRedisCache;
import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.credential.domain.GitHubUserInfoEntity;
import com.c4me.server.core.credential.domain.GoogleUserInfoEntity;
import com.c4me.server.utils.RequestUtils;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthGoogleRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * <dl>
 *     <dd><a target="_blank" href="http://127.0.0.1:3000/oauth/render/github">github</a></dd>
 *     <dd><a target="_blank" href="http://127.0.0.1:3000/oauth/render/google">Google</a></dd>
 * </dl>
 */

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@RestController
@RequestMapping("/oauth")
public class OauthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthStateRedisCache stateRedisCache;

    @RequestMapping("/render/{source}")
    @LogAndWrap(log = "Apply third-party token", wrap = false)
    public void renderAuth(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        logger.info("applying " + source + " token");
        AuthRequest authRequest = getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(authorizeUrl);
    }

    @RequestMapping("/callback/{source}")
    @LogAndWrap(log = "Get third-party token", wrap = false)
    public Object login(@PathVariable("source") String source, AuthCallback callback) throws GeneralSecurityException, IOException {
        logger.info("enter callback：" + source + " callback params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        logger.info("Get the token: "+JSONObject.toJSONString(response));
        return response;
    }

    @RequestMapping("/revoke/{source}/{token}")
    @LogAndWrap(log = "Revoke third-party token", wrap = false)
    public Object revokeAuth(@PathVariable("source") String source, @PathVariable("token") String token) throws IOException {
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.revoke(AuthToken.builder().accessToken(token).build());
    }

    @RequestMapping("/refresh/{source}")
    @LogAndWrap(log = "Refresh third-party token", wrap = false)
    public Object refreshAuth(@PathVariable("source") String source, String token){
        AuthRequest authRequest = getAuthRequest(source);
        return authRequest.refresh(AuthToken.builder().refreshToken(token).build());
    }

    private Object getUserInfo(AuthResponse response, String source){
        AuthUser authUser = (AuthUser)response.getData();
        switch (source){
            case "google":
                String idToken = authUser.getToken().getIdToken();
                String requestBody = RequestUtils.sendGetRequest(
                        "https://oauth2.googleapis.com/tokeninfo",
                        new LinkedMultiValueMap<String, String>(){
                            {add("id_token", idToken);}
                        });
                GoogleUserInfoEntity googleUser = JSON.parseObject(
                        requestBody, GoogleUserInfoEntity.class);
                logger.info(googleUser.toString());
                return googleUser;
            case "github":
                GitHubUserInfoEntity gitHubUser = GitHubUserInfoEntity.builder()
                        .email(authUser.getEmail())
                        .name(authUser.getNickname())
                        .build();
                logger.info(gitHubUser.toString());
                return gitHubUser;
            default:
                logger.error("Cannot find correspond userinfo token source");
                return null;
        }
    }

    private AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("892bd60532493d6165a6")
                        .clientSecret("d383bf40b22cc0658261ac873bebf8d3f32319d1")
                        .redirectUri("http://127.0.0.1:3000/oauth/callback/github")
                        .build(), stateRedisCache);
                break;
            case "google":
                authRequest = new AuthGoogleRequest(AuthConfig.builder()
                        .clientId("580576959354-nrf2camc0ho1tsagigb5dipbr5e3kn7h.apps.googleusercontent.com")
                        .clientSecret("upvSaAducHK1UznSL0b5FSIG")
                        .redirectUri("http://127.0.0.1:3000/oauth/callback/google")
                        .build(), stateRedisCache);
                break;
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("JustOauth Config Error: authRequest is null");
        }
        return authRequest;
    }
}