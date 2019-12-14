package kristian9577.cardealer.web.models;

import kristian9577.cardealer.data.models.Vehicle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OfferViewModel {

    private String id;
    private String createdOn;
    private String validUntil;
    private BigDecimal price;
    private String description;
    private Vehicle vehicle;

}
