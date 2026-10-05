package com.gamebox.app.Service;

import com.gamebox.app.Repository.BibliotecaJogoRepository;
import org.springframework.stereotype.Service;

@Service
public class BibliotecaJogoService {

    private final BibliotecaJogoRepository repository;

    public BibliotecaJogoService(BibliotecaJogoRepository repository) {
        this.repository = repository;
    }





}
