package lol.gilliard.springboot2fa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserDto, Long> {

        UserDto findByUsername(String username);

}
