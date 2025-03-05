package org.setup.listify.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.setup.listify.dto.TokenDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class TokenController {

    @GetMapping("/getToken")
    public ResponseEntity<TokenDTO> getToken( @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken,HttpServletRequest request) {

        TokenDTO tokenDTO = new TokenDTO(idToken.getTokenValue(),(CsrfToken) request.getAttribute(CsrfToken.class.getName()));

       return ResponseEntity.ok(tokenDTO);

    }

}


