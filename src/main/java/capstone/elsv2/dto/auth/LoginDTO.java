package capstone.elsv2.dto.auth;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private String token;
}
