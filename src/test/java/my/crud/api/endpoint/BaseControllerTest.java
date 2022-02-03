package my.crud.api.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import my.crud.api.model.UserEntity;
import my.crud.api.repository.UserRepository;
import my.crud.api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.when;

abstract class BaseControllerTest {
    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected UserRepository userRepository;

    protected UserEntity userEntity;
    protected String token;

    @BeforeEach
    public void setUp() {

        userEntity = new UserEntity()
                .setName("samad")
                .setEmail("samad@emailku.com")
                .setPassword("abcd1234");

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        token = "faketoken";
        when(jwtService.getSubjectFromToken(token)).thenReturn(Optional.of(userEntity.getEmail()));
    }

    public static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(MapperFeature.USE_ANNOTATIONS).build();
        return mapper.writeValueAsBytes(object);
    }
}