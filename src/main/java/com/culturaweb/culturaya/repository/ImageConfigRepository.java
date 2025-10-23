package com.culturaweb.culturaya.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.culturaweb.culturaya.model.entity.ImageConfig;

@Repository
public interface ImageConfigRepository extends JpaRepository<ImageConfig, Long> {
    Optional<ImageConfig> findBySectionName(String sectionName);
    boolean existsBySectionName(String sectionName);
}
