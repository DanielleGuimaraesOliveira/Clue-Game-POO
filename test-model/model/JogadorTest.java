package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JogadorTest {

    
    @Test
    void deveReceberCarta() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));
        Carta carta = new Carta("Faca", TipoCarta.ARMA);

        jogador.recebeCartas(carta);

        assertEquals(1, jogador.getMao().size());
    }

  
    @Test
    void deveMostrarCartaSePossuir() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        Carta carta = new Carta("Faca", TipoCarta.ARMA);
        jogador.recebeCartas(carta);

        Carta resultado = jogador.mostrarCarta(new Carta("Faca", TipoCarta.ARMA));

        assertNotNull(resultado);
        assertEquals("Faca", resultado.getNome());
    }


    @Test
    void naoDeveMostrarCartaSeNaoPossuir() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        jogador.recebeCartas(new Carta("Corda", TipoCarta.ARMA));

        Carta resultado = jogador.mostrarCarta(new Carta("Faca", TipoCarta.ARMA));

        assertNull(resultado);
    }


    @Test
    void jogadorNaoComecaEliminado() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        assertFalse(jogador.isEliminado());
    }
}