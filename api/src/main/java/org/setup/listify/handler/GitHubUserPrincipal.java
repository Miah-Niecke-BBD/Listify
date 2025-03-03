package org.setup.listify.handler;

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

    private Map<String, Object> attributes;

    public GitHubUserPrincipal(String githubUserInfo) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(githubUserInfo);
            this.id = jsonNode.get("id").asText();
            this.attributes = Map.of(
                    "id", id
            );
        } catch (IOException e) {
            throw new RuntimeException("Error parsing GitHub user info", e);
        }
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getName() {
        return "";
    }
}