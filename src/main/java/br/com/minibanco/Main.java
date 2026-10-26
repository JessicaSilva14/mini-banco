package br.com.minibanco;

import br.com.minibanco.excecao.ContaNaoEncontradaException;
import br.com.minibanco.excecao.SaldoInsuficienteException;
import br.com.minibanco.excecao.ValorInvalidoException;
import br.com.minibanco.modelo.Cliente;
import br.com.minibanco.modelo.Conta;
import br.com.minibanco.modelo.ContaPoupanca;
import br.com.minibanco.modelo.Transacao;
import br.com.minibanco.servico.Banco;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private static final Scanner scanner = new Scanner(System.in);
    private static final Banco banco = new Banco();

    public static void main(String[] args) {
        System.out.println("=== MINI BANCO ===");
        boolean rodando = true;
        while (rodando) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();
            try {
                switch (opcao) {
                    case "1" -> criarConta();
                    case "2" -> depositar();
                    case "3" -> sacar();
                    case "4" -> transferir();
                    case "5" -> verExtrato();
                    case "6" -> listarContas();
                    case "7" -> renderJuros();
                    case "0" -> rodando = false;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SaldoInsuficienteException | ValorInvalidoException
                     | ContaNaoEncontradaException | IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
        System.out.println("Até logo!");
    }

    private static void exibirMenu() {
        System.out.println("""

                1 - Criar conta
                2 - Depositar
                3 - Sacar
                4 - Transferir
                5 - Ver extrato
                6 - Listar contas
                7 - Render juros (poupança)
                0 - Sair
                Escolha:""");
    }

    private static void criarConta() {
        String nome = ler("Nome do titular: ");
        String cpf = ler("CPF: ");
        Cliente cliente = new Cliente(nome, cpf);
        String tipo = ler("Tipo (1 = Corrente, 2 = Poupança): ");

        Conta conta;
        if (tipo.equals("1")) {
            BigDecimal limite = lerValor("Limite de cheque especial: ");
            conta = banco.abrirContaCorrente(cliente, limite);
        } else if (tipo.equals("2")) {
            conta = banco.abrirContaPoupanca(cliente);
        } else {
            System.out.println("Tipo inválido.");
            return;
        }
        System.out.println("Conta criada! Número: " + conta.getNumero());
    }

    private static void depositar() {
        Conta conta = banco.buscarConta(ler("Número da conta: "));
        conta.depositar(lerValor("Valor do depósito: "));
        System.out.println("Depósito realizado. Saldo: " + MOEDA.format(conta.getSaldo()));
    }

    private static void sacar() {
        Conta conta = banco.buscarConta(ler("Número da conta: "));
        conta.sacar(lerValor("Valor do saque: "));
        System.out.println("Saque realizado. Saldo: " + MOEDA.format(conta.getSaldo()));
    }

    private static void transferir() {
        String origem = ler("Conta de origem: ");
        String destino = ler("Conta de destino: ");
        banco.transferir(origem, destino, lerValor("Valor da transferência: "));
        System.out.println("Transferência realizada com sucesso.");
    }

    private static void verExtrato() {
        Conta conta = banco.buscarConta(ler("Número da conta: "));
        System.out.println("\nExtrato - " + conta.getTipo() + " " + conta.getNumero()
                + " (" + conta.getTitular().getNome() + ")");
        if (conta.getExtrato().isEmpty()) {
            System.out.println("Nenhuma movimentação.");
        }
        for (Transacao t : conta.getExtrato()) {
            System.out.println(t);
        }
        System.out.println("Saldo atual: " + MOEDA.format(conta.getSaldo()));
    }

    private static void listarContas() {
        if (banco.listarContas().isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
        }
        for (Conta c : banco.listarContas()) {
            System.out.printf("%s | %-14s | %-20s | %s%n", c.getNumero(), c.getTipo(),
                    c.getTitular().getNome(), MOEDA.format(c.getSaldo()));
        }
    }

    private static void renderJuros() {
        Conta conta = banco.buscarConta(ler("Número da conta poupança: "));
        if (!(conta instanceof ContaPoupanca poupanca)) {
            System.out.println("Essa conta não é poupança.");
            return;
        }
        BigDecimal taxa = lerValor("Taxa (ex.: 0.005 para 0,5%): ");
        poupanca.renderJuros(taxa);
        System.out.println("Juros aplicados. Saldo: " + MOEDA.format(conta.getSaldo()));
    }

    private static String ler(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private static BigDecimal lerValor(String mensagem) {
        String texto = ler(mensagem).replace(",", ".");
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            throw new ValorInvalidoException("Digite um número válido");
        }
    }
}
