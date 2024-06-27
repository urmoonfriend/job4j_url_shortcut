package kz.job4j.shortcut.service;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.SignInDto;
import kz.job4j.shortcut.model.dto.SignUpDto;
import kz.job4j.shortcut.model.response.JwtAuthenticationResponse;
import kz.job4j.shortcut.model.response.SiteRegistrationResponse;

public interface AuthenticationService {
    SiteRegistrationResponse signUp(SignUpDto request);

    ResultMessage<JwtAuthenticationResponse> signIn(SignInDto request);
}
