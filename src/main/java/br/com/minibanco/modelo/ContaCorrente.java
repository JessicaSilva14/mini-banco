package br.com.minibanco.modelo;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {
    private final BigDecimal limiteChequeEspecial;

    public ContaCorrente(String numero, Cliente titular, BigDecimal limiteChequeEspecial) {
        super(numero, titular);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    protected boolean podeSacar(BigDecimal valor) {
        // saldo + limite precisa cobrir o valor
        return getSaldo().add(limiteChequeEspecial).compareTo(valor) >= 0;
    }

    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    @Override
    public String getTipo() {
        return "Conta Corrente";
    }
}
