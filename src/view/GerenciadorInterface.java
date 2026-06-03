package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.ControladorPartida;


public class GerenciadorInterface {

    public static void iniciar(ControladorPartida controlador) {

        JanelaInicial inicial = new JanelaInicial();

        inicial.setAcaoNovoJogo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JanelaPersonagens personagens = new JanelaPersonagens(controlador);
                personagens.setVisible(true);

                inicial.dispose();
            }
        });

        inicial.setAcaoContinuar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // se não houver jogadores, inicializa partida via controlador
                if (controlador.getJogadores().isEmpty()) {
                    controlador.adicionarJogador("Dani", "Miss Scarlet");
                    controlador.adicionarJogador("Judy", "Coronel Mustard");
                    controlador.iniciarPartida();
                }
                JanelaJogo jogo = new JanelaJogo(controlador);
                jogo.setVisible(true);

                inicial.dispose();
            }
        });

        inicial.setVisible(true);
    }
}