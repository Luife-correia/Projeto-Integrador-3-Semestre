package com.gamebox.app.Service;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public Jogo salvar(Jogo jogo) {
        return jogoRepository.save(jogo);
    }

    public Jogo buscarJogo(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado com id: "+id));
    }

    public void deletar(Long id){
        buscarJogo(id);
        jogoRepository.deleteById(id);
    }

    public List<Jogo> listarTodos(String nome){
        if (nome == null || nome.isBlank()){
            return jogoRepository.findAll();
        }
        return jogoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Jogo atualizar(Long id, Jogo jogoAtualizado){
        Jogo jogoAtual = buscarJogo(id);

        jogoAtual.setNome(jogoAtualizado.getNome());
        jogoAtual.setCapa(jogoAtualizado.getCapa());
        jogoAtual.setPlataformas(jogoAtualizado.getPlataformas());
        jogoAtual.setGeneros(jogoAtualizado.getGeneros());
        jogoAtual.setDesenvolvedora(jogoAtualizado.getDesenvolvedora());
        jogoAtual.setDataLancamento(jogoAtualizado.getDataLancamento());
        jogoAtual.setSinopse(jogoAtualizado.getSinopse());

        return jogoRepository.save(jogoAtual);
    }



}
