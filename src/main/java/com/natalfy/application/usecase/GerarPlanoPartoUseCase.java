package com.natalfy.application.usecase;

import com.natalfy.application.dto.PlanoPartoResponse; // Seu DTO
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class GerarPlanoPartoUseCase {

    private final ChatClient chatClient;

    public GerarPlanoPartoUseCase(ChatClient.Builder chatClientBuilder) {
        // Vincula o ChatClient configurado pelo starter do Spring AI
        this.chatClient = chatClientBuilder.build();
    }

    public PlanoPartoResponse executar(String nomeGestante, String dadosClinicos) {
        // Prompt do sistema que blinda o comportamento do modelo
        String systemPrompt = """
            Você é a inteligência artificial especialista em obstetrícia humanizada da Clínica Natalfy.
            Sua tarefa é gerar estritamente o corpo de um "Plano de Parto Humanizado".
            
            Regras de formatação cruciais:
            1. Use exclusivamente Markdown limpo (títulos com #, subtítulos com ###, negritos com ** e listas com *).
            2. Não inclua saudações iniciais como "Olá" ou introduções informais da clínica no início do texto.
            3. Não inclua assinaturas ou mensagens de encerramento amigáveis no final.
            4. Vá direto para o título principal em Markdown e estruture os módulos clínicos.
            """;

        String userPrompt = String.format(
                "Gere o rascunho de plano de parto para a gestante %s. Condições e preferências: %s",
                nomeGestante, dadosClinicos
        );

        // Dispara a chamada ao Gemini 2.5-Flash
        String planoMarkdown = this.chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        // Mapeia e retorna o objeto estruturado em vez de String pura
        return new PlanoPartoResponse(
                nomeGestante,
                "Baixo Risco", // Aqui você pode mapear dinamicamente conforme sua lógica
                planoMarkdown,
                LocalDateTime.now()
        );
    }
}