package by.vstu.electronicjournal.security;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import java.util.Map;

public class CustomTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication auth2Authentication = super.extractAuthentication(map);
        auth2Authentication.setDetails(map);
        return auth2Authentication;
    }
}
