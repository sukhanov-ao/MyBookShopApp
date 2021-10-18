package com.example.mybookshopapp.security.oauth2;

import com.example.mybookshopapp.security.BookStoreUser;
import com.example.mybookshopapp.security.BookStoreUserDetailService;
import com.example.mybookshopapp.security.BookStoreUserDetails;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";

    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };

    private final BookStoreUserDetailService userService;

    private final RestOperations restOperations;

    private final Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();

    public CustomOAuth2UserService(BookStoreUserDetailService bookStoreUserDetailService) {
        this.userService = bookStoreUserDetailService;
        this.restOperations = createRestTemplate();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        checkUserInfo(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        checkAttributeNamePresence(registrationId, userNameAttributeName);

        ResponseEntity<Map<String, Object>> response = null;
        try {
            response = this.restOperations.exchange(requestEntityConverter.convert(userRequest), PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException ex) {
            getErrorDetails(userRequest, ex);
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }

        Map<String, Object> userAttributes = getUserAttributes(userRequest, response);

        BookStoreUser user = findOrCreate(userAttributes);
        return new BookStoreUserDetails(user);
    }

    private Map<String, Object> getUserAttributes(OAuth2UserRequest userRequest, ResponseEntity<Map<String, Object>> response) {
        Map<String, Object> userAttributes = response.getBody();

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));

        for (String authority : userRequest.getAccessToken().getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }
        return userAttributes;
    }

    private void checkAttributeNamePresence(String registrationId, String userNameAttributeName) {
        if (!StringUtils.hasText(userNameAttributeName)) {
            OAuth2Error oauth2Error = new OAuth2Error(
                    MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE,
                    "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + registrationId, null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }

    private void checkUserInfo(OAuth2UserRequest userRequest) {
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE, "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }

    private void getErrorDetails(OAuth2UserRequest userRequest, OAuth2AuthorizationException ex) {
        OAuth2Error oauth2Error = ex.getError();
        StringBuilder errorDetails = new StringBuilder();
        errorDetails.append("Error details: [");
        errorDetails.append("UserInfo Uri: ").append(
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
        errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
        if (oauth2Error.getDescription() != null) {
            errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
        }
        errorDetails.append("]");
        oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE,
                "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), null);
        throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
    }

    private BookStoreUser findOrCreate(Map<String, Object> userAttributes) {
        String login = (String) userAttributes.get("login");
        String username = (String) userAttributes.get("name");
        String email = (String) userAttributes.get("email");
        BookStoreUser bookStoreUser = new BookStoreUser();
        bookStoreUser.setLogin(login);
        bookStoreUser.setEmail(email);
        bookStoreUser.setName(username);
        bookStoreUser.setOAuth2(true);
        return bookStoreUser;
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        return restTemplate;
    }

}
