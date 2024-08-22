package com.Core_Service.repository.db_repository;

import com.Core_Service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM transaction WHERE viewer_id = ?1")
    List<Transaction> findAllByViewerId(Long viewerId);
}
