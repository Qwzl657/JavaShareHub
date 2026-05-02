package com.javasharehub.service;

import com.javasharehub.entity.SharedFile;
import com.javasharehub.entity.User;
import com.javasharehub.repository.SharedFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private static final String UPLOAD_DIR = "uploads/";
    private final SharedFileRepository fileRepository;

    public void upload(MultipartFile file, String status,
                       String category, User user) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(storedName);
        file.transferTo(filePath.toFile());

        SharedFile sharedFile = SharedFile.builder()
                .originalName(file.getOriginalFilename())
                .storedName(storedName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .status(status.toUpperCase())
                .category(category)
                .downloadCount(0)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();

        fileRepository.save(sharedFile);
        log.info("Файл загружен: {} пользователем: {}", file.getOriginalFilename(), user.getEmail());
    }

    public Page<SharedFile> getPublicFiles(String category, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("uploadedAt").descending());
        if (category != null && !category.isBlank()) {
            return fileRepository.findByStatusAndCategory("PUBLIC", category, pageable);
        }
        return fileRepository.findByStatus("PUBLIC", pageable);
    }

    public Page<SharedFile> getUserFiles(Long userId, String category, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("uploadedAt").descending());
        if (category != null && !category.isBlank()) {
            return fileRepository.findByUserIdAndCategory(userId, category, pageable);
        }
        return fileRepository.findByUserId(userId, pageable);
    }

    public Optional<SharedFile> getById(Long id) {
        return fileRepository.findById(id);
    }

    @Transactional
    public void incrementDownload(SharedFile file) {
        file.setDownloadCount(file.getDownloadCount() + 1);
        fileRepository.save(file);
    }

    public void delete(Long fileId, String email) {
        SharedFile file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Файл не найден"));
        if (!file.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Нет доступа");
        }
        try {
            Files.deleteIfExists(Paths.get(UPLOAD_DIR + file.getStoredName()));
        } catch (IOException e) {
            log.error("Ошибка удаления файла: {}", e.getMessage());
        }
        fileRepository.delete(file);
        log.info("Файл удалён: {} пользователем: {}", file.getOriginalFilename(), email);
    }

    public long countFiles() {
        return fileRepository.count();
    }
}