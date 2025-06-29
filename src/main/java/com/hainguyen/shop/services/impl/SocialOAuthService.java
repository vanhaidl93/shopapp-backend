package com.hainguyen.shop.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hainguyen.shop.services.ISocialOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration")
@RequiredArgsConstructor
public class SocialOAuthService implements ISocialOAuthService {

    private Map<String,String> google;
    private Map<String,String> facebook;

    // Google
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String googleUserInfoUri;

    // Facebook
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String facebookClientId;
    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String facebookClientSecret;
    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String facebookRedirectUri;
    @Value("${spring.security.oauth2.client.registration.facebook.auth-uri}")
    private String facebookAuthUri;
    @Value("${spring.security.oauth2.client.registration.facebook.token-uri}")
    private String facebookTokenUri;
    @Value("${spring.security.oauth2.client.registration.facebook.user-info-uri}")
    private String facebookUserInfoUri;

    @Override
    public String generateUrlRequireAuthorizationToken(String loginType) {
        String url = "";
        loginType = loginType.trim().toLowerCase(); // Normalize the login type

        if ("google".equals(loginType)) {
            GoogleAuthorizationCodeRequestUrl urlBuilder = new GoogleAuthorizationCodeRequestUrl(
                    googleClientId,
                    googleRedirectUri,
                    Arrays.asList("email", "profile", "openid")
            );
            url = urlBuilder.build();

        } else if ("facebook".equals(loginType)) {
            /*
            url = String.format("https://www.facebook.com/v3.2/dialog/oauth
            ?client_id=%s
            &redirect_uri=%s
            &scope=email,public_profile
            &response_type=code",facebookClientId, facebookRedirectUri);
             */
            url = UriComponentsBuilder
                    .fromUriString(facebookAuthUri)
                    .queryParam("client_id", facebookClientId)
                    .queryParam("redirect_uri", facebookRedirectUri)
                    .queryParam("scope", "email,public_profile")
                    .queryParam("response_type", "code")
                    .build()
                    .toUriString();
        }
        return url;
    }

    @Override
    public Map<String, Object> exchangeAuthorizationCodeForAccessTokenAndFetchProfile(String authorizationCode, String loginType) {

        String accessToken;
        ObjectMapper objectMapper = new ObjectMapper();

        switch (loginType.toLowerCase()) {
            case "google" -> {
                // REST CLIENT APPROACH - Google client library
                // Step 1: Exchange authorization code for access token
                try {
                    accessToken = new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            new GsonFactory(),
                            googleClientId,
                            googleClientSecret,
                            authorizationCode,
                            googleRedirectUri
                    )
                            .execute()
                            .getAccessToken();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to get access token", e);
                }

                // Step 2: Use RestClient to fetch user profile
                RestClient restClient = RestClient.builder()
                        .baseUrl(googleUserInfoUri)
                        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .build();
                String jsonResponse = restClient.get()
                        .retrieve()
                        .body(String.class);

                // Step 3: Deserialize JSON response to Map
                try {
                    return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to parse user info JSON", e);
                }
            }

            case "facebook" -> {
                // REST CLIENT APPROACH - raw uri.
                // Step 1: Build the token exchange URL
                String urlGetAccessToken = UriComponentsBuilder
                        .fromUriString(facebookTokenUri)
                        .queryParam("client_id", facebookClientId)
                        .queryParam("redirect_uri", facebookRedirectUri)
                        .queryParam("client_secret", facebookClientSecret)
                        .queryParam("code", authorizationCode)
                        .toUriString();

                // Step 2: Create RestClient and fetch access token
                RestClient restClient = RestClient.create();
                String tokenJsonResponse = restClient.get()
                        .uri(urlGetAccessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(String.class);
                try {
                    JsonNode node = objectMapper.readTree(tokenJsonResponse);
                    accessToken = node.get("access_token").asText();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to parse access token JSON", e);
                }

                // Step 3: Fetch user info with access token
                String userInfoUri = facebookUserInfoUri + "&access_token=" + accessToken;
                String userInfoResponse = restClient.get()
                        .uri(userInfoUri)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(String.class);
                try {
                    return objectMapper.readValue(userInfoResponse, new TypeReference<>() {});
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to parse user info JSON", e);
                }
            }

            default -> {
                throw  new IllegalArgumentException("Unsupported login type: " + loginType);
            }
        }
    }
}
