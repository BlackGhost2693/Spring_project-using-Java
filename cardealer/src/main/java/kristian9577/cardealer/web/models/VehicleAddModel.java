package kristian9577.cardealer.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class VehicleAddModel {
    private String id;
    private String type;
    private String maker;
    private String model;
    private String state;
    private Integer year;
    private Integer mileage;
    private String transmission;
    private String euroStandard;
    private String fuelType;
    private String color;
}
