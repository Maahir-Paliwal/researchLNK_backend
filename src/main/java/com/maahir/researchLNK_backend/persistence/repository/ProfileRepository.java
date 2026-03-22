package com.maahir.researchLNK_backend.persistence.repository;

import com.maahir.researchLNK_backend.persistence.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
