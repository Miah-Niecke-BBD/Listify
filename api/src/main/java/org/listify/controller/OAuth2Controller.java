package org.listify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.listify.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${redirect-uri}")
    private String redirectUri;

    @GetMapping("/authentication")
    public ResponseEntity<TokenDTO> getAuthentication(@RequestParam("code") String code, HttpServletRequest request) throws JsonProcessingException {

        String tokenUrl = "https://oauth2.googleapis.com/token";

        String decodedCode;
        try {
            decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", decodedCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject(tokenUrl, body, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);

        String jwtToken = jsonResponse.get("id_token").asText();
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        TokenDTO tokenDTO = new TokenDTO(jwtToken,csrfToken);

        return ResponseEntity.ok(tokenDTO);
    }


    @GetMapping("/login")
    public ResponseEntity<Map> login() {
        String authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=openid%20profile%20email"
                + "&client_id=" + clientId
                + "&redirect_uri=http://localhost:8080/oauth2/callback/google"
                + "&response_type=code";

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("authorization_url", authorizationUrl);

        return ResponseEntity.ok(responseMap);
    }
}
