package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.GerenciadorDePartida;


public class GerenciadorInterface {

    public static void iniciar(GerenciadorDePartida gerenciadorPartida) {

        JanelaInicial inicial = new JanelaInicial();

        inicial.setAcaoNovoJogo(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JanelaPersonagens personagens =
                        new JanelaPersonagens(gerenciadorPartida);

                personagens.setVisible(true);

                inicial.dispose();
            }
        });

        inicial.setAcaoContinuar(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            

                JanelaJogo jogo =new JanelaJogo(gerenciadorPartida);

                jogo.setVisible(true);

                inicial.dispose();
            }
        });

        inicial.setVisible(true);
    }
}