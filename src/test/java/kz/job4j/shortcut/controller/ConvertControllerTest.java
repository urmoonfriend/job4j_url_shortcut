package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ConvertDto;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.response.UrlRegistrationResponse;
import kz.job4j.shortcut.service.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConvertControllerTest {
    @Autowired
    private ConvertController controller;

    @MockBean
    private UrlService urlService;

    private ConvertDto convertDto;

    @BeforeEach
    public void setUp() {
        convertDto = new ConvertDto();
    }

    @Test
    public void whenConvertThenOk() {
        String rightUrl = "https://www.google.com";
        UUID id = UUID.randomUUID();
        convertDto.setUrl(rightUrl);
        Url url = new Url()
                .setId(id)
                .setUrl(rightUrl);
        when(urlService.create(any()))
                .thenReturn(Optional.of(ResultMessage.success(url)));
        ResponseEntity<ResultMessage<UrlRegistrationResponse>> result =
                controller.convert(convertDto);
        assertThat(result).isEqualTo(ResponseEntity.ok(ResultMessage.success(
                new UrlRegistrationResponse()
                        .setCode(id.toString())
        )));
    }

    @Test
    public void whenConvertThenBadRequest() {
        String incorrectUrl = "badUrl";
        convertDto.setUrl(incorrectUrl);
        when(urlService.create(any()))
                .thenReturn(Optional.of(ResultMessage.failure("Ошибка сервера")));
        ResponseEntity<ResultMessage<UrlRegistrationResponse>> result =
                controller.convert(convertDto);
        assertThat(result).isEqualTo(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultMessage.failure(
                "Ошибка сервера"
        )));
    }

}
