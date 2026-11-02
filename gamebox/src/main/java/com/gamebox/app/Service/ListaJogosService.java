package com.gamebox.app.Service;

import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Domain.Usuario;
import com.gamebox.app.Dto.request.ListaJogosRequest;
import com.gamebox.app.Dto.response.ListaJogosResponse;
import com.gamebox.app.Exception.RecursoNaoEncontradoException;
import com.gamebox.app.Repository.ListaJogosRepository;
import com.gamebox.app.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListaJogosService {

    private final ListaJogosRepository listaJogosRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaJogosService(ListaJogosRepository listaJogosRepository, UsuarioRepository usuarioRepository) {
        this.listaJogosRepository = listaJogosRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Busca a entidade da lista e informa 404 quando ela não existe.
    public ListaJogos buscarListaJogos(Long id) {
        return listaJogosRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Lista de jogos não encontrada!"));
    }

    // Valida o proprietário antes de criar ou alterar uma lista.
    private Usuario buscarUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Usuário não encontrado com id: " + usuarioId));
    }

    @Transactional
    public ListaJogosResponse criarLista(ListaJogosRequest request) {
        Usuario usuario = buscarUsuario(request.usuarioId());

        ListaJogos listaJogos = new ListaJogos(
                usuario,
                null,
                request.nome(),
                request.publica(),
                request.principal()
        );

        ListaJogos salva = listaJogosRepository.save(listaJogos);
        return paraResposta(salva);
    }

    @Transactional
    public void deletar(Long id) {
        buscarListaJogos(id);
        listaJogosRepository.deleteById(id);
    }

    @Transactional
    public ListaJogosResponse atualizar(Long id, ListaJogosRequest request) {
        ListaJogos listaAtual = buscarListaJogos(id);
        Usuario usuario = buscarUsuario(request.usuarioId());

        listaAtual.setNome(request.nome());
        listaAtual.setPublica(request.publica());
        listaAtual.setPrincipal(request.principal());
        listaAtual.setUsuario(usuario);

        ListaJogos salva = listaJogosRepository.save(listaAtual);
        return paraResposta(salva);
    }

    @Transactional(readOnly = true)
    public ListaJogosResponse buscarResposta(Long id) {
        return paraResposta(buscarListaJogos(id));
    }

    @Transactional(readOnly = true)
    public List<ListaJogosResponse> listarTodas() {
        return listaJogosRepository.findAll()
                .stream()
                .map(this::paraResposta)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ListaJogosResponse> listarPorUsuario(Long usuarioId) {
        buscarUsuario(usuarioId);
        return listaJogosRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::paraResposta)
                .toList();
    }

    public ListaJogosResponse paraResposta(ListaJogos lista) {
        return new ListaJogosResponse(
                lista.getId(),
                lista.getNome(),
                lista.getPublica(),
                lista.isPrincipal(),
                lista.getUsuario() != null ? lista.getUsuario().getId() : null,
                lista.getUsuario() != null ? lista.getUsuario().getNome() : null,
                lista.getJogos()
        );
    }
}
