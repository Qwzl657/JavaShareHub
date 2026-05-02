package com.javasharehub.config;

import com.javasharehub.entity.SharedFile;
import com.javasharehub.entity.User;
import com.javasharehub.repository.SharedFileRepository;
import com.javasharehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final SharedFileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (fileRepository.count() > 0) {
            log.info("Файлы уже есть в БД, пропускаем инициализацию");
            return;
        }

        User user = userRepository.findByEmail("user@mail.com").orElse(null);
        User test = userRepository.findByEmail("test@mail.com").orElse(null);

        if (user == null || test == null) {
            log.warn("Тестовые пользователи не найдены — пропускаем");
            return;
        }

        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        saveFile("photo.png", "photo_001.png",
                "image/png", "Images", "PUBLIC", user);

        saveFile("document.pdf", "doc_001.pdf",
                "application/pdf", "Documents", "PUBLIC", user);

        saveFile("video.mp4", "video_001.mp4",
                "video/mp4", "Videos", "PUBLIC", test);

        saveFile("notes_001.txt", "notes_001.txt",
                "text/plain", "Documents", "PUBLIC", test);

        saveFile("secret.txt", "secret_001.txt",
                "text/plain", "Documents", "PRIVATE", user);

        log.info("=== Инициализация завершена ===");
    }

    private void saveFile(String originalName, String storedName,
                          String fileType, String category,
                          String status, User user) {
        try {
            Path filePath = Paths.get(
                    System.getProperty("user.dir"), "uploads", storedName);

            if (!Files.exists(filePath)) {
                Files.writeString(filePath, "Файл: " + originalName);
                log.warn("Файл не найден, создана заглушка: {}", storedName);
            }

            SharedFile sharedFile = SharedFile.builder()
                    .originalName(originalName)
                    .storedName(storedName)
                    .fileType(fileType)
                    .fileSize(Files.size(filePath))
                    .status(status)
                    .category(category)
                    .downloadCount(0)
                    .uploadedAt(LocalDateTime.now())
                    .user(user)
                    .build();

            fileRepository.save(sharedFile);
            log.info("Зарегистрирован файл: {}", originalName);

        } catch (Exception e) {
            log.error("Ошибка: {}", e.getMessage());
        }
    }

    private void createFromResource(Path uploadPath,
                                    String resourcePath,
                                    String storedName,
                                    String originalName,
                                    String fileType,
                                    String category,
                                    String status,
                                    User user) {
        try {
            Path filePath = uploadPath.resolve(storedName);

            try (InputStream input =
                         getClass().getResourceAsStream(resourcePath)) {
                if (input == null) {
                    log.warn("Ресурс не найден: {}", resourcePath);
                    Files.writeString(filePath,
                            "Файл не найден: " + originalName);
                } else {
                    Files.copy(input, filePath);
                    log.info("Скопирован файл: {}", originalName);
                }
            }

            SharedFile sharedFile = SharedFile.builder()
                    .originalName(originalName)
                    .storedName(storedName)
                    .fileType(fileType)
                    .fileSize(Files.size(filePath))
                    .status(status)
                    .category(category)
                    .downloadCount(0)
                    .uploadedAt(LocalDateTime.now())
                    .user(user)
                    .build();

            fileRepository.save(sharedFile);

        } catch (Exception e) {
            log.error("Ошибка создания файла {}: {}",
                    originalName, e.getMessage());
        }
    }

    private void createFromText(Path uploadPath,
                                String originalName,
                                String storedName,
                                String category,
                                String status,
                                User user,
                                String content) {
        try {
            Path filePath = uploadPath.resolve(storedName);
            Files.writeString(filePath, content);

            SharedFile sharedFile = SharedFile.builder()
                    .originalName(originalName)
                    .storedName(storedName)
                    .fileType("text/plain")
                    .fileSize(Files.size(filePath))
                    .status(status)
                    .category(category)
                    .downloadCount(0)
                    .uploadedAt(LocalDateTime.now())
                    .user(user)
                    .build();

            fileRepository.save(sharedFile);
            log.info("Создан текстовый файл: {}", originalName);

        } catch (Exception e) {
            log.error("Ошибка создания файла {}: {}",
                    originalName, e.getMessage());
        }
    }
}