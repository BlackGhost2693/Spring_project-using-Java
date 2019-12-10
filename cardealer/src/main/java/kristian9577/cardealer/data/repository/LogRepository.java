package kristian9577.cardealer.data.repository;

import kristian9577.cardealer.data.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    Optional<Log> findLogByUsername(String username);

}
