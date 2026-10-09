package com.gamebox.app.Service;


import com.gamebox.app.Domain.ListaJogos;
import com.gamebox.app.Domain.Usuario;
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


    public ListaJogos salvar(ListaJogos listaJogos) {
        return listaJogosRepository.save(listaJogos);
    }

    public void deletar(Long id) {
        buscarListaJogos(id);
        listaJogosRepository.deleteById(id);
    }

    public ListaJogos atualizar(ListaJogos listaJogos) {
        buscarListaJogos(listaJogos.getId());
        return listaJogosRepository.save(listaJogos);
    }

    /**
     * Metodos faltantes:
     * ☐ Adicionar jogo à lista
     * ☐ Remover jogo da lista
     * ☐ Listar listas públicas de um usuário
      */
}
