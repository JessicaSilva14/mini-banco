package br.com.minibanco.modelo;

import br.com.minibanco.excecao.SaldoInsuficienteException;
import br.com.minibanco.excecao.ValorInvalidoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Conta {
    private final String numero;
    private final Cliente titular;
    private BigDecimal saldo = BigDecimal.ZERO;
    private final List<Transacao> extrato = new ArrayList<>();

    protected Conta(String numero, Cliente titular) {
        this.numero = numero;
        this.titular = titular;
    }

    public void depositar(BigDecimal valor) {
        validarValor(valor);
        saldo = saldo.add(valor);
        registrar("DEPÓSITO", valor);
    }

    public void sacar(BigDecimal valor) {
        validarValor(valor);
        if (!podeSacar(valor)) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque");
        }
        saldo = saldo.subtract(valor);
        registrar("SAQUE", valor);
    }

    /** Cada tipo de conta define sua própria regra de quanto pode sacar. */
    protected abstract boolean podeSacar(BigDecimal valor);

    /** Usado pelas subclasses (ex.: juros) sem expor um setter público de saldo. */
    protected void creditar(BigDecimal valor, String descricao) {
        saldo = saldo.add(valor);
        registrar(descricao, valor);
    }

    // Usados na transferência para registrar no extrato o tipo correto.
    public void sacarParaTransferencia(BigDecimal valor) {
        validarValor(valor);
        if (!podeSacar(valor)) {
            throw new SaldoInsuficienteException("Saldo insuficiente para transferência");
        }
        saldo = saldo.subtract(valor);
        registrar("TRANSFERÊNCIA ENVIADA", valor);
    }

    public void receberTransferencia(BigDecimal valor) {
        validarValor(valor);
        saldo = saldo.add(valor);
        registrar("TRANSFERÊNCIA RECEBIDA", valor);
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor deve ser maior que zero");
        }
    }

    private void registrar(String tipo, BigDecimal valor) {
        extrato.add(new Transacao(tipo, valor));
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public List<Transacao> getExtrato() {
        return Collections.unmodifiableList(extrato);
    }

    public abstract String getTipo();
}
