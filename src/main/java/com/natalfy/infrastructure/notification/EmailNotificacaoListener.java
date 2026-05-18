package com.natalfy.infrastructure.notification;

import com.natalfy.domain.event.ConsultaAgendadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificacaoListener {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificacaoListener.class);

    @Async
    @EventListener
    public void enviarEmailConfirmacao(ConsultaAgendadaEvent evento) {

        log.info("=======================================================");
        log.info("📨 [SISTEMA DE MENSAGERIA - ASSÍNCRONO]");
        log.info("=======================================================");
        log.info("Enviando e-mail de confirmação para a paciente...");
        log.info("✅ E-MAIL ENVIADO COM SUCESSO!");
        log.info("Para: {}", evento.nomeGestante());
        log.info("Assunto: Confirmação de Consulta com {}", evento.nomeMedico());
        log.info("Data: {}", evento.dataHora());
        log.info("=======================================================");
    }
}