package lol.gilliard.springboot2fa;

import lol.gilliard.springboot2fa.auth.custom.CustomAuthenticationDetailsSource;
import lol.gilliard.springboot2fa.auth.custom.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // for simplicity's sake. I *don't* recommend this in real apps.

            .authorizeRequests()  // Defines which URLs can be accessed without being logged in.
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/user/registration").permitAll()
                .antMatchers("/user/registered").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated() // Anything without `.permitAll()` will need user to be logged in.

            .and()
            .headers().frameOptions().sameOrigin() // only needed for the h2-console

            .and()
            .formLogin()  // Spring handles the login process for us setting a JSESSIONID cookie
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .authenticationDetailsSource(customAuthenticationDetailsSource)

            .and()
            .logout()  // Spring handles the logout flow too
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(@Autowired UserDetailsService userDetailsService,
                                                            @Autowired PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
    }

}
