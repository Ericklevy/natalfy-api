package com.natalfy.domain.validator;

import com.natalfy.application.dto.ConsultaRequestDTO;

public interface ValidadorAgendamentoConsulta {
    void validar(ConsultaRequestDTO dados);
}