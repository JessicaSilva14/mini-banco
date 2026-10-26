package br.com.minibanco.servico;

import br.com.minibanco.excecao.ContaNaoEncontradaException;
import br.com.minibanco.excecao.ValorInvalidoException;
import br.com.minibanco.modelo.Cliente;
import br.com.minibanco.modelo.Conta;
import br.com.minibanco.modelo.ContaCorrente;
import br.com.minibanco.modelo.ContaPoupanca;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Banco {
    private final Map<String, Conta> contas = new HashMap<>();
    private int proximoNumero = 1001;

    public Conta abrirContaCorrente(Cliente cliente, BigDecimal limite) {
        Conta conta = new ContaCorrente(gerarNumero(), cliente, limite);
        contas.put(conta.getNumero(), conta);
        return conta;
    }

    public Conta abrirContaPoupanca(Cliente cliente) {
        Conta conta = new ContaPoupanca(gerarNumero(), cliente);
        contas.put(conta.getNumero(), conta);
        return conta;
    }

    public Conta buscarConta(String numero) {
        Conta conta = contas.get(numero);
        if (conta == null) {
            throw new ContaNaoEncontradaException("Conta " + numero + " não encontrada");
        }
        return conta;
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public void transferir(String numeroOrigem, String numeroDestino, BigDecimal valor) {
        if (numeroOrigem.equals(numeroDestino)) {
            throw new ValorInvalidoException("Origem e destino devem ser contas diferentes");
        }
        Conta origem = buscarConta(numeroOrigem);
        Conta destino = buscarConta(numeroDestino);

        // Se o saque falhar, lança exceção e o destino nunca é creditado.
        origem.sacarParaTransferencia(valor);
        destino.receberTransferencia(valor);
    }

    private String gerarNumero() {
        return String.valueOf(proximoNumero++);
    }
}
