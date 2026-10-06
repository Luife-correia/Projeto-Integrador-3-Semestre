package com.gamebox.app.Service;

import com.gamebox.app.Domain.BibliotecaJogo;
import com.gamebox.app.Repository.BibliotecaJogoRepository;
import org.springframework.stereotype.Service;

@Service
public class BibliotecaJogoService {

    private final BibliotecaJogoRepository repository;

    public BibliotecaJogoService(BibliotecaJogoRepository repository) {
        this.repository = repository;
    }

    // Metódo auxiliar
    private BibliotecaJogo buscarBibliotecaJogo(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biblioteca não encontrada!"));
    }

    // Metódo auxiliar
    private void verificarBibliotecaJogo(Long usuarioId, Long jogoId) {
        if (repository.existsByUsuarioIdAndJogoId(usuarioId, jogoId)) {
            throw new RuntimeException("Jogo já adicionado à biblioteca.");
        }
    }

    public BibliotecaJogo salvar(BibliotecaJogo bibliotecaJogo) {

        verificarBibliotecaJogo(
                bibliotecaJogo.getUsuario().
                        getId(), bibliotecaJogo.getJogo().getId()
        );

        return repository.save(bibliotecaJogo);
    }

    public void deletar(Long id) {
        buscarBibliotecaJogo(id);
        repository.deleteById(id);
    }

    public BibliotecaJogo buscarBibliotecaJogoPorId(Long id) {
        return buscarBibliotecaJogo(id);
    }

    public BibliotecaJogo atualizar(BibliotecaJogo bibliotecaJogo) {
        buscarBibliotecaJogo(bibliotecaJogo.getId());
        return repository.save(bibliotecaJogo);
    }

}
