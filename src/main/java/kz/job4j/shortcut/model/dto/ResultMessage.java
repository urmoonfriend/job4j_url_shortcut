package kz.job4j.shortcut.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultMessage<T> {
    private boolean success = true;
    private T message;
    private String error;

    public static <T> ResultMessage<T> success(T message) {
        return new <T>ResultMessage<T>()
                .setSuccess(true)
                .setMessage(message);
    }

    public static <T> ResultMessage<T> failure(String error) {
        return new <T>ResultMessage<T>()
                .setSuccess(false)
                .setError(error);
    }
}
