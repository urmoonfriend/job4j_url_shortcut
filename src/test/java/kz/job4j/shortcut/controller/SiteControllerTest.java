package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.SignInDto;
import kz.job4j.shortcut.model.dto.SignUpDto;
import kz.job4j.shortcut.model.dto.UrlDto;
import kz.job4j.shortcut.model.response.JwtAuthenticationResponse;
import kz.job4j.shortcut.model.response.SiteRegistrationResponse;
import kz.job4j.shortcut.service.AuthenticationService;
import kz.job4j.shortcut.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SiteControllerTest {
    @Autowired
    private SiteController controller;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private UrlService urlService;

    private SignUpDto signUpDto;

    private SignInDto signInDto;

    @BeforeEach
    public void setUp() {
        signUpDto = new SignUpDto();
        signInDto = new SignInDto();
    }

    @Test
    public void whenSiteRegistrationThenOk() {
        signUpDto.setUsername("login")
                .setPassword("password")
                .setSite("site.com");
        SiteRegistrationResponse expected = new SiteRegistrationResponse()
                .setRegistration(true)
                .setLogin("login")
                .setPassword("password");
        when(authenticationService.signUp(signUpDto)).thenReturn(expected);
        ResponseEntity<SiteRegistrationResponse> result =
                controller.siteRegistration(signUpDto);
        assertThat(result).isEqualTo(
                ResponseEntity.ok(expected));
    }

    @Test
    public void whenSiteRegistrationThenBadRequest() {
        signUpDto.setUsername("login")
                .setPassword("password")
                .setSite("incorrectSite");
        SiteRegistrationResponse expected = new SiteRegistrationResponse()
                .setRegistration(false)
                .setLogin("login")
                .setPassword("password");
        when(authenticationService.signUp(signUpDto)).thenReturn(expected);
        ResponseEntity<SiteRegistrationResponse> result =
                controller.siteRegistration(signUpDto);
        assertThat(result).isEqualTo(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(expected));
    }

    @Test
    public void whenSiteAuthorizationThenOk() {
        signInDto.setUsername("login")
                .setPassword("password");
        ResultMessage<JwtAuthenticationResponse> expected =
                ResultMessage.success(new JwtAuthenticationResponse()
                        .setToken("GWTToken"));
        when(authenticationService.signIn(signInDto)).thenReturn(expected);
        ResponseEntity<ResultMessage<JwtAuthenticationResponse>> result =
                controller.signIn(signInDto);
        assertThat(result).isEqualTo(
                ResponseEntity.ok(expected));
    }

    @Test
    public void whenSiteAuthorizationThenBadRequest() {
        signInDto.setUsername("incorrectLogin")
                .setPassword("orIncorrectPassword");
        ResultMessage<JwtAuthenticationResponse> expected =
                ResultMessage.failure("Некорректные учетные данные");
        when(authenticationService.signIn(signInDto)).thenReturn(expected);
        ResponseEntity<ResultMessage<JwtAuthenticationResponse>> result =
                controller.signIn(signInDto);
        assertThat(result).isEqualTo(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(expected));
    }

    @Test
    public void whenRedirectThenOk() {
        String id = "someUUIDCode";
        ResultMessage<UrlDto> expected = ResultMessage.success(new UrlDto().setUrl("someUtl"));
        when(urlService.findByIdAndIncreaseCount(id)).thenReturn(expected);
        ResponseEntity<ResultMessage<UrlDto>> result =
                controller.redirect(id);
        assertThat(result).isEqualTo(
                ResponseEntity.status(302).body(expected));
    }

    @Test
    public void whenRedirectThenBadRequest() {
        String id = "IncorrectCode";
        ResultMessage<UrlDto> expected = ResultMessage.failure("Url not found");
        when(urlService.findByIdAndIncreaseCount(id)).thenReturn(expected);
        ResponseEntity<ResultMessage<UrlDto>> result =
                controller.redirect(id);
        assertThat(result).isEqualTo(
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(expected));
    }
}
