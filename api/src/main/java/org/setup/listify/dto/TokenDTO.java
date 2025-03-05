package org.setup.listify.dto;

import org.springframework.security.web.csrf.CsrfToken;

public class TokenDTO {
    private String jwtToken;
    private CsrfToken csrfToken;


    public TokenDTO(String jwtToken, CsrfToken csrfToken) {
        this.jwtToken = jwtToken;
        this.csrfToken = csrfToken;
    }

    // Getters and setters
    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public CsrfToken geCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(CsrfToken message) {
    }
}
