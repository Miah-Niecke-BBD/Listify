package org.listify.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GitHubUserPrincipal implements OAuth2User {
    private String id;
    private String login;
    private String email;
    private Map<String, Object> attributes;

    public GitHubUserPrincipal(String githubUserInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(githubUserInfo);
            this.id = jsonNode.get("id").asText();
            this.login = jsonNode.get("login").asText();
            this.email = jsonNode.get("email").asText();
            this.attributes = Map.of(
                    "id", id,
                    "login", login,
                    "email", email
            );
        } catch (IOException e) {
            throw new RuntimeException("Error parsing GitHub user info", e);
        }
    }

    @Override
    public String getName() {
        return login;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
