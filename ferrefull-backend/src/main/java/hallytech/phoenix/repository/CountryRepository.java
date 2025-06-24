package hallytech.phoenix.repository;

import hallytech.phoenix.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
