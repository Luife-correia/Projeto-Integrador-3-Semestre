package com.gamebox.app.Service;


import com.gamebox.app.Domain.ListaJogos;

import com.gamebox.app.Dto.request.ListaJogosRequest;
import com.gamebox.app.Dto.response.ListaJogosResponse;
import com.gamebox.app.Repository.ListaJogosRepository;
import org.springframework.stereotype.Service;

@Service
public class ListaJogosService {

    private final ListaJogosRepository listaJogosRepository;


    public ListaJogosService(ListaJogosRepository listaJogosRepository) {
        this.listaJogosRepository = listaJogosRepository;
    }

    //Método auxiliar
    private ListaJogos buscarListaJogos(Long id) {
        return listaJogosRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException
                                ("Lista de jogos não encontrada!"));
    }


    public ListaJogosResponse salvar(ListaJogosRequest request) {

        ListaJogos listaJogos = new ListaJogos();

        listaJogos.setNome
                (request.nome());

        listaJogos.setPublica
                (request.publica());

        listaJogos.setStatus
                (request.status());

        listaJogos.getUsuario()
                .setId(request.jogoId());

        return new ListaJogosResponse(
                listaJogosRepository.save
                        (listaJogos).getId(),
                null,
                listaJogos.
                        getNome(),
                listaJogos.
                        getPublica(),
                listaJogos.
                        getStatus()
        );
    }

    public void deletar(Long id) {
        buscarListaJogos(id);
        listaJogosRepository.deleteById(id);
    }

    public ListaJogosResponse atualizar(ListaJogosRequest request) {

        ListaJogos listaJogos = new ListaJogos();

        listaJogos.setNome
                (request.nome());

        listaJogos.setPublica
                (request.publica());

        listaJogos.setStatus
                (request.status());

        listaJogos.getUsuario()
                .setId(request.jogoId());

        return new ListaJogosResponse(
                listaJogosRepository.save
                        (listaJogos).getId(),
                null,
                listaJogos.
                        getNome(),
                listaJogos.
                        getPublica(),
                listaJogos.
                        getStatus()
        );
    }

    /**
     * Metodos faltantes:
     * ☐ Adicionar jogo à lista
     * ☐ Remover jogo da lista
     * ☐ Listar listas públicas de um usuário
     */
}
