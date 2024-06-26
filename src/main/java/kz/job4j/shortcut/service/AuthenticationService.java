package kz.job4j.shortcut.service;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.request.SignInRequest;
import kz.job4j.shortcut.model.request.SignUpRequest;
import kz.job4j.shortcut.model.response.JwtAuthenticationResponse;
import kz.job4j.shortcut.model.response.SiteRegistrationResponse;

public interface AuthenticationService {
    SiteRegistrationResponse signUp(SignUpRequest request);

    ResultMessage<JwtAuthenticationResponse> signIn(SignInRequest request);
}
