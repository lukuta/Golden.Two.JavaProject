package com.goldentwo.controller;

import com.goldentwo.model.Typer;
import com.goldentwo.service.TyperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/typers")
public class TyperController {

    private final TyperService typerService;

    @Autowired
    public TyperController(TyperService typerService) {
        this.typerService = typerService;
    }

    @GetMapping
    public List<Typer> getTyperRanking() {
        return typerService.getTyperRanking();
    }
}
