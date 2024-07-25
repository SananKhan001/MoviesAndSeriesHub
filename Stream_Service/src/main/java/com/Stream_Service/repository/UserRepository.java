package com.Stream_Service.repository;

import com.Stream_Service.models.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query(value = "SELECT * FROM user_details WHERE gmail = :username")
    Mono<User> findByUsername(String username);
}
