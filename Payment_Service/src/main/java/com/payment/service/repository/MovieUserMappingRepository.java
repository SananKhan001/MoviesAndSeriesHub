package com.payment.service.repository;

import com.payment.service.models.MovieUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieUserMappingRepository extends JpaRepository<MovieUserMapping, Long> {

}
