package com.javasharehub.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException e,
                                HttpServletRequest request,
                                Model model) {
        log.error("Ошибка на {}: {}", request.getRequestURI(), e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException e, Model model) {
        log.warn("Доступ запрещён: {}", e.getMessage());
        model.addAttribute("error", "У вас нет доступа к этой странице");
        return "error";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSize(MaxUploadSizeExceededException e, Model model) {
        log.warn("Файл слишком большой: {}", e.getMessage());
        model.addAttribute("error", "Файл слишком большой. Максимум 50MB");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception e,
                                HttpServletRequest request,
                                Model model) {
        log.error("Неожиданная ошибка на {}: {}", request.getRequestURI(), e.getMessage());
        model.addAttribute("error", "Что-то пошло не так. Попробуйте позже.");
        return "error";
    }
}