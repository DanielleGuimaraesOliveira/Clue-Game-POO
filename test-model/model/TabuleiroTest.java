package model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class TabuleiroTest {


    @Test
    void deveRetornarCasasValidas() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5);
        Casa origem = tabuleiro.getCasa(2, 2);

        List<Casa> casas = tabuleiro.calculaCaminhosValidos(origem, 2);

        assertNotNull(casas);
        assertFalse(casas.isEmpty());
    }

    
    @Test
    void deveRespeitarNumeroDePassos() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5);
        Casa origem = tabuleiro.getCasa(2, 2);

        List<Casa> casas = tabuleiro.calculaCaminhosValidos(origem, 1);

        assertTrue(casas.size() <= 4);
    }


    @Test
    void naoDevePassarPorCasaOcupada() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5);
        Casa origem = tabuleiro.getCasa(2, 2);

        Casa bloqueada = tabuleiro.getCasa(2, 3);
        bloqueada.setOcupante(new PecaSuspeito("Teste"));

        List<Casa> casas = tabuleiro.calculaCaminhosValidos(origem, 1);

        assertFalse(casas.contains(bloqueada));
    }


    @Test
    void deveMoverPecaCorretamente() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5);

        PecaSuspeito peca = new PecaSuspeito("Teste");

        Casa origem = tabuleiro.getCasa(0, 0);
        Casa destino = tabuleiro.getCasa(1, 0);

        tabuleiro.moverPeca(peca, origem);
        tabuleiro.moverPeca(peca, destino);

        assertNull(origem.getOcupante());
        assertEquals(peca, destino.getOcupante());
        assertEquals(destino, peca.getPosicaoAtual());
    }


    @Test
    void origemPermaneceSeNaoMover() {
        Tabuleiro tabuleiro = new Tabuleiro(5, 5);

        Casa origem = tabuleiro.getCasa(2, 2);

        List<Casa> casas = tabuleiro.calculaCaminhosValidos(origem, 0);

        assertTrue(casas.contains(origem));
    }
}