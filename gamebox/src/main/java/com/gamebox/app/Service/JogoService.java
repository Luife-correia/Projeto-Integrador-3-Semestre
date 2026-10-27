package com.gamebox.app.Service;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Dto.request.JogoRequest;
import com.gamebox.app.Dto.response.JogoResponse;
import com.gamebox.app.Repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JogoService {

    private final JogoRepository jogoRepository;

    public JogoService(JogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public JogoResponse salvar(JogoRequest request) {
        return paraResposta(jogoRepository.save(new Jogo(
                request.nome(),
                request.capa(),
                request.plataformas(),
                request.generos(),
                request.desenvolvedora(),
                request.dataLancamento(),
                request.sinopse()
        )));
    }

    public Jogo buscarJogo(Long id) {
        return jogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado com id: "+id));
    }

    public JogoResponse buscarResposta(Long id) {
        return paraResposta(buscarJogo(id));
    }

    public void deletar(Long id){
        buscarJogo(id);
        jogoRepository.deleteById(id);
    }

    public List<JogoResponse> listarTodosOuBuscarPorNome(String nome){
        List<Jogo> jogos;
        if (nome == null || nome.isBlank()){
            jogos = jogoRepository.findAll();
        } else {
            jogos = jogoRepository.findByNomeContainingIgnoreCase(nome);
        }

        return jogos.stream().map(this::paraResposta).toList();
    }

    public JogoResponse atualizar(Long id, JogoRequest jogoAtualizado){
        Jogo jogoAtual = buscarJogo(id);

        jogoAtual.setNome(jogoAtualizado.nome());
        jogoAtual.setCapa(jogoAtualizado.capa());
        jogoAtual.setPlataformas(jogoAtualizado.plataformas());
        jogoAtual.setGeneros(jogoAtualizado.generos());
        jogoAtual.setDesenvolvedora(jogoAtualizado.desenvolvedora());
        jogoAtual.setDataLancamento(jogoAtualizado.dataLancamento());
        jogoAtual.setSinopse(jogoAtualizado.sinopse());

        return paraResposta(jogoRepository.save(jogoAtual));
    }

    private JogoResponse paraResposta(Jogo jogo) {
        return new JogoResponse(
                jogo.getId(),
                jogo.getNome(),
                jogo.getCapa(),
                jogo.getPlataformas(),
                jogo.getGeneros(),
                jogo.getDesenvolvedora(),
                jogo.getDataLancamento(),
                jogo.getSinopse(),
                jogo.getNotaTotal()
        );
    }
}
