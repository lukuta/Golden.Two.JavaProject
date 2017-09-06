package com.goldentwo.repository;

import com.goldentwo.model.Typer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TyperRepository extends JpaRepository<Typer, Long>{
    Optional<Typer> findByTyper(String typer);
}
