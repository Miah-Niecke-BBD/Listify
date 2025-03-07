package org.listify.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.security.web.csrf.CsrfToken;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {
    private String jwtToken;
    private CsrfToken csrfToken;


    public TokenDTO(String jwtToken, CsrfToken csrfToken) {
        this.jwtToken = jwtToken;
        this.csrfToken = csrfToken;
    }

    public TokenDTO() {

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
