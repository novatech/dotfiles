package my.crud.api.service;

import my.crud.api.AppConfig;
import my.crud.api.exception.DuplicateResourceException;
import my.crud.api.model.User;
import my.crud.api.model.UserEntity;
import my.crud.api.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AppConfig.class})
class UserServiceTest {

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private JwtService jwtService = mock(JwtService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Test
    void testNewRegistrationSuccess() {
        UserService userService = new UserService(userRepository, jwtService, passwordEncoder, modelMapper);

        UserEntity userEntity = new UserEntity()
                .setEmail("emailku@gmail.com")
                .setName("abdul samad")
                .setPassword("abcdef1234");

        User user = modelMapper.map(userEntity, User.class);

        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(userEntity);
        User created = userService.register(user);

        assertThat(created.getEmail()).isSameAs(user.getEmail());
        assertThat(created.getName()).isSameAs(user.getName());

        verify(userRepository).findByEmail(user.getEmail());
        verify(userRepository).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void testDuplicateRegistrationFailed() throws DuplicateResourceException {
        UserService userService = new UserService(userRepository, jwtService, passwordEncoder, modelMapper);

        UserEntity userEntity = new UserEntity()
                .setEmail("emailku@gmail.com")
                .setName("abdul samad")
                .setPassword("abcdef1234");

        User user = modelMapper.map(userEntity, User.class);

        when(userRepository.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(userEntity);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));
        Assertions.assertThrows(DuplicateResourceException.class, () -> userService.register(user));
    }
}