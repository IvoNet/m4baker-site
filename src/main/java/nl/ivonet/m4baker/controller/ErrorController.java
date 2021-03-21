package nl.ivonet.m4baker.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController {

    @GetMapping("/error")
    public String error(final Model model) {
        return "error";
    }

}
