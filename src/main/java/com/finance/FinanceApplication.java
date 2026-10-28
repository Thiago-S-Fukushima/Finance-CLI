package com.finance;

import com.finance.entity.Lancamento;
import com.finance.service.LancamentoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@SpringBootApplication
public class FinanceApplication implements CommandLineRunner {
    
    private final LancamentoService lancamentoService;

    private void cadastrarLancamento(Scanner scanner){
        System.out.println("\n===== CADASTRAR LANÇAMENTO =====");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Valor (ex.: 150,50): ");
        String valorText = scanner.nextLine().trim().replace(",", ".");

        System.out.print("Data (AAAA-MM-DD): ");
        String dataText = scanner.nextLine().trim();

        try {
            BigDecimal valor = new BigDecimal(valorText);
            LocalDate data = LocalDate.parse(dataText);

            Lancamento lancamento = new Lancamento(descricao, valor, data);
            
            System.out.println("Lançamento cadastrado com sucesso!");
        } catch(NumberFormatException e) {
            System.out.println("Valor inválido. Exemplo válido: 150,50");
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Use o formato AAAA-MM-DD");
        }
    }

    public FinanceApplication(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }

    @Override
    public void run(String... args){
        Scanner scanner = new Scanner(System.in);

        int opcao;

        do {
            System.out.println("\n===== FINANCE CLI =====");
            System.out.println("1 - Cadastrar lançamento");
            System.out.println("2 - Listar lançamento");
            System.out.println("3 - Excluir lançamento");
            System.out.println("0 - Sair");
            System.out.println("Escolha outra opção: ");

            while(!scanner.hasNextInt()) {
                System.out.println("Digite uma opção numérica.");
                scanner.next();
                System.out.println("Escolha uma opção: ");
            }

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao){
                case 1:
                    cadastrarLancamento(scanner);
                    break;
                case 2:
                    System.out.println("Listagem de lançamentos");
                    break;
                case 3:
                    System.out.println("Exclusão de lançamento");
                    break;
                case 0:
                    System.out.println("Encerrando o Finance CLI...");
                    break;
                default:
                    System.out.println("Opção invalida!");
            }
        } while (opcao != 0);

        scanner.close();
    }
}