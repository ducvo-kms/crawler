package asia.ducvo.crawler.atheahealth.repository;

import asia.ducvo.crawler.atheahealth.domain.AthenahealthPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthenahealthPatientRepository extends JpaRepository<AthenahealthPatient, String> {
  Page<AthenahealthPatient> findByDepartmentIdIsNotNull(Pageable pageable);
}
