package kz.job4j.shortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kz.job4j.shortcut.enums.ResultCode;
import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.UrlDto;
import kz.job4j.shortcut.model.request.SignInRequest;
import kz.job4j.shortcut.model.request.SignUpRequest;
import kz.job4j.shortcut.model.response.JwtAuthenticationResponse;
import kz.job4j.shortcut.model.response.SiteRegistrationResponse;
import kz.job4j.shortcut.service.UrlService;
import kz.job4j.shortcut.service.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/site")
@RequiredArgsConstructor
@Slf4j
public class SiteController {
    private final AuthenticationServiceImpl authenticationService;
    private final UrlService urlService;
    private static final String SITE_TYPE = "site";

    @Operation(summary = "Регистрация сайта")
    @PostMapping("/registration")
    public ResponseEntity<SiteRegistrationResponse> siteRegistration(@RequestBody @Valid SignUpRequest request) {
        SiteRegistrationResponse result = authenticationService.signUp(request);
        ResponseEntity<SiteRegistrationResponse> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        if (result.isRegistration()) {
            response = ResponseEntity.ok(result);
        }
        return response;
    }

    @Operation(summary = "Авторизация сайта")
    @PostMapping("/authorization")
    public ResponseEntity<ResultMessage<JwtAuthenticationResponse>> signIn(@RequestBody @Valid SignInRequest request) {
        ResultMessage<JwtAuthenticationResponse> result = authenticationService.signIn(request);
        ResponseEntity<ResultMessage<JwtAuthenticationResponse>> response =
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        if (result.isSuccess()) {
            response = ResponseEntity.ok(result);
        }
        return response;
    }

    @GetMapping("/redirect/{id}")
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
