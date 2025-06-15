package ru.avdonin.address_comparison.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.avdonin.address_comparison.model.entity.AddressComparison;

public interface AddressComparisonRepository extends JpaRepository<AddressComparison, Long> {
}
