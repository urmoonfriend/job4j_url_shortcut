package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.model.Url;
import kz.job4j.shortcut.model.dto.ConvertDto;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.response.UrlRegistrationResponse;
import kz.job4j.shortcut.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ConvertController {
    private final UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<ResultMessage<UrlRegistrationResponse>> convert(@RequestBody ConvertDto dto) {
        Optional<ResultMessage<Url>> resultMessage = urlService.create(new Url().setUrl(dto.getUrl()));
        return resultMessage
                .filter(ResultMessage::isSuccess)
                .map(result -> ResponseEntity.ok(
                        ResultMessage.success(new UrlRegistrationResponse().setCode(result.getMessage().getId().toString()))
                ))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResultMessage.failure(resultMessage.map(ResultMessage::getError).orElse("Unknown error")))
                );
    }
}
