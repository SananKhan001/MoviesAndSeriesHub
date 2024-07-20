package com.Core_Service.repository;

import com.Core_Service.model.Admin;
import com.Core_Service.model.Viewer;
import jdk.jfr.Registered;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query(value = "SELECT * FROM admins ORDER BY ID DESC", nativeQuery = true)
    Page<Admin> findAllAdminsDescOrderById(Pageable pageRequest);
}
