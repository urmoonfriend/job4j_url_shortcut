package kz.job4j.shortcut.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class StatisticResponse {
    private String url;
    private Integer count;
}
