package hallytech.phoenix.repository;

import hallytech.phoenix.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
