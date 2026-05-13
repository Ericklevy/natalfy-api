package com.natalfy.infrastructure.notification;

import com.natalfy.domain.event.ConsultaAgendadaEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificacaoListener {

    // O @Async faz isso rodar em uma Thread separada (em segundo plano).
    // O paciente não fica esperando o e-mail enviar para ver a tela de sucesso!
    @Async
    @EventListener
    public void enviarEmailConfirmacao(ConsultaAgendadaEvent evento) {

        System.out.println("\n=======================================================");
        System.out.println(" 📨 [SISTEMA DE MENSAGERIA - ASSÍNCRONO] ");
        System.out.println("=======================================================");
        System.out.println("Enviando e-mail de confirmação para a paciente...");

        // Simulando a demora de 2 segundos que um servidor de e-mail real teria
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("✅ E-MAIL ENVIADO COM SUCESSO!");
        System.out.println("Para: " + evento.nomeGestante());
        System.out.println("Assunto: Confirmação de Consulta com " + evento.nomeMedico());
        System.out.println("Data: " + evento.dataHora());
        System.out.println("=======================================================\n");
    }
}