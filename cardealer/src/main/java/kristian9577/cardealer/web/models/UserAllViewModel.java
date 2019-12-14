package kristian9577.cardealer.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserAllViewModel {
    private String id;
    private String username;
    private String password;
    private String email;

    private Set<String> authorities;
}
