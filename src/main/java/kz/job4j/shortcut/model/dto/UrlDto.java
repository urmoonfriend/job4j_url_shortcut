package kz.job4j.shortcut.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UrlDto {
    private String url;
}
