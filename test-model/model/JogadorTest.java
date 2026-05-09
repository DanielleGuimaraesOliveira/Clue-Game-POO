package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class JogadorTest {

    
    @Test
    public void deveReceberCarta() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));
        Carta carta = new Carta("Faca", TipoCarta.ARMA);

        jogador.recebeCartas(carta);

        assertEquals(1, jogador.getMao().size());
    }

  
    @Test
    public void deveMostrarCartaSePossuir() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        Carta carta = new Carta("Faca", TipoCarta.ARMA);
        jogador.recebeCartas(carta);

        Carta resultado = jogador.mostrarCarta(new Carta("Faca", TipoCarta.ARMA));

        assertNotNull(resultado);
        assertEquals("Faca", resultado.getNome());
    }


    @Test
    public void naoDeveMostrarCartaSeNaoPossuir() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        jogador.recebeCartas(new Carta("Corda", TipoCarta.ARMA));

        Carta resultado = jogador.mostrarCarta(new Carta("Faca", TipoCarta.ARMA));

        assertNull(resultado);
    }


    @Test
    public void jogadorNaoComecaEliminado() {
        Jogador jogador = new Jogador("Ana", new PecaSuspeito("Srta. Scarlet"));

        assertFalse(jogador.isEliminado());
    }
}