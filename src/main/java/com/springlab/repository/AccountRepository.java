package com.springlab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springlab.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a where a.username = ?1 AND a.password = ?2")
	Account findByAccount(String username, String password);

	Optional<Account> findByUsername(String username);
}
