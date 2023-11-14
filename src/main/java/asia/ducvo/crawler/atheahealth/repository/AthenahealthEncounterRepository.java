package asia.ducvo.crawler.atheahealth.repository;

import asia.ducvo.crawler.atheahealth.domain.AthenahealthDocument;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthEncounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthenahealthEncounterRepository extends JpaRepository<AthenahealthEncounter, Long> {
}
