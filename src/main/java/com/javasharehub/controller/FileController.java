package com.javasharehub.controller;

import com.javasharehub.entity.SharedFile;
import com.javasharehub.entity.User;
import com.javasharehub.service.FileService;
import com.javasharehub.service.PrivateLinkService;
import com.javasharehub.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;
    private final UserService userService;
    private final PrivateLinkService privateLinkService;

    private static final String UPLOAD_DIR = "uploads/";

    private boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @GetMapping({"/", "/files"})
    public String fileList(@RequestParam(defaultValue = "") String category,
                           @RequestParam(defaultValue = "0") int page,
                           Authentication authentication,
                           Model model) {
        Page<SharedFile> files = fileService.getPublicFiles(category, page);
        model.addAttribute("files", files.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", files.getTotalPages());
        model.addAttribute("category", category);
        model.addAttribute("username",
                authentication != null ? authentication.getName() : null);
        model.addAttribute("isAdmin", isAdmin(authentication));
        return "file/list";
    }

    @GetMapping("/profile")
    public String profile(@RequestParam(defaultValue = "") String category,
                          @RequestParam(defaultValue = "0") int page,
                          Authentication authentication,
                          Model model) {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Page<SharedFile> files = fileService.getUserFiles(
                user.getId(), category, page);
        model.addAttribute("files", files.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", files.getTotalPages());
        model.addAttribute("category", category);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", isAdmin(authentication));
        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/files/upload")
    public String uploadPage(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", isAdmin(authentication));
        return "file/upload";
    }

    @PostMapping("/files/upload")
    public String upload(@RequestParam MultipartFile file,
                         @RequestParam String status,
                         @RequestParam(defaultValue = "") String category,
                         Authentication authentication) throws IOException {
        User user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        fileService.upload(file, status, category, user);
        return "redirect:/profile";
    }

    @GetMapping("/files/download/{id}")
    public void downloadPublic(@PathVariable Long id,
                               HttpServletResponse response) throws IOException {
        SharedFile file = fileService.getById(id)
                .orElseThrow(() -> new RuntimeException("Файл не найден"));

        if (!"PUBLIC".equals(file.getStatus())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Файл приватный");
            return;
        }

        fileService.incrementDownload(file);
        sendFile(file, response);
        log.info("Публичный файл скачан: {}", file.getOriginalName());
    }

    @GetMapping("/files/private/{token}")
    public void downloadPrivate(@PathVariable String token,
                                HttpServletResponse response) throws IOException {
        var link = privateLinkService.useLink(token)
                .orElseThrow(() -> new RuntimeException("Ссылка недействительна"));

        SharedFile file = link.getFile();
        fileService.incrementDownload(file);
        sendFile(file, response);
        log.info("Приватный файл скачан по токену: {}", token);
    }

    @PostMapping("/files/generate-link/{id}")
    public String generateLink(@PathVariable Long id,
                               Authentication authentication,
                               Model model) {
        SharedFile file = fileService.getById(id)
                .orElseThrow(() -> new RuntimeException("Файл не найден"));

        if (!file.getUser().getEmail().equals(authentication.getName())) {
            throw new RuntimeException("Нет доступа");
        }

        String token = privateLinkService.generateLink(file);
        model.addAttribute("link", "/files/private/" + token);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", isAdmin(authentication));
        return "file/private-link";
    }

    @PostMapping("/files/delete/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication) {
        fileService.delete(id, authentication.getName());
        return "redirect:/profile";
    }

    private void sendFile(SharedFile file,
                          HttpServletResponse response) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR + file.getStoredName());

        if (!Files.exists(filePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    "Файл не найден на сервере");
            return;
        }

        Resource resource = new UrlResource(filePath.toUri());
        response.setContentType(file.getFileType() != null
                ? file.getFileType() : "application/octet-stream");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getOriginalName() + "\"");
        resource.getInputStream().transferTo(response.getOutputStream());
    }
}