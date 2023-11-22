package asia.ducvo.crawler.atheahealth.repository;

import asia.ducvo.crawler.atheahealth.domain.AthenahealthSurgeryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthenahealthSurgeryHistoryRepository extends JpaRepository<AthenahealthSurgeryHistory, Long> {
}
