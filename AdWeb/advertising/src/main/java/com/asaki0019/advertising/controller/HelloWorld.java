package com.asaki0019.advertising.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorld {
    @RequestMapping("/hello-world")
    @ResponseBody
    public String helloWorld(){
        return "Hello World";
    }
}