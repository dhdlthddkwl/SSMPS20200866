package capstone_design_1.ssmps_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {
    String token;

    public Token(String token) {
        this.token = token;
    }
}
