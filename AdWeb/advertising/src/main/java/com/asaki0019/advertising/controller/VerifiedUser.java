package com.asaki0019.advertising.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class VerifiedUser {
    @GetMapping("/verifiedUser")
    public ResponseEntity<Map<String, Object>> Verification(HttpSession session) {
        try {
            Boolean authenticated = (session.getAttribute("user") == null);
            return ResponseEntity.ok(Map.of("code", 200, "authenticated", authenticated));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }
}