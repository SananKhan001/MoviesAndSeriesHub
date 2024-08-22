package com.payment.service.repository;

import com.payment.service.models.SeriesUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesUserMappingRepository extends JpaRepository<SeriesUserMapping, Long> {
}
