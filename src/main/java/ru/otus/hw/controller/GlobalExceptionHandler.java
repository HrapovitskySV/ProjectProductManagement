package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.hw.exceptions.InfoSystemNotFoundException;
import ru.otus.hw.exceptions.ProductNotFoundException;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.exceptions.SprintNotFoundException;
import ru.otus.hw.exceptions.TaskNotFoundException;
import ru.otus.hw.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(ProductNotFoundException.class)
    public ModelAndView handeProductNotFoundException(ProductNotFoundException ex) {
        String errorText = messageSource.getMessage("product-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ModelAndView handeTaskNotFoundException(TaskNotFoundException ex) {
        String errorText = messageSource.getMessage("task-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }

    @ExceptionHandler(InfoSystemNotFoundException.class)
    public ModelAndView handeInfoSystemNotFoundException(InfoSystemNotFoundException ex) {
        String errorText = messageSource.getMessage("infoSystem-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }


    @ExceptionHandler(SprintNotFoundException.class)
    public ModelAndView handeSprintNotFoundException(SprintNotFoundException ex) {
        String errorText = messageSource.getMessage("sprint-not-found-error", null,
                LocaleContextHolder.getLocale());
        return new ModelAndView("customError", "errorText", errorText);
    }


    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        return handleExceptionInternal(
                ex, errors, headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(value = {RoleNotFoundException.class})
    protected ResponseEntity<Object> handleRoleNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Role not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "User not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }



}
