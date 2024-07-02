package kz.job4j.shortcut.integrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.job4j.shortcut.model.Site;
import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ConvertDto;
import kz.job4j.shortcut.repository.SiteRepository;
import kz.job4j.shortcut.repository.UrlRepository;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConvertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UrlRepository urlRepository;

    private Site site;

    @BeforeAll
    void setUp() {
        site = new Site()
                .setId(1L)
                .setUsername("username")
                .setPassword("password")
                .setSiteName("www.google.com");

        siteRepository.save(site);
    }

    @AfterEach
    void truncate() {
        urlRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void whenConvertThenOk() throws Exception {
        ConvertDto dto = new ConvertDto()
                .setUrl("https://www.google.com");

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", Is.is(true)))
                .andExpect(authenticated());
    }

    @Test
    public void whenConvertThenUnauthenticated() throws Exception {
        ConvertDto dto = new ConvertDto()
                .setUrl("https://www.google.com");

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().is(403))
                .andExpect(unauthenticated());
    }

    @Test
    public void whenConvertThenUrlAlreadyExists() throws Exception {
        ConvertDto dto = new ConvertDto()
                .setUrl("https://www.google.com");
        urlRepository.save(
                new Url()
                        .setId(UUID.randomUUID())
                        .setUrl(dto.getUrl())
                        .setSite(site)
                        .setCreatedAt(LocalDateTime.now())
        );
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("success", Is.is(false)))
                .andExpect(jsonPath("error", Is.is("Url уже существует")))
                .andExpect(authenticated());
    }

    @Test
    public void whenConvertThenIncorrectUrl() throws Exception {
        ConvertDto dto = new ConvertDto()
                .setUrl("incorrectUrl.com");
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("success", Is.is(false)))
                .andExpect(jsonPath("error", Is.is("Некорректный Url")))
                .andExpect(authenticated());
    }

    @Test
    public void whenEmptyConvertThenIncorrectUrl() throws Exception {
        ConvertDto dto = new ConvertDto();
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("success", Is.is(false)))
                .andExpect(jsonPath("error", Is.is("Некорректный Url")))
                .andExpect(authenticated());
    }

    @Test
    public void whenConvertThenBadRequest() throws Exception {
        ConvertDto dto = new ConvertDto()
                .setUrl("https://www.yandex.ru");
        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(jwt())
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("success", Is.is(false)))
                .andExpect(jsonPath("error", Is.is("Site не найден")))
                .andExpect(authenticated());
    }
}
