package kz.job4j.shortcut.service.impl;

import kz.job4j.shortcut.model.dto.ResultMessage;
import kz.job4j.shortcut.model.dto.SignInDto;
import kz.job4j.shortcut.model.dto.SignUpDto;
import kz.job4j.shortcut.model.response.JwtAuthenticationResponse;
import kz.job4j.shortcut.model.response.SiteRegistrationResponse;
import kz.job4j.shortcut.service.AuthenticationService;
import kz.job4j.shortcut.service.JwtService;
import kz.job4j.shortcut.service.SiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final SiteService siteService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public SiteRegistrationResponse signUp(SignUpDto request) {
        SiteRegistrationResponse response = new SiteRegistrationResponse()
                .setLogin(request.getUsername())
                .setPassword(request.getPassword())
                .setRegistration(false);
        try {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            siteService.create(request);
            response = new SiteRegistrationResponse()
                    .setRegistration(true)
                    .setPassword(request.getPassword())
                    .setLogin(request.getUsername());
        } catch (Exception e) {
            log.error("sign up error: [{}]", e.getMessage(), e);
        }
        return response;
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Override
    public ResultMessage<JwtAuthenticationResponse> signIn(SignInDto request) {
        ResultMessage<JwtAuthenticationResponse> response = ResultMessage.failure("Некорректные учетные данные");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));

            var userDetails = siteService
                    .userDetailsService()
                    .loadUserByUsername(request.getUsername());

            var jwt = jwtService.generateToken(userDetails);
            response = ResultMessage.success(new JwtAuthenticationResponse().setToken(jwt));
        } catch (Exception e) {
            log.error("sign in error: [{}]", e.getMessage(), e);
        }
        return response;
    }
}