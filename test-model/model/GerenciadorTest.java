package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GerenciadorTest {

    private GerenciadorDePartida jogo;

    @BeforeEach
    void setup() {
        jogo = new GerenciadorDePartida();

        jogo.adicionarJogador(new Jogador("Ana", new PecaSuspeito("Scarlet")));
        jogo.adicionarJogador(new Jogador("João", new PecaSuspeito("Verde")));

        jogo.iniciarPartida();
    }

    
    @Test
    void deveIniciarPartidaSemErro() {
        assertNotNull(jogo);
    }


    @Test
    void deveLancarDadosEntre2e12() {
        int valor = jogo.lancarDados();

        assertTrue(valor >= 2 && valor <= 12);
    }

    
    @Test
    void deveRetornarCasasValidas() {
        int passos = 2;

        List<Casa> casas = jogo.mapearCasas(passos);

        assertNotNull(casas);
        assertFalse(casas.isEmpty());
    }

   
    @Test
    void deveMoverPeca() {
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
    void jogadoresDevemReceberCartas() {
        GerenciadorDePartida jogo = new GerenciadorDePartida();

        jogo.adicionarJogador(new Jogador("A", new PecaSuspeito("X")));
        jogo.adicionarJogador(new Jogador("B", new PecaSuspeito("Y")));

        jogo.iniciarPartida();

        for (Jogador j : jogo.getJogadores()) {
            assertFalse(j.getMao().isEmpty());
        }
    }
    
    @Test
    void pecasDevemSerPosicionadas() {
        GerenciadorDePartida jogo = new GerenciadorDePartida();

        jogo.adicionarJogador(new Jogador("A", new PecaSuspeito("X")));
        jogo.adicionarJogador(new Jogador("B", new PecaSuspeito("Y")));

        jogo.iniciarPartida();

        for (Jogador j : jogo.getJogadores()) {
            assertNotNull(j.getPersonagem().getPosicaoAtual());
        }
        
    }
}