package com.javasharehub.repository;

import com.javasharehub.entity.PrivateLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrivateLinkRepository extends JpaRepository<PrivateLink, Long> {
    Optional<PrivateLink> findByTokenAndUsedFalse(String token);
}