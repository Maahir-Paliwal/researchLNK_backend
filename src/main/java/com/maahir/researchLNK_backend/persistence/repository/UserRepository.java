package com.maahir.researchLNK_backend.persistence.repository;

import com.maahir.researchLNK_backend.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
