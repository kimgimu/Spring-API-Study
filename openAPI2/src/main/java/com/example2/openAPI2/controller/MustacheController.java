package com.example2.openAPI2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MustacheController {

    @GetMapping("/book")
    public String movieSearch(){

        return "bookSearch";
    }


}
