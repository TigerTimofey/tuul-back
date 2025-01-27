package tuul.demo.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String email;
    private String password;
    private String name;
}
