package kz.job4j.shortcut.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import kz.job4j.shortcut.enums.Role;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Schema(description = "Запрос на регистрацию")
@Slf4j
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Jon")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @JsonProperty("login")
    private String username;

    @Schema(description = "site", example = "job4j.ru")
    @Size(min = 5, max = 255, message = "site должен содержать от 5 до 255 символов")
    private String site;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    @Hidden
    private Role role;
}