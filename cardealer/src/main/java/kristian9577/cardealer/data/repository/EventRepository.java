package kristian9577.cardealer.data.repository;

import kristian9577.cardealer.data.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,String> {
    List<Event> findAllByUser_Username(String name);

    Optional<Event> findById(String id);

    Optional<Event> findByName(String name);

}
