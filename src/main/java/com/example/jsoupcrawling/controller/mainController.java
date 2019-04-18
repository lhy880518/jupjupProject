package com.example.jsoupcrawling.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class mainController {
    @GetMapping("/")
    public String mainPage(){
        log.info("mainPage");
        System.out.println("mainPage");
        return "/main";
    }

}
