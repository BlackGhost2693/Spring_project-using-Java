package kristian9577.cardealer.data.repository;

import kristian9577.cardealer.data.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
    List<Offer> findAllByUser_Username(String name);
}
