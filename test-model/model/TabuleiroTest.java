package model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TabuleiroTest {

    private Tabuleiro tabuleiro;

    @Before
    public void setUp() {
        tabuleiro = new Tabuleiro();
    }
    
    @Test
    public void testeGetCasaValida() {
        Casa casa = tabuleiro.getCasa(0, 0);
        assertNotNull(casa);
    }
    
    @Test
    public void testeGetCasaForaLimite() {
        Casa casa = tabuleiro.getCasa(-1, 0);

        assertNull(casa);
    }
    
    @Test
    public void testeGetCasaMaiorQueLimite() {
        Casa casa = tabuleiro.getCasa(100, 100);
        assertNull(casa);
    }
    
    @Test
    public void testeGetVizinhos() {
        Casa casa = tabuleiro.getCasa(5, 5);
        List<Casa> vizinhos = tabuleiro.getVizinhos(casa);
        assertEquals(4, vizinhos.size());
    }
    
    @Test
    public void testeGetVizinhosCanto() {
        Casa casa = tabuleiro.getCasa(0, 0);
        List<Casa> vizinhos = tabuleiro.getVizinhos(casa);
        assertEquals(2, vizinhos.size());
    }
    
    @Test
    public void testeMoverPeca() {
        Casa origem = tabuleiro.getCasa(1, 1);
        Casa destino = tabuleiro.getCasa(1, 2);

        PecaSuspeito peca = new PecaSuspeito("Mr. Green");

        origem.setOcupante(peca);
        peca.setPosicaoAtual(origem);

        tabuleiro.moverPeca(peca, destino);

        assertNull(origem.getOcupante());
        assertEquals(peca, destino.getOcupante());
        assertEquals(destino, peca.getPosicaoAtual());
    }
    
    @Test
    public void testeCalculaCaminhosValidos() {
        Casa origem = tabuleiro.getCasa(7, 24);

        List<Casa> caminhos = tabuleiro.calculaCaminhosValidos(origem, 2);
        assertFalse(caminhos.isEmpty());
    }
    
    @Test
    public void testeOrigemNaoEstaNosResultados() {
        Casa origem = tabuleiro.getCasa(7, 24);

        List<Casa> caminhos = tabuleiro.calculaCaminhosValidos(origem, 1);
        assertFalse(caminhos.contains(origem));
    }
    
    @Test
    public void testeMapaCarregado() {
        Casa casa = tabuleiro.getCasa(10, 10);

        assertNotNull(casa);
        assertNotNull(casa.getTipo());
    }
    
    @Test
    public void testeDestinoFicaOcupado() {
        Casa origem = tabuleiro.getCasa(2, 2);
        Casa destino = tabuleiro.getCasa(2, 3);

        PecaSuspeito peca = new PecaSuspeito("Mr. Green");

        origem.setOcupante(peca);
        peca.setPosicaoAtual(origem);

        tabuleiro.moverPeca(peca, destino);
        assertTrue(destino.estaOcupada());
    }
}