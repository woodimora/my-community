package com.sparta.community.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControlExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    public String noPost(CustomErrorException exception, Model model) {
        model.addAttribute("ErrorMsg", exception.getMessage());

        return "errorPage";
    }
}
