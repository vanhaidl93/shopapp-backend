package com.hainguyen.shop.services;

import java.util.Map;

public interface ISocialOAuthService {

    String generateUrlRequireAuthorizationToken(String loginType);
    Map<String, Object> exchangeAuthorizationCodeForAccessTokenAndFetchProfile(String code, String loginType);

}
