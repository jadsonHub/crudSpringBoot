package com.autor.mvc.repositorio;

import com.autor.mvc.model.Autor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    
}
