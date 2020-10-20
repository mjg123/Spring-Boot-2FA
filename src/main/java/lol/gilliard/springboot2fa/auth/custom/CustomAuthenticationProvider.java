package lol.gilliard.springboot2fa.auth.custom;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;
import lol.gilliard.springboot2fa.UserDto;
import lol.gilliard.springboot2fa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

// TODO: (DONE login) This might be useful if we want to look up a UserDto by name
    @Autowired
    private UserRepository userRepository;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        super();
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // TODO: (DONE login) we can provide extra authentication checks here.

        // `authentication.getName()` gives use the username that is trying to log in
        // `(CustomAuthenticationDetails) authentication.getDetails()` gives us an object
        //     which has extra info that the user provided when they tried to log in.

        UserDto userDto = userRepository.findByUsername(authentication.getName());

        if (userDto != null){

            try {
                String serverGeneratedCode = OTP.create(userDto.getSecret(), OTP.timeInHex(), 6, Type.TOTP);

                CustomAuthenticationDetails userProvidedLoginDetails = (CustomAuthenticationDetails) authentication.getDetails();

                logger.info("Server code " + serverGeneratedCode);
                logger.info("User code " + userProvidedLoginDetails.getUser2FaCode());

                if (!serverGeneratedCode.equals(userProvidedLoginDetails.getUser2FaCode())){
                    throw new BadCredentialsException("User's 2FA code didn't match server code");
                }

            } catch (Exception e) {
                logger.error("Oh no", e);
                throw new AuthenticationServiceException("Failed to generate server-side 2FA code");
            }
        }

        // throw an AuthenticationException, or a subclass like BadCredentialsException here
        // to reject this login attempt.

        return super.authenticate(authentication);
    }
}
