package lol.gilliard.springboot2fa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void createNewUser(UserDto userDto) throws UserAlreadyExistsException {

        if (userRepository.findByUsername(userDto.getUsername()) != null){
            throw new UserAlreadyExistsException();
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // TODO: (registration) This is the place to set any extra details on the userDto before it's persisted to the database

        userRepository.save(userDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = userRepository.findByUsername(username);

        if (userDto == null){
            throw new UsernameNotFoundException("user " + username + " not found");
        }

        return new UserPrincipal(userDto);
    }

    public static class UserAlreadyExistsException extends Exception {
        // thrown if necessary by `createNewUser` above.
    }
}
