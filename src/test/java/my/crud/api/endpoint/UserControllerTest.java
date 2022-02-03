package my.crud.api.endpoint;

import my.crud.api.exception.DuplicateResourceException;
import my.crud.api.model.User;
import my.crud.api.model.UserLogin;
import my.crud.api.security.WebSecurityConfig;
import my.crud.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({UserController.class})
@Import({WebSecurityConfig.class})
class UserControllerTest extends BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    protected UserService userService;

    @Test
    void registerSuccess() throws Exception {
        User user = new User();
        user.setName("user name");
        user.setEmail("name@user.com");
        user.setPassword("abcd1234");
        when(userService.register(any())).thenReturn(user);

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void registerFailedDuplicateEmail() throws Exception {
        User user = new User();
        user.setName("user name");
        user.setEmail("name@user.com");
        user.setPassword("abcd1234");
        when(userService.register(any())).thenThrow(new DuplicateResourceException("Email already registered"));

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error", is("DuplicateResourceException")))
                .andExpect(jsonPath("$.details", hasItem("Email already registered")));
    }

    @Test
    void registerFailedInvalidEmail() throws Exception {
        User user = new User();
        user.setName("user name");
        user.setEmail("notanemail");
        user.setPassword("abcd1234");

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error", is("MethodArgumentNotValidException")))
                .andExpect(jsonPath("$.details", hasItem("email:must be a well-formed email address")));
    }

    @Test
    void registerFailedBlankInputs() throws Exception {
        User user = new User();
        user.setName(null);
        user.setEmail(null);
        user.setPassword(null);

        mockMvc.perform(post("/users/register")
                        .contentType("application/json")
                        .content(toJson(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error", is("MethodArgumentNotValidException")))
                .andExpect(jsonPath("$.details", hasItem("name:must not be blank")))
                .andExpect(jsonPath("$.details", hasItem("email:must not be blank")))
                .andExpect(jsonPath("$.details", hasItem("password:must not be blank")));
    }


    @Test
    void loginSuccess() throws Exception {
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail("samad@emailku.com");
        userLogin.setPassword("abcd1234");

        when(userService.login(anyString(), anyString())).thenReturn(Optional.of("mylegittoken"));

        mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(toJson(userLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token", is("mylegittoken")))
                .andExpect(jsonPath("$.result", is("Login Success")));
    }

    @Test
    void loginFailedwrongPassword() throws Exception {
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail("samad@emailku.com");
        userLogin.setPassword("abcd1234");

        when(userService.login(anyString(), anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(toJson(userLogin)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.access_token").doesNotExist())
                .andExpect(jsonPath("$.result", is("User not found")));
    }

    @Test
    void loginFailedBlankInputs() throws Exception {
        UserLogin userLogin = new UserLogin();
        mockMvc.perform(post("/users/login")
                        .contentType("application/json")
                        .content(toJson(userLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.error", is("MethodArgumentNotValidException")))
                .andExpect(jsonPath("$.details", hasItem("email:must not be blank")))
                .andExpect(jsonPath("$.details", hasItem("password:must not be blank")));
    }
}