package com.springlab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springlab.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // oauth2

	User findByEmail(String email);

	Optional<User> findByUsername(String username);
}
