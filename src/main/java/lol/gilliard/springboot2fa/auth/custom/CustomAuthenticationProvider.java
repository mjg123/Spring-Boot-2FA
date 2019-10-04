package lol.gilliard.springboot2fa.auth.custom;

import lol.gilliard.springboot2fa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

// TODO: (login) This might be useful if we want to look up a UserDto by name
//    @Autowired
//    private UserRepository userRepository;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        super();
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // TODO: (login) we can provide extra authentication checks here.

        // `authentication.getName()` gives use the username that is trying to log in
        // `(CustomAuthenticationDetails) authentication.getDetails()` gives us an object
        //     which has extra info that the user provided when they tried to log in.

        // throw an AuthenticationException, or a subclass like BadCredentialsException here
        // to reject this login attempt.

        return super.authenticate(authentication);
    }
}
