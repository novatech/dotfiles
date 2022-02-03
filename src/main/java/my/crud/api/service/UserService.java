package my.crud.api.service;

import my.crud.api.exception.DuplicateResourceException;
import my.crud.api.model.User;
import my.crud.api.model.UserEntity;
import my.crud.api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already registered");
        }
        UserEntity userEntity = new UserEntity()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .setPassword(passwordEncoder.encode(user.getPassword()));

        return modelMapper.map(userRepository.save(userEntity), User.class);
    }

    public Optional<String> login(String email, String password) {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent() && passwordEncoder.matches(password, userEntity.get().getPassword())) {
            return Optional.ofNullable(jwtService.generateToken(email));
        }
        return Optional.empty();
    }
}
