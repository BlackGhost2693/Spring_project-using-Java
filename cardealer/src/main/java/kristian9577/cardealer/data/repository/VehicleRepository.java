package kristian9577.cardealer.data.repository;

import kristian9577.cardealer.data.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,String> {
    List<Vehicle> findAllByUser_Username(String name);
}
