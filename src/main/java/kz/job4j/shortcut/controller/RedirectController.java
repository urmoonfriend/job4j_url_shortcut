package kz.job4j.shortcut.controller;

import kz.job4j.shortcut.enums.ResultCode;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.UrlDto;
import kz.job4j.shortcut.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redirect")
@RequiredArgsConstructor
@Slf4j
public class RedirectController {
    private final UrlService urlService;

    @GetMapping("/{id}")
    public ResponseEntity<ResultMessage<UrlDto>> redirect(@PathVariable String id) {
        ResponseEntity<ResultMessage<UrlDto>> responseEntity =
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResultMessage.failure(ResultCode.BAD_REQUEST.name()));
        try {
            var resultOpt = urlService.findByIdAndIncreaseCount(id);
            if (resultOpt.isSuccess()) {
                responseEntity = ResponseEntity.status(302).body(resultOpt);
            } else {
                responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultOpt);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return responseEntity;
    }
}
