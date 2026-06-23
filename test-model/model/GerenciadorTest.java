package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Interfaces.IJogador;

public class GerenciadorTest {

    private GerenciadorDePartida gerenciador;

    @Before
    public void setUp() {

        gerenciador = GerenciadorDePartida.getInstance();

        gerenciador.reiniciarPartida();
    }
    
    @Test
    public void testeDefinirResultadoDados() {

        gerenciador.definirResultadoDados(3, 4);

        assertEquals(3, gerenciador.getUltimoDado1());

        assertEquals(4, gerenciador.getUltimoDado2());

        assertEquals(7, gerenciador.getValorDados());
    }
    
    @Test
    public void testeZerarDados() {

        gerenciador.definirResultadoDados(5, 6);

        gerenciador.zerarDadosRolados();

        assertEquals(0, gerenciador.getUltimoDado1());

        assertEquals(0, gerenciador.getUltimoDado2());

        assertEquals(0, gerenciador.getValorDados());
    }
    
    @Test
    public void testeAdicionarJogador() {

        gerenciador.adicionarJogador("Danielle", "Srta. Rosa");

        assertEquals(1, gerenciador.getJogadores().size());
    }
    
    @Test
    public void testeNomeJogador() {

        gerenciador.adicionarJogador("Danielle", "Srta. Rosa");

        IJogador jogador = gerenciador.getJogadores().get(0);

        assertEquals("Danielle", jogador.getNome());
    }
    
    @Test
    public void testeIniciarPartida() {
    	
    	
    	

        gerenciador.adicionarJogador("Ana","Srta. Scarlet");

        gerenciador.adicionarJogador("Carlos", "Coronel Mustard");

        gerenciador.iniciarPartida();

        assertNotNull(gerenciador.getJogadorAtual());
    }
    
    @Test
    public void testeLancarDados() {

    	gerenciador.adicionarJogador("Danielle", "Srta. Scarlet");

        gerenciador.iniciarPartida();

        int[] dados = gerenciador.lancarDados();

        assertEquals(2, dados.length);

        assertTrue(dados[0] >= 1 && dados[0] <= 6);

        assertTrue(dados[1] >= 1 && dados[1] <= 6);
    }
    
    @Test
    public void testeValorDados() {

        gerenciador.definirResultadoDados(2, 5);

        assertEquals(7, gerenciador.getValorDados());
    }
    
    @Test
    public void testeListaJogadoresVazia() {

        assertEquals(0, gerenciador.getJogadores().size());
    }
    
    @Test
    public void testeProximoTurno() {
    	

        gerenciador.adicionarJogador("Ana", "Srta. Scarlet");

        gerenciador.adicionarJogador("Carlos","Coronel Mustard");

        gerenciador.iniciarPartida();

        IJogador jogadorInicial = gerenciador.getJogadorAtual();

        gerenciador.proximoTurno();

        IJogador novoJogador = gerenciador.getJogadorAtual();

        assertNotSame(jogadorInicial, novoJogador);
    }
    
    @Test
    public void testeGarantirBlocoNotas() {

        gerenciador.adicionarJogador("Ana", "Srta. Scarlet");

        gerenciador.iniciarPartida();

        gerenciador.garantirBlocoParaJogadorAtual();

        assertNotNull(
            gerenciador.obterBlocoJogadorAtual()
        );
    }
}