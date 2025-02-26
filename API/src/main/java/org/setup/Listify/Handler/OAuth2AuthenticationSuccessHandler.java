package org.setup.Listify.Handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String username = oauth2User.getAttribute("login");

        String jwtToken = JwtTokenUtil.generateToken(username);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
