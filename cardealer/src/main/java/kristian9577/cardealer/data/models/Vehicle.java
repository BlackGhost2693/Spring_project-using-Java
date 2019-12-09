package kristian9577.cardealer.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "vehicle")
    private Offer offer;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "maker", nullable = false)
    private String maker;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "year", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT '0'")
    private Integer year;

    @Column(name = "mileage", nullable = false, columnDefinition = "INT UNSIGNED DEFAULT '0'")
    private Integer mileage;

    @Column(name = "transmission", nullable = false)
    private String transmission;

    @Column(name = "euro_standard", nullable = false)
    private String euroStandard;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Column(name = "color", nullable = false)
    private String color;

}
