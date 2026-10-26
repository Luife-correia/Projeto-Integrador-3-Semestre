package com.gamebox.app.Service;

import com.gamebox.app.Dto.response.MetricaStatusResponse;
import com.gamebox.app.Repository.UsuarioJogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricasSistemaService {

    private final UsuarioJogoRepository usuarioJogoRepository;

    public MetricasSistemaService(
            UsuarioJogoRepository usuarioJogoRepository) {
        this.usuarioJogoRepository = usuarioJogoRepository;
    }

    public List<MetricaStatusResponse> buscarMetricas() {
        return usuarioJogoRepository.buscarMetricasPorStatus();
    }
}