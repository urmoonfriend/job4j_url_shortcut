package kz.job4j.shortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.request.ConvertDto;
import kz.job4j.shortcut.model.response.UrlRegistrationResponse;
import kz.job4j.shortcut.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConvertController {
    private final UrlService urlService;

    @GetMapping("/example")
    @Operation(summary = "Доступен только авторизованным пользователям")
    public String example() {
        return "Hello, world!";
    }

    @GetMapping("/admin")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

    @PostMapping("/convert")
    public ResponseEntity<ResultMessage<UrlRegistrationResponse>> convert(@RequestBody ConvertDto dto) {
        ResultMessage<Url> resultMessage = urlService.create(new Url().setUrl(dto.getUrl()));
        ResponseEntity<ResultMessage<UrlRegistrationResponse>> response =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultMessage.failure(resultMessage.getError()));
        if (resultMessage.isSuccess()) {
            response = ResponseEntity.ok(ResultMessage.success(
                    new UrlRegistrationResponse().setCode(resultMessage.getMessage().getId().toString())));
        }
        return response;
    }
}
