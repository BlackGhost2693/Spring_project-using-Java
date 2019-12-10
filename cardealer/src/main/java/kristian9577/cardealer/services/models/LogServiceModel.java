package kristian9577.cardealer.services.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LogServiceModel extends BaseServiceModel {

    private String username;
    private String description;
    private LocalDateTime time;
}
