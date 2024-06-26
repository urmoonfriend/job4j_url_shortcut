package kz.job4j.shortcut.model.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SiteRegistrationResponse {
    private boolean registration;
    private String login;
    private String password;
}
