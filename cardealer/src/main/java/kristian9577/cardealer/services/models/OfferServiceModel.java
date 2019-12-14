package kristian9577.cardealer.services.models;

import kristian9577.cardealer.data.models.Vehicle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OfferServiceModel extends BaseServiceModel {

    private Vehicle vehicle;
    private String createdOn;
    private String validUntil;
    private BigDecimal price;
    private String description;
}
