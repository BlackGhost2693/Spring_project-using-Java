package kristian9577.cardealer.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventViewModel {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private String date;
}
