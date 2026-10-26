package br.com.minibanco.modelo;

import br.com.minibanco.excecao.SaldoInsuficienteException;
import br.com.minibanco.excecao.ValorInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ContaTest {
    private Conta conta;

    @BeforeEach
    void preparar() {
        conta = new ContaPoupanca("1", new Cliente("Ana", "123.456.789-00"));
    }

    @Test
    void depositoAumentaOSaldo() {
        conta.depositar(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
    }

    @Test
    void saqueMaiorQueSaldoLancaExcecao() {
        conta.depositar(new BigDecimal("50.00"));
        assertThrows(SaldoInsuficienteException.class,
                () -> conta.sacar(new BigDecimal("80.00")));
        assertEquals(new BigDecimal("50.00"), conta.getSaldo());
    }

    @Test
    void depositoNegativoLancaExcecao() {
        assertThrows(ValorInvalidoException.class,
                () -> conta.depositar(new BigDecimal("-10")));
    }

    @Test
    void contaCorrentePodeUsarChequeEspecial() {
        Conta corrente = new ContaCorrente("2", new Cliente("Bia", "111.111.111-11"),
                new BigDecimal("200"));
        corrente.sacar(new BigDecimal("150"));
        assertEquals(new BigDecimal("-150"), corrente.getSaldo());
    }

    @Test
    void contaCorrenteNaoPassaDoLimite() {
        Conta corrente = new ContaCorrente("2", new Cliente("Bia", "111.111.111-11"),
                new BigDecimal("200"));
        assertThrows(SaldoInsuficienteException.class,
                () -> corrente.sacar(new BigDecimal("250")));
    }

    @Test
    void poupancaRendeJuros() {
        ContaPoupanca poupanca = (ContaPoupanca) conta;
        poupanca.depositar(new BigDecimal("1000.00"));
        poupanca.renderJuros(new BigDecimal("0.005"));
        assertEquals(new BigDecimal("1005.00"), poupanca.getSaldo());
    }

    @Test
    void extratoRegistraAsOperacoes() {
        conta.depositar(new BigDecimal("100"));
        conta.sacar(new BigDecimal("30"));
        assertEquals(2, conta.getExtrato().size());
    }
}
