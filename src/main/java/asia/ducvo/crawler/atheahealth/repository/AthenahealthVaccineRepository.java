package asia.ducvo.crawler.atheahealth.repository;

import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import asia.ducvo.crawler.atheahealth.domain.AthenahealthVaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthenahealthVaccineRepository extends JpaRepository<AthenahealthVaccine, String> {
}
