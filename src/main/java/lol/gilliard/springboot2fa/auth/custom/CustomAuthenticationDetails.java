package lol.gilliard.springboot2fa.auth.custom;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationDetails extends WebAuthenticationDetails {

    private String user2FaCode;

    public CustomAuthenticationDetails(HttpServletRequest request) {

        // TODO: (DONE login) This would be a good place to pull any extra parameters that we might
        // need to authenticate our user out of the HttpServletRequest.
        // We could store them in fields here and access them in an AuthenticationProvider
        super(request);
        System.out.println(request.getRequestURI() + " :: " + request.getParameter("2facode"));
        this.user2FaCode = request.getParameter("2facode");
    }

    public String getUser2FaCode() {
        return user2FaCode;
    }
}