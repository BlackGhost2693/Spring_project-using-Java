package kristian9577.cardealer.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class EventAddModel {
    private String id;
    private String name;
    private MultipartFile imageUrl;
    private String description;
    private String date;
}
