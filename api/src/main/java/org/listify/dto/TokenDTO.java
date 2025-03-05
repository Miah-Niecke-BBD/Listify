package org.listify.dto;

import org.springframework.security.web.csrf.CsrfToken;

public class TokenDTO {
    private String jwtToken;
    private CsrfToken csrfToken;


    public TokenDTO(String jwtToken, CsrfToken csrfToken) {
        this.jwtToken = jwtToken;
        this.csrfToken = csrfToken;
    }


    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public CsrfToken getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(CsrfToken csrfToken) {
        this.csrfToken = csrfToken;
    }
}
