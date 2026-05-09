package model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class GerenciadorTest {

    private GerenciadorDePartida jogo;

    @Before
    public void setup() {
        jogo = new GerenciadorDePartida();

        jogo.adicionarJogador("Ana", "Srta. Scarlet");
        jogo.adicionarJogador("João", "Verde");

        jogo.iniciarPartida();
    }

    
    @Test
    public void deveIniciarPartidaSemErro() {
        assertNotNull(jogo);
    }


    @Test
    public void deveLancarDadosEntre2e12() {
        int valor = jogo.lancarDados();

        assertTrue(valor >= 2 && valor <= 12);
    }

    
    @Test
    public void deveRetornarCasasValidas() {
        int passos = 2;

        List<Casa> casas = jogo.mapearCasas(passos);

        assertNotNull(casas);
        assertFalse(casas.isEmpty());
    }

   
    @Test
    public void deveMoverPeca() {
        int passos = 1;

        List<Casa> casas = jogo.mapearCasas(passos);

        Casa destino = casas.get(0);

        PecaSuspeito peca = jogo.getJogadorAtual().getPersonagem();
        Casa antes = peca.getPosicaoAtual();

        jogo.deslocarPiao(destino);

        Casa depois = peca.getPosicaoAtual();

        assertNotEquals(antes, depois);
        assertEquals(destino, depois);
    }
    
    @Test
    public void jogadoresDevemReceberCartas() {
        for (Jogador j : jogo.getJogadores()) {
            assertFalse(j.getMao().isEmpty());
        }
    }
    
    @Test
    public void pecasDevemSerPosicionadas() {

        for (Jogador j : jogo.getJogadores()) {
            assertNotNull(j.getPersonagem().getPosicaoAtual());
        }
        
    }
    
    @Test 
    public void jogadoresDevemReceberBlocoDeNotas() {
    	// verifica item 6 da regra
    	for (Jogador j : jogo.getJogadores()) {
    		assertTrue("O jogador deveria ter recebido o bloco de notas", j.isPossuiBlocoDeNotas());
    	}
    }
    
    @Test
    public void srtaScarletDeveSerPrimeiroJogador() {
    	// verifica item 7 da regra
    	Jogador primeiro = jogo.getJogadorAtual();
    	assertEquals("A Srta. Scarlet deve ser a primeira  a jogar", "Srta. Scarlet", primeiro.getPersonagem().getNome());
    }
}