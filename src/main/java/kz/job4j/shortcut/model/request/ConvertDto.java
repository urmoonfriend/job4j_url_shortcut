package kz.job4j.shortcut.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Data
@Accessors(chain = true)
public class ConvertDto {
    @Schema(description = "URL", example = "https://job4j.ru/profile/exercise/106/task-view/532")
    @Size(min = 5, max = 255, message = "URL должен содержать от 5 до 255 символов")
    @URL(message = "URL should be valid")
    private String url;
}
