package com.gamebox.app.Dto.response;

import com.gamebox.app.Enum.StatusJogos;

public record MetricaStatusResponse(StatusJogos status, Long quantidade) {}
