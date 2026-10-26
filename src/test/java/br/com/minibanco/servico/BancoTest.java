package br.com.minibanco.servico;

import br.com.minibanco.excecao.ContaNaoEncontradaException;
import br.com.minibanco.excecao.SaldoInsuficienteException;
import br.com.minibanco.modelo.Cliente;
import br.com.minibanco.modelo.Conta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BancoTest {
    private Banco banco;
    private Conta origem;
    private Conta destino;

    @BeforeEach
    void preparar() {
        banco = new Banco();
        origem = banco.abrirContaPoupanca(new Cliente("Ana", "123.456.789-00"));
        destino = banco.abrirContaPoupanca(new Cliente("Bia", "987.654.321-00"));
        origem.depositar(new BigDecimal("500.00"));
    }

    @Test
    void transferenciaAtualizaAsDuasContas() {
        banco.transferir(origem.getNumero(), destino.getNumero(), new BigDecimal("200.00"));
        assertEquals(new BigDecimal("300.00"), origem.getSaldo());
        assertEquals(new BigDecimal("200.00"), destino.getSaldo());
    }

    @Test
    void transferenciaSemSaldoNaoAlteraNenhumaConta() {
        assertThrows(SaldoInsuficienteException.class, () ->
                banco.transferir(origem.getNumero(), destino.getNumero(), new BigDecimal("900")));
        assertEquals(new BigDecimal("500.00"), origem.getSaldo());
        assertEquals(BigDecimal.ZERO, destino.getSaldo());
    }

    @Test
    void contaInexistenteLancaExcecao() {
        assertThrows(ContaNaoEncontradaException.class, () -> banco.buscarConta("9999"));
    }
}
