package com.javasharehub.controller;

import com.javasharehub.entity.SharedFile;
import com.javasharehub.entity.User;
import com.javasharehub.repository.SharedFileRepository;
import com.javasharehub.repository.UserRepository;
import com.javasharehub.service.FileService;
import com.javasharehub.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final FileService fileService;
    private final UserService userService;
    private final SharedFileRepository fileRepository;
    private final UserRepository userRepository;

    @GetMapping
    public String panel(@RequestParam(defaultValue = "0") int page,
                        Authentication authentication,
                        Model model) {
        Page<SharedFile> files = fileRepository.findAll(
                PageRequest.of(page, 10, Sort.by("uploadedAt").descending())
        );
        List<User> users = userRepository.findAll();

        model.addAttribute("files", files.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", files.getTotalPages());
        model.addAttribute("totalFiles", fileService.countFiles());
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("users", users);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("isAdmin", true);
        return "admin/panel";
    }

    @PostMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable Long id,
                             Authentication authentication) {
        fileService.adminDelete(id);
        log.info("Админ {} удалил файл: {}", authentication.getName(), id);
        return "redirect:/admin";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             Authentication authentication) {
        userRepository.deleteById(id);
        log.info("Админ {} удалил пользователя: {}",
                authentication.getName(), id);
        return "redirect:/admin";
    }
}