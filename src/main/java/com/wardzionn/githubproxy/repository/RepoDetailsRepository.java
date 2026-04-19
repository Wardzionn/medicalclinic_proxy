package com.wardzionn.githubproxy.repository;

import com.wardzionn.githubproxy.model.RepoDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepoDetailsRepository extends JpaRepository<RepoDetails, Long> {
    Optional<RepoDetails> findByFullName(String fullName);

    void deleteByFullName(String fullName);
}
