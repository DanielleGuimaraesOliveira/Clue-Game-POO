package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class BaralhoTest {


    @Test
    void deveInicializarComCartas() {
        Baralho baralho = new Baralho();

        assertFalse(baralho.getCartas().isEmpty());
    }


    @Test
    void deveRemoverCartaAoComprar() {
        Baralho baralho = new Baralho();

        int tamanhoInicial = baralho.getCartas().size();

        Carta carta = baralho.compraCarta();

        assertNotNull(carta);
        assertEquals(tamanhoInicial - 1, baralho.getCartas().size());
    }

 
    @Test
    void deveFiltrarPorTipo() {
        Baralho baralho = new Baralho();

        List<Carta> suspeitos = baralho.filtrarPorTipo(TipoCarta.SUSPEITO);

        assertFalse(suspeitos.isEmpty());

        for (Carta c : suspeitos) {
            assertEquals(TipoCarta.SUSPEITO, c.getTipo());
        }
    }

   
    @Test
    void embaralharNaoDeveAlterarQuantidade() {
        Baralho baralho = new Baralho();

        int tamanhoAntes = baralho.getCartas().size();

        baralho.embaralhar();

        int tamanhoDepois = baralho.getCartas().size();

        assertEquals(tamanhoAntes, tamanhoDepois);
    }


    @Test
    void deveRemoverCartaEspecifica() {
        Baralho baralho = new Baralho();

        Carta carta = baralho.getCartas().get(0);

        baralho.removeCarta(carta);

        assertFalse(baralho.getCartas().contains(carta));
    }
}