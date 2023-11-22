package asia.ducvo.crawler.atheahealth.repository;

import asia.ducvo.crawler.atheahealth.domain.AthenahealthDocument;
import asia.ducvo.crawler.atheahealth.domain.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthenahealthObservationRepository extends JpaRepository<Observation, Long> {
}
