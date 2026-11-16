package com.gamebox.app.Service;

import com.gamebox.app.Domain.Jogo;
import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.ListaJogosRequest;
import com.gamebox.app.Dto.response.JogoResponse;
import com.gamebox.app.Dto.response.ListaJogosResponse;
import com.gamebox.app.Exception.RecursoNaoEncontradoException;
import com.gamebox.app.Repository.ListaJogosRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListaJogosService {

    private final ListaJogosRepository listaJogosRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaJogosService(ListaJogosRepository listaJogosRepository, UsuarioRepository usuarioRepository) {
        this.listaJogosRepository = listaJogosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ListaJogosResponse criarLista(ListaJogosRequest request) {
        Usuario usuario = buscarUsuario(request.usuarioId());
        ListaJogos lista = new ListaJogos(
                usuario,
                new ArrayList<>(),
                request.nome(),
                request.publica(),
                request.principal()
        );
        return paraResposta(listaJogosRepository.save(lista));
    }

    @Transactional
    public ListaJogosResponse atualizar(Long id, ListaJogosRequest request) {
        ListaJogos lista = buscarListaJogos(id);
        Usuario usuario = buscarUsuario(request.usuarioId());

        lista.setNome(request.nome());
        lista.setPublica(request.publica());
        lista.setPrincipal(request.principal());
        lista.setUsuario(usuario);

        return paraResposta(listaJogosRepository.save(lista));
    }

    @Transactional
    public void deletar(Long id) {
        buscarListaJogos(id);
        listaJogosRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ListaJogosResponse buscarResposta(Long id) {
        return paraResposta(buscarListaJogos(id));
    }

    @Transactional(readOnly = true)
    public List<ListaJogosResponse> listarTodas() {
        return listaJogosRepository.findAll().stream()
                .map(this::paraResposta)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ListaJogosResponse> listarPorUsuario(Long usuarioId) {
        buscarUsuario(usuarioId);
        return listaJogosRepository.findByUsuarioId(usuarioId).stream()
                .map(this::paraResposta)
                .toList();
    }

    // --- Métodos Auxiliares e de Mapeamento ---

    public ListaJogos buscarListaJogos(Long id) {
        return listaJogosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lista de jogos não encontrada!"));
    }

    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + usuarioId));
    }

    public ListaJogosResponse paraResposta(ListaJogos lista) {
        List<JogoResponse> jogosResponse = lista.getJogos() != null
                ? lista.getJogos().stream().map(this::jogoParaResposta).toList()
                : new ArrayList<>();

        return new ListaJogosResponse(
                lista.getId(),
                lista.getNome(),
                lista.getPublica(),
                lista.isPrincipal(),
                lista.getUsuario() != null ? lista.getUsuario().getId() : null,
                lista.getUsuario() != null ? lista.getUsuario().getNome() : null,
                jogosResponse
        );
    }

    private JogoResponse jogoParaResposta(Jogo jogo) {
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
