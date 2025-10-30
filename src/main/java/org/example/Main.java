package org.example;

import analisar.AnalisadorLexico;
import analisar.Token;
import analisar.TipoToken;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Digite seu código. Digite 'RUN' em uma nova linha para analisar:");
        Scanner scanner = new Scanner(System.in);
        StringBuilder codigoBuilder = new StringBuilder();
        String linha;

        while (true) {
            linha = scanner.nextLine();
            if (linha.equals("RUN")) {
                break;
            }
            codigoBuilder.append(linha).append("\n");
        }

        String codigoFonte = codigoBuilder.toString();

        AnalisadorLexico lexer = new AnalisadorLexico(codigoFonte);
        Token token;

        System.out.println("\nIniciando Análise Léxica...");
        System.out.println("---------------------------------");

        try {
            token = lexer.obterToken();
            while (token.kind != TipoToken.EOF) {
                String textoToken = token.texto.replace("\n", "\\n");
                System.out.printf("Tipo: %-10s | Texto: \"%s\"\n", token.kind, textoToken);

                token = lexer.obterToken();
            }
            System.out.printf("Tipo: %-10s | Texto: \"%s\"\n", token.kind, token.texto);

            System.out.println("---------------------------------");
            System.out.println("Análise Léxica concluída.");

        } catch (RuntimeException e) {
            System.err.println("\n--- ERRO ---");
            System.err.println(e.getMessage());
        }
    }
}