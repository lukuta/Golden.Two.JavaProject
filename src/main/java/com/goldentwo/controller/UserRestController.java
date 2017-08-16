package com.goldentwo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @GetMapping("/me")
    public Principal getLoggedUserPrincipal(Principal principal) {
        return principal;
    }
}
