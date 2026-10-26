package com.gamebox.app.Controller;

import com.gamebox.app.Dto.response.MetricaStatusResponse;
import com.gamebox.app.Service.MetricasSistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metricas")
public class MetricasSistemaController {

    private final MetricasSistemaService metricasSistemaService;

    public MetricasSistemaController(MetricasSistemaService metricasSistemaService) {
        this.metricasSistemaService = metricasSistemaService;
    }


    // @PreAuthorize("hasAnyRole('USER')")
    @GetMapping
    public ResponseEntity<List<MetricaStatusResponse>> listarTodos() {
        return ResponseEntity.ok
                (metricasSistemaService.buscarMetricas());
    }
}
