package com.project.siihomework.repository;

import com.project.siihomework.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Auth, Integer>, JpaSpecificationExecutor<Auth> {
}
