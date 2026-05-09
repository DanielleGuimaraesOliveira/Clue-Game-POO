package model;

import static org.junit.Assert.*;
import org.junit.Test;

public class EnvelopeTest {

 
    @Test
    public void deveRetornarTrueParaAcusacaoCorreta() {
        Carta suspeito = new Carta("Sr. Verde", TipoCarta.SUSPEITO);
        Carta arma = new Carta("Faca", TipoCarta.ARMA);
        Carta local = new Carta("Cozinha", TipoCarta.COMODO);

        Envelope envelope = new Envelope(suspeito, arma, local);

        boolean resultado = envelope.verificarAcusacao(suspeito, arma, local);

        assertTrue(resultado);
    }


    @Test
    public void deveRetornarFalseParaSuspeitoErrado() {
        Envelope envelope = new Envelope(
                new Carta("Sr. Verde", TipoCarta.SUSPEITO),
                new Carta("Faca", TipoCarta.ARMA),
                new Carta("Cozinha", TipoCarta.COMODO)
        );

        boolean resultado = envelope.verificarAcusacao(
                new Carta("Srta. Scarlet", TipoCarta.SUSPEITO),
                new Carta("Faca", TipoCarta.ARMA),
                new Carta("Cozinha", TipoCarta.COMODO)
        );

        assertFalse(resultado);
    }


    @Test
    public void deveRetornarFalseParaArmaErrada() {
        Envelope envelope = new Envelope(
                new Carta("Sr. Verde", TipoCarta.SUSPEITO),
                new Carta("Faca", TipoCarta.ARMA),
                new Carta("Cozinha", TipoCarta.COMODO)
        );

        boolean resultado = envelope.verificarAcusacao(
                new Carta("Sr. Verde", TipoCarta.SUSPEITO),
                new Carta("Corda", TipoCarta.ARMA),
                new Carta("Cozinha", TipoCarta.COMODO)
        );

        assertFalse(resultado);
    }


    @Test
    public void deveRetornarFalseParaLocalErrado() {
        Envelope envelope = new Envelope(
                new Carta("Sr. Verde", TipoCarta.SUSPEITO),
                new Carta("Faca", TipoCarta.ARMA),
                new Carta("Cozinha", TipoCarta.COMODO)
        );

        boolean resultado = envelope.verificarAcusacao(
                new Carta("Sr. Verde", TipoCarta.SUSPEITO),
                new Carta("Faca", TipoCarta.ARMA),
                new Carta("Hall", TipoCarta.COMODO)
        );

        assertFalse(resultado);
    }
}