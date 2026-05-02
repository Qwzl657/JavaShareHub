package com.javasharehub.repository;

import com.javasharehub.entity.SharedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedFileRepository extends JpaRepository<SharedFile, Long> {
    Page<SharedFile> findByStatus(String status, Pageable pageable);
    Page<SharedFile> findByStatusAndCategory(String status, String category, Pageable pageable);
    Page<SharedFile> findByUserId(Long userId, Pageable pageable);
    Page<SharedFile> findByUserIdAndCategory(Long userId, String category, Pageable pageable);
}