package kristian9577.cardealer.web.models;

import kristian9577.cardealer.data.models.Vehicle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
public class OfferAddModel {

    private String id;
    private String createdOn;
    private String ValidUntil;
    private BigDecimal price;
    private String description;
    private Vehicle vehicle;
}
