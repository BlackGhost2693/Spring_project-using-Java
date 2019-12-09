package kristian9577.cardealer.data.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id",
            referencedColumnName = "id")
    private User user;

    @Column(name = "created_on", nullable = false)
    private String createdOn;

    @Column(name = "valid_until", nullable = false)
    private String validUntil;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id",
    referencedColumnName = "id")
    private Vehicle vehicle;
}
