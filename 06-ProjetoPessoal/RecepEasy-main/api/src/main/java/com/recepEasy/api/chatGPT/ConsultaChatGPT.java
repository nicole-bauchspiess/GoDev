package com.recepEasy.api.chatGPT;


import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterReceita(String texto) {
        OpenAiService service = new OpenAiService("sk-proj-z2a8HObY0DlkD1VmpZOIT3BlbkFJw5ARg83jRXoELFsUdxPi");

        CompletionRequest requisicao = CompletionRequest.builder()
                //.model("text-davinci-003")
                .model("gpt-3.5-turbo-instruct")
                .prompt("gere uma receita com os ingredientes: " + texto + ". A receita precisa ter os " +
                        "campos 'nome', 'ingredientes', 'preparo', " +
                        "'sabor' que pode ser DOCE, AGRIDOCE, SALGADO OU CITRICO.")
                .maxTokens(2000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
    public static String validaComestivel(String texto) {
        OpenAiService service = new OpenAiService("sk-proj-z2a8HObY0DlkD1VmpZOIT3BlbkFJw5ARg83jRXoELFsUdxPi");

        CompletionRequest requisicao = CompletionRequest.builder()
                //.model("text-davinci-003")
                .model("gpt-3.5-turbo-instruct")
                .prompt("Verifique se TODOS os ingredientes são comestiveis: " + texto + ". Se forem, retorne 'comestivel', se não retorne 'nao comestivel'")
                .maxTokens(2000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }



}