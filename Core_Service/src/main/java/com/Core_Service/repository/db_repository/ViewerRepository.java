package com.Core_Service.repository.db_repository;

import com.Core_Service.model.Viewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewerRepository extends JpaRepository<Viewer, Long> {
    @Query(value = "SELECT * FROM viewers ORDER BY ID DESC", nativeQuery = true)
    Page<Viewer> findAllViewerDescOrderById(Pageable pageRequest);
}
