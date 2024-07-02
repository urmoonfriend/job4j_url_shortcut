package kz.job4j.shortcut.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Schema(description = "Запрос на регистрацию")
@Accessors(chain = true)
public class SignUpDto {

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
}