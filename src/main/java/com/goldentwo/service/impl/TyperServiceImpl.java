package com.goldentwo.service.impl;

import com.goldentwo.model.Typer;
import com.goldentwo.repository.TyperRepository;
import com.goldentwo.service.TyperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TyperServiceImpl implements TyperService {

    private final TyperRepository typerRepository;

    @Autowired
    public TyperServiceImpl(TyperRepository typerRepository) {
        this.typerRepository = typerRepository;
    }

    @Override
    public List<Typer> getTyperRanking() {
        List<Typer> typerRepositoryAll = typerRepository.findAll();
        typerRepositoryAll.sort(Comparator.comparing(Typer::getPoints).reversed());

        return typerRepositoryAll;
    }
}
