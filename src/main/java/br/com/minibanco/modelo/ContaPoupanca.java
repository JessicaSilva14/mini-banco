package br.com.minibanco.modelo;

import br.com.minibanco.excecao.ValorInvalidoException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaPoupanca extends Conta {

    public ContaPoupanca(String numero, Cliente titular) {
        super(numero, titular);
    }

    @Override
    protected boolean podeSacar(BigDecimal valor) {
        // poupança não tem cheque especial: só saca o que tem
        return getSaldo().compareTo(valor) >= 0;
    }

    /** taxa em formato decimal: 0.005 = 0,5% */
    public void renderJuros(BigDecimal taxa) {
        if (taxa == null || taxa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("A taxa deve ser maior que zero");
        }
        BigDecimal juros = getSaldo().multiply(taxa).setScale(2, RoundingMode.HALF_EVEN);
        if (juros.compareTo(BigDecimal.ZERO) > 0) {
            creditar(juros, "RENDIMENTO");
        }
    }

    @Override
    public String getTipo() {
        return "Conta Poupança";
    }
}
