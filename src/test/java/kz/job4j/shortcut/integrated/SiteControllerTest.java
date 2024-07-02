package kz.job4j.shortcut.integrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.dto.SignInDto;
import kz.job4j.shortcut.model.dto.SignUpDto;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.service.impl.AuthenticationServiceImpl;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    private SignUpDto signUpDto;

    private SignInDto signInDto;

    @AfterEach
    public void truncate() {
        siteRepository.deleteAll();
    }

    @Test
    public void whenRegistrationThenOk() throws Exception {
        signUpDto = new SignUpDto()
                .setUsername("username")
                .setPassword("password")
                .setSite("www.google.com");

        mockMvc.perform(post("/site/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("registration", Is.is(true)))
                .andExpect(jsonPath("login", Is.is(signUpDto.getUsername())));
    }

    @Test
    public void whenRegistrationThenBadRequest() throws Exception {
        signUpDto = new SignUpDto();

        mockMvc.perform(post("/site/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("registration", Is.is(false)))
                .andExpect(jsonPath("login", Is.is(signUpDto.getUsername())));
    }

    @Test
    public void whenRegistrationThenSiteAlreadyExist() throws Exception {
        siteRepository.save(
                new Site()
                        .setId(1L)
                        .setUsername("username")
                        .setPassword("password")
                        .setSiteName("www.google.com")
        );

        signUpDto = new SignUpDto()
                .setUsername("username")
                .setPassword("password")
                .setSite("www.google.com");

        mockMvc.perform(post("/site/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("registration", Is.is(false)))
                .andExpect(jsonPath("login", Is.is(signUpDto.getUsername())));
    }

    @Test
    public void whenSignInThenOk() throws Exception {
        authenticationService.signUp(new SignUpDto()
                .setSite("www.authenticated.com")
                .setUsername("authenticated")
                .setPassword("authenticated"));

        signInDto = new SignInDto()
                .setUsername("authenticated")
                .setPassword("authenticated");

        mockMvc.perform(post("/site/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Is.is(true)));
    }

    @Test
    public void whenSignInThenBadCredentials() throws Exception {
        signInDto = new SignInDto()
                .setUsername("authenticated")
                .setPassword("authenticated");

        mockMvc.perform(post("/site/authorization")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signInDto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("success", Is.is(false)))
                .andExpect(jsonPath("error", Is.is("Некорректные учетные данные")));
    }
}
