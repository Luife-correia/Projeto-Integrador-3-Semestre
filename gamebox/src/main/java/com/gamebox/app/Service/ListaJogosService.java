package com.gamebox.app.Service;


import com.gamebox.app.Domain.ListaJogos;

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


    public ListaJogos criarLista(ListaJogos listaJogos) {
      return listaJogosRepository.save(listaJogos);
    }

    public void deletar(Long id) {
        buscarListaJogos(id);
        listaJogosRepository.deleteById(id);
    }

    public ListaJogos atualizar(ListaJogos listaJogos, Long id) {

       ListaJogos listaAtual = buscarListaJogos(id);

        listaJogos.setNome
                (listaAtual.getNome());

        listaJogos.setPublica
                (listaAtual.getPublica());

        listaJogos.setStatus
                (listaAtual.getStatus());

        listaJogos.getUsuario()
                .setId(listaAtual.getId());

        return listaJogosRepository.save(listaJogos);
    }


    /**
     * Metodos faltantes:

     * ☐ Listar listas públicas de um usuário
     */
}
