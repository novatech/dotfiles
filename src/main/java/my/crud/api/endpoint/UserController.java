package my.crud.api.endpoint;


import my.crud.api.model.User;
import my.crud.api.model.UserLogin;
import my.crud.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        User newUser = userService.register(user);
        return ResponseEntity.ok().body(newUser);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserLogin user) {
        Optional<String> token = userService.login(user.getEmail(), user.getPassword());
        if (token.isPresent()) {
            return ResponseEntity.ok().body(Map.of("access_token", token, "result", "Login Success"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("result", "User not found"));
    }
}
