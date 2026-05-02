package com.javasharehub.service;

import com.javasharehub.entity.PrivateLink;
import com.javasharehub.entity.SharedFile;
import com.javasharehub.repository.PrivateLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateLinkService {

    private final PrivateLinkRepository privateLinkRepository;

    public String generateLink(SharedFile file) {
        String token = UUID.randomUUID().toString();
        PrivateLink link = PrivateLink.builder()
                .token(token)
                .file(file)
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();
        privateLinkRepository.save(link);
        log.info("Сгенерирована приватная ссылка для файла: {}", file.getId());
        return token;
    }

    @Transactional
    public Optional<PrivateLink> useLink(String token) {
        Optional<PrivateLink> link = privateLinkRepository.findByTokenAndUsedFalse(token);

        if (link.isEmpty()) {
            log.warn("Ссылка недействительна или уже использована: {}", token);
            return Optional.empty();
        }

        PrivateLink privateLink = link.get();
        privateLink.setUsed(true);
        privateLinkRepository.saveAndFlush(privateLink); // flush — сразу в БД
        log.info("Приватная ссылка использована: {}", token);

        return Optional.of(privateLink);
    }
}